package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Anggota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnggotaRepository extends JpaRepository<Anggota, String> {

    Optional<Anggota> findByNim(String nim);

    boolean existsByNim(String nim);
}
