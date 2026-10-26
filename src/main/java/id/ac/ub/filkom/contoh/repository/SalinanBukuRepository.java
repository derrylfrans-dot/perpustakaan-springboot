package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.SalinanBuku;
import id.ac.ub.filkom.contoh.entity.StatusSalinan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalinanBukuRepository extends JpaRepository<SalinanBuku, String> {

    Optional<SalinanBuku> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    List<SalinanBuku> findByStatusOrderByBarcode(StatusSalinan status);
}
