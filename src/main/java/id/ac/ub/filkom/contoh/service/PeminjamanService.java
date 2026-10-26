package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.DetailPeminjaman;
import id.ac.ub.filkom.contoh.entity.SalinanBuku;
import id.ac.ub.filkom.contoh.entity.StatusSalinan;
import id.ac.ub.filkom.contoh.entity.StatusTransaksi;
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.SalinanBukuRepository;
import id.ac.ub.filkom.contoh.repository.TransaksiPinjamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service: orkestrasi alur peminjaman sesuai sequence diagram
 * (cekAnggota -> cekStokBuku -> catatPeminjaman, atau batalkanTransaksi).
 */
@Service
public class PeminjamanService {

    public static final int LAMA_PINJAM_HARI = 7;

    private final AnggotaRepository anggotaRepository;
    private final SalinanBukuRepository salinanBukuRepository;
    private final TransaksiPinjamRepository transaksiPinjamRepository;

    public PeminjamanService(AnggotaRepository anggotaRepository,
                             SalinanBukuRepository salinanBukuRepository,
                             TransaksiPinjamRepository transaksiPinjamRepository) {
        this.anggotaRepository = anggotaRepository;
        this.salinanBukuRepository = salinanBukuRepository;
        this.transaksiPinjamRepository = transaksiPinjamRepository;
    }

    // barcode boleh berisi beberapa barcode, dipisah koma/spasi/baris baru
    @Transactional
    public TransaksiPinjam memprosesPeminjaman(String nim, String barcode) {
        Set<String> daftarBarcode = pecahBarcode(barcode);
        if (daftarBarcode.isEmpty()) {
            throw new IllegalArgumentException("Barcode salinan buku wajib diisi");
        }

        Anggota anggota = cekAnggota(nim);

        // alt: anggota tidak valid / kuota habis
        if (!anggota.validasiStatus()) {
            throw new IllegalStateException("Anggota " + anggota.getInfoAnggota() + " berstatus NONAKTIF");
        }
        if (!anggota.validasiKuota(daftarBarcode.size())) {
            throw new IllegalStateException("Kuota pinjaman tidak cukup: " + anggota.getJumlahPinjamanAktif()
                    + " aktif dari batas " + anggota.getBatasPinjaman()
                    + ", diminta " + daftarBarcode.size() + " buku");
        }

        List<SalinanBuku> daftarSalinan = new ArrayList<>();
        for (String b : daftarBarcode) {
            SalinanBuku salinan = cekStokBuku(b);
            // alt: buku sedang dipinjam
            if (!salinan.validasiKetersediaan()) {
                batalkanTransaksi("Salinan " + b + " (" + salinan.getBuku().getJudul() + ") sedang dipinjam");
            }
            daftarSalinan.add(salinan);
        }

        // alt: buku tersedia
        return catatPeminjaman(anggota, daftarSalinan);
    }

    public Anggota cekAnggota(String nim) {
        return anggotaRepository.findByNim(nim == null ? "" : nim.trim())
                .orElseThrow(() -> new IllegalArgumentException("Anggota dengan NIM " + nim + " tidak terdaftar"));
    }

    public SalinanBuku cekStokBuku(String barcode) {
        return salinanBukuRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Salinan dengan barcode " + barcode + " tidak ditemukan"));
    }

    @Transactional
    public TransaksiPinjam catatPeminjaman(Anggota anggota, List<SalinanBuku> daftarSalinan) {
        LocalDateTime sekarang = LocalDateTime.now();
        String idTransaksi = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TransaksiPinjam transaksi = new TransaksiPinjam(idTransaksi, anggota, sekarang,
                sekarang.toLocalDate().plusDays(LAMA_PINJAM_HARI));

        int nomor = 1;
        for (SalinanBuku salinan : daftarSalinan) {
            transaksi.tambahDetail(new DetailPeminjaman(idTransaksi + "-" + nomor++, salinan));   // mencatatDetailPeminjaman()
            salinan.ubahStatus(StatusSalinan.DIPINJAM);                                         // setDipinjam()
        }
        if (!transaksi.validasiTransaksi()) {
            batalkanTransaksi("Data transaksi tidak valid");
        }
        anggota.ubahJumlahPinjaman(transaksi.hitungJumlahItem());                                // updateKuota()
        return transaksiPinjamRepository.save(transaksi);
    }

    // Membatalkan proses: exception membuat @Transactional melakukan rollback
    public void batalkanTransaksi(String alasan) {
        throw new IllegalStateException("Transaksi dibatalkan: " + alasan);
    }

    public List<TransaksiPinjam> daftarTransaksi() {
        return transaksiPinjamRepository.findAllByOrderByTanggalPinjamDesc();
    }

    public TransaksiPinjam ambilTransaksi(String idTransaksi) {
        return transaksiPinjamRepository.findById(idTransaksi)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi " + idTransaksi + " tidak ditemukan"));
    }

    // Semua buku dikembalikan -> SELESAI
    @Transactional
    public TransaksiPinjam kembalikanBuku(String idTransaksi) {
        return akhiriTransaksi(idTransaksi, StatusTransaksi.SELESAI);
    }

    // Transaksi aktif dibatalkan petugas -> DIBATALKAN
    @Transactional
    public TransaksiPinjam batalkanPeminjaman(String idTransaksi) {
        return akhiriTransaksi(idTransaksi, StatusTransaksi.DIBATALKAN);
    }

    private TransaksiPinjam akhiriTransaksi(String idTransaksi, StatusTransaksi statusAkhir) {
        TransaksiPinjam transaksi = ambilTransaksi(idTransaksi);
        if (transaksi.getStatus() != StatusTransaksi.AKTIF) {
            throw new IllegalStateException("Transaksi " + idTransaksi + " sudah berstatus " + transaksi.getStatus());
        }
        for (DetailPeminjaman detail : transaksi.getDaftarDetail()) {
            detail.getSalinanBuku().ubahStatus(StatusSalinan.TERSEDIA);
        }
        transaksi.getAnggota().ubahJumlahPinjaman(-transaksi.hitungJumlahItem());
        transaksi.ubahStatus(statusAkhir);
        return transaksi;
    }

    private Set<String> pecahBarcode(String barcode) {
        Set<String> hasil = new LinkedHashSet<>();
        if (barcode != null) {
            for (String b : barcode.split("[,;\\s]+")) {
                if (!b.isBlank()) {
                    hasil.add(b.trim());
                }
            }
        }
        return hasil;
    }
}
