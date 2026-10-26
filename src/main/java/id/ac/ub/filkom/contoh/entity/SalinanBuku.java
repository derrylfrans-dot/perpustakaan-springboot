package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Entity: eksemplar fisik sebuah buku. Tidak bisa eksis tanpa Buku induk. */
@Entity
@Table(name = "salinan_buku")
public class SalinanBuku {

    @Id
    @Column(name = "id_salinan", length = 10)
    private String idSalinan;

    @Column(name = "barcode", length = 30, nullable = false, unique = true)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private StatusSalinan status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_buku", nullable = false)
    private Buku buku;

    protected SalinanBuku() { }        // wajib ada untuk JPA

    // Hanya dibuat lewat Buku.tambahSalinan()
    SalinanBuku(String idSalinan, String barcode, Buku buku) {
        this.idSalinan = idSalinan;
        this.barcode = barcode;
        this.buku = buku;
        this.status = StatusSalinan.TERSEDIA;
    }

    public boolean validasiKetersediaan() {
        return status == StatusSalinan.TERSEDIA;
    }

    public void ubahStatus(StatusSalinan statusBaru) {
        this.status = statusBaru;
    }

    public String getIdSalinan()     { return idSalinan; }
    public String getBarcode()       { return barcode; }
    public StatusSalinan getStatus() { return status; }
    public Buku getBuku()            { return buku; }
}
