package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Perpustakaan;
import id.ac.ub.filkom.contoh.entity.RakBuku;
import id.ac.ub.filkom.contoh.repository.PerpustakaanRepository;
import id.ac.ub.filkom.contoh.repository.RakBukuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerpustakaanService {

    private final PerpustakaanRepository perpustakaanRepository;
    private final RakBukuRepository rakBukuRepository;

    public PerpustakaanService(PerpustakaanRepository perpustakaanRepository,
                               RakBukuRepository rakBukuRepository) {
        this.perpustakaanRepository = perpustakaanRepository;
        this.rakBukuRepository = rakBukuRepository;
    }

    public List<Perpustakaan> daftarPerpustakaan() {
        return perpustakaanRepository.findAll();
    }

    public List<RakBuku> daftarRak() {
        return rakBukuRepository.findAllByOrderByKodeRak();
    }

    public List<RakBuku> daftarRakTanpaPerpustakaan() {
        return rakBukuRepository.findByPerpustakaanIsNullOrderByKodeRak();
    }

    public Perpustakaan tambahPerpustakaan(String idPerpustakaan, String nama, String lokasi) {
        if (perpustakaanRepository.existsById(idPerpustakaan)) {
            throw new IllegalArgumentException("ID perpustakaan " + idPerpustakaan + " sudah dipakai");
        }
        return perpustakaanRepository.save(new Perpustakaan(idPerpustakaan, nama, lokasi));
    }

    // idPerpustakaan kosong = rak disimpan sebagai inventaris tanpa perpustakaan
    public RakBuku tambahRak(String idRak, String kodeRak, String lokasi, String idPerpustakaan) {
        if (rakBukuRepository.existsById(idRak)) {
            throw new IllegalArgumentException("ID rak " + idRak + " sudah dipakai");
        }
        return rakBukuRepository.save(new RakBuku(idRak, kodeRak, lokasi, cariPerpustakaan(idPerpustakaan)));
    }

    @Transactional
    public void pindahRak(String idRak, String idPerpustakaan) {
        RakBuku rak = rakBukuRepository.findById(idRak)
                .orElseThrow(() -> new IllegalArgumentException("Rak " + idRak + " tidak ditemukan"));
        rak.pindahKe(cariPerpustakaan(idPerpustakaan));
    }

    // Agregasi: perpustakaan dihapus, rak-raknya dilepas dan tetap ada
    @Transactional
    public void hapusPerpustakaan(String idPerpustakaan) {
        Perpustakaan perpustakaan = cariPerpustakaan(idPerpustakaan);
        if (perpustakaan == null) {
            throw new IllegalArgumentException("Perpustakaan wajib dipilih");
        }
        for (RakBuku rak : new ArrayList<>(perpustakaan.getDaftarRak())) {
            rak.pindahKe(null);
        }
        perpustakaan.getDaftarRak().clear();
        perpustakaanRepository.delete(perpustakaan);
    }

    private Perpustakaan cariPerpustakaan(String idPerpustakaan) {
        if (idPerpustakaan == null || idPerpustakaan.isBlank()) {
            return null;
        }
        return perpustakaanRepository.findById(idPerpustakaan)
                .orElseThrow(() -> new IllegalArgumentException("Perpustakaan " + idPerpustakaan + " tidak ditemukan"));
    }
}
