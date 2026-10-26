package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** Entity: unit perpustakaan yang menaungi beberapa RakBuku. */
@Entity
@Table(name = "perpustakaan")
public class Perpustakaan {

    @Id
    @Column(name = "id_perpustakaan", length = 10)
    private String idPerpustakaan;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "lokasi", length = 150)
    private String lokasi;

    // Agregasi: tanpa cascade, rak tetap ada walau perpustakaan dihapus
    @OneToMany(mappedBy = "perpustakaan")
    @OrderBy("kodeRak")
    private List<RakBuku> daftarRak = new ArrayList<>();

    protected Perpustakaan() { }       // wajib ada untuk JPA

    public Perpustakaan(String idPerpustakaan, String nama, String lokasi) {
        this.idPerpustakaan = idPerpustakaan;
        this.nama = nama;
        this.lokasi = lokasi;
    }

    public String getInfoPerpustakaan() {
        return nama + " (" + lokasi + ")";
    }

    public String getIdPerpustakaan()   { return idPerpustakaan; }
    public String getNama()             { return nama; }
    public String getLokasi()           { return lokasi; }
    public List<RakBuku> getDaftarRak() { return daftarRak; }
}
