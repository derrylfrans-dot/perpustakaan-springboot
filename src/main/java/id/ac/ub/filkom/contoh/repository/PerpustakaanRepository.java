package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Perpustakaan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerpustakaanRepository extends JpaRepository<Perpustakaan, String> {
}
