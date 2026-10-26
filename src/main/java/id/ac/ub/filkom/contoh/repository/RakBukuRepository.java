package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.RakBuku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RakBukuRepository extends JpaRepository<RakBuku, String> {

    List<RakBuku> findAllByOrderByKodeRak();

    // Rak yang tidak dinaungi perpustakaan mana pun
    List<RakBuku> findByPerpustakaanIsNullOrderByKodeRak();
}
