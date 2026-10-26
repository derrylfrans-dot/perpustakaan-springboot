package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Buku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository: akses data ke database.
 * Cukup interface; implementasinya dibuat otomatis oleh Spring.
 * Method bawaan: findAll(), findById(), save(), deleteById(), ...
 */
public interface BukuRepository extends JpaRepository<Buku, String> {

    // Query turunan dari nama method:
    // SELECT * FROM buku WHERE judul LIKE %kata%
    List<Buku> findByJudulContainingIgnoreCase(String kata);
}
