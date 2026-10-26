package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Entity: rak buku, inventaris kampus yang bisa dipindah antar perpustakaan. */
@Entity
@Table(name = "rak_buku")
public class RakBuku {

    @Id
    @Column(name = "id_rak", length = 10)
    private String idRak;

    @Column(name = "kode_rak", length = 20, nullable = false)
    private String kodeRak;

    @Column(name = "lokasi", length = 150)
    private String lokasi;

    // Boleh null: rak sedang tidak dinaungi perpustakaan mana pun
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perpustakaan")
    private Perpustakaan perpustakaan;

    protected RakBuku() { }            // wajib ada untuk JPA

    public RakBuku(String idRak, String kodeRak, String lokasi, Perpustakaan perpustakaan) {
        this.idRak = idRak;
        this.kodeRak = kodeRak;
        this.lokasi = lokasi;
        this.perpustakaan = perpustakaan;
    }

    public String getInfoRak() {
        return kodeRak + " (" + lokasi + ")";
    }

    public void pindahKe(Perpustakaan perpustakaanBaru) {
        this.perpustakaan = perpustakaanBaru;
    }

    public String getIdRak()              { return idRak; }
    public String getKodeRak()            { return kodeRak; }
    public String getLokasi()             { return lokasi; }
    public Perpustakaan getPerpustakaan() { return perpustakaan; }
}
