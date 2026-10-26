package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.DetailPeminjaman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailPeminjamanRepository extends JpaRepository<DetailPeminjaman, String> {

    // Apakah ada salinan dari buku ini yang pernah tercatat dalam peminjaman?
    boolean existsBySalinanBuku_Buku_IdBuku(String idBuku);
}
