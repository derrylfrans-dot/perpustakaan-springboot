package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Entity: header transaksi peminjaman yang dibuat oleh satu anggota. */
@Entity
@Table(name = "transaksi_pinjam")
public class TransaksiPinjam {

    @Id
    @Column(name = "id_transaksi", length = 20)
    private String idTransaksi;

    @Column(name = "tanggal_pinjam", nullable = false)
    private LocalDateTime tanggalPinjam;

    @Column(name = "tanggal_jatuh_tempo", nullable = false)
    private LocalDate tanggalJatuhTempo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 12, nullable = false)
    private StatusTransaksi status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_anggota", nullable = false)
    private Anggota anggota;

    // Komposisi: detail tidak bermakna tanpa header transaksi
    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idDetail")
    private List<DetailPeminjaman> daftarDetail = new ArrayList<>();

    protected TransaksiPinjam() { }    // wajib ada untuk JPA

    public TransaksiPinjam(String idTransaksi, Anggota anggota,
                           LocalDateTime tanggalPinjam, LocalDate tanggalJatuhTempo) {
        this.idTransaksi = idTransaksi;
        this.anggota = anggota;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.status = StatusTransaksi.AKTIF;
    }

    public void tambahDetail(DetailPeminjaman detail) {
        detail.setTransaksi(this);
        daftarDetail.add(detail);
    }

    public int hitungJumlahItem() {
        return daftarDetail.size();
    }

    // Sah jika punya anggota, minimal 1 detail, dan jatuh tempo tidak sebelum tanggal pinjam
    public boolean validasiTransaksi() {
        return anggota != null
                && !daftarDetail.isEmpty()
                && !tanggalJatuhTempo.isBefore(tanggalPinjam.toLocalDate());
    }

    public boolean isTerlambat() {
        return status == StatusTransaksi.AKTIF && LocalDate.now().isAfter(tanggalJatuhTempo);
    }

    public void ubahStatus(StatusTransaksi statusBaru) {
        this.status = statusBaru;
    }

    public String getIdTransaksi()                  { return idTransaksi; }
    public LocalDateTime getTanggalPinjam()         { return tanggalPinjam; }
    public LocalDate getTanggalJatuhTempo()         { return tanggalJatuhTempo; }
    public StatusTransaksi getStatus()              { return status; }
    public Anggota getAnggota()                     { return anggota; }
    public List<DetailPeminjaman> getDaftarDetail() { return daftarDetail; }
}
