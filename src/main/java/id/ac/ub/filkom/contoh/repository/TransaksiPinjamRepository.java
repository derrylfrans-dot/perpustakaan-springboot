package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaksiPinjamRepository extends JpaRepository<TransaksiPinjam, String> {

    List<TransaksiPinjam> findAllByOrderByTanggalPinjamDesc();
}
