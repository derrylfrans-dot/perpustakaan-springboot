package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.SalinanBuku;
import id.ac.ub.filkom.contoh.entity.StatusSalinan;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.DetailPeminjamanRepository;
import id.ac.ub.filkom.contoh.repository.SalinanBukuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Service: tempat logika bisnis/aturan aplikasi. */
@Service
public class BukuService {

    private final BukuRepository bukuRepository;
    private final SalinanBukuRepository salinanBukuRepository;
    private final DetailPeminjamanRepository detailPeminjamanRepository;

    // Dependency injection lewat constructor
    public BukuService(BukuRepository bukuRepository,
                       SalinanBukuRepository salinanBukuRepository,
                       DetailPeminjamanRepository detailPeminjamanRepository) {
        this.bukuRepository = bukuRepository;
        this.salinanBukuRepository = salinanBukuRepository;
        this.detailPeminjamanRepository = detailPeminjamanRepository;
    }

    public List<Buku> cariBuku(String kata) {
        if (kata == null || kata.isBlank()) {
            return bukuRepository.findAll();
        }
        return bukuRepository.findByJudulContainingIgnoreCase(kata.trim());
    }

    public Buku ambilBuku(String idBuku) {
        return bukuRepository.findById(idBuku)
                .orElseThrow(() -> new IllegalArgumentException("Buku " + idBuku + " tidak ditemukan"));
    }

    public List<SalinanBuku> daftarSalinanTersedia() {
        return salinanBukuRepository.findByStatusOrderByBarcode(StatusSalinan.TERSEDIA);
    }

    public Buku tambahBuku(String idBuku, String isbn, String judul,
                           String penulis, String penerbit, int tahunTerbit) {
        if (bukuRepository.existsById(idBuku)) {
            throw new IllegalArgumentException("ID buku " + idBuku + " sudah dipakai");
        }
        if (tahunTerbit < 0) {
            throw new IllegalArgumentException("Tahun terbit tidak valid");
        }
        return bukuRepository.save(new Buku(idBuku, isbn, judul, penulis, penerbit, tahunTerbit));
    }

    @Transactional
    public SalinanBuku tambahSalinan(String idBuku, String idSalinan, String barcode) {
        Buku buku = ambilBuku(idBuku);
        if (salinanBukuRepository.existsById(idSalinan)) {
            throw new IllegalArgumentException("ID salinan " + idSalinan + " sudah dipakai");
        }
        if (salinanBukuRepository.existsByBarcode(barcode)) {
            throw new IllegalArgumentException("Barcode " + barcode + " sudah dipakai");
        }
        return buku.tambahSalinan(idSalinan, barcode);   // tersimpan lewat cascade
    }

    // Komposisi: menghapus buku ikut menghapus seluruh salinannya
    @Transactional
    public void hapusBuku(String idBuku) {
        Buku buku = ambilBuku(idBuku);
        if (detailPeminjamanRepository.existsBySalinanBuku_Buku_IdBuku(idBuku)) {
            throw new IllegalStateException("Buku \"" + buku.getJudul()
                    + "\" tidak dapat dihapus karena salinannya tercatat di riwayat peminjaman");
        }
        bukuRepository.delete(buku);
    }
}
