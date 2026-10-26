package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.StatusAnggota;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnggotaService {

    private final AnggotaRepository anggotaRepository;

    public AnggotaService(AnggotaRepository anggotaRepository) {
        this.anggotaRepository = anggotaRepository;
    }

    public List<Anggota> daftarAnggota() {
        return anggotaRepository.findAll();
    }

    public Anggota tambahAnggota(String idAnggota, String nim, String nama,
                                 String email, int batasPinjaman) {
        if (anggotaRepository.existsById(idAnggota)) {
            throw new IllegalArgumentException("ID anggota " + idAnggota + " sudah dipakai");
        }
        if (anggotaRepository.existsByNim(nim)) {
            throw new IllegalArgumentException("NIM " + nim + " sudah terdaftar");
        }
        if (batasPinjaman < 1) {
            throw new IllegalArgumentException("Batas pinjaman minimal 1");
        }
        return anggotaRepository.save(new Anggota(idAnggota, nim, nama, email, batasPinjaman));
    }

    // AKTIF <-> NONAKTIF
    @Transactional
    public Anggota ubahStatus(String idAnggota) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> new IllegalArgumentException("Anggota " + idAnggota + " tidak ditemukan"));
        anggota.ubahStatus(anggota.validasiStatus() ? StatusAnggota.NONAKTIF : StatusAnggota.AKTIF);
        return anggota;
    }
}
