package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** Entity: data bibliografi buku (judul katalog), dipetakan ke tabel "buku". */
@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @Column(name = "id_buku", length = 10)
    private String idBuku;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "judul", length = 200, nullable = false)
    private String judul;

    @Column(name = "penulis", length = 100)
    private String penulis;

    @Column(name = "penerbit", length = 100)
    private String penerbit;

    @Column(name = "tahun_terbit")
    private int tahunTerbit;

    // Komposisi: salinan ikut tersimpan & terhapus bersama bukunya
    @OneToMany(mappedBy = "buku", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idSalinan")
    private List<SalinanBuku> daftarSalinan = new ArrayList<>();

    protected Buku() { }               // wajib ada untuk JPA

    public Buku(String idBuku, String isbn, String judul, String penulis, String penerbit, int tahunTerbit) {
        this.idBuku = idBuku;
        this.isbn = isbn;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
    }

    public String getInfoBuku() {
        return judul + " - " + penulis + " (" + penerbit + ", " + tahunTerbit + ")";
    }

    public SalinanBuku tambahSalinan(String idSalinan, String barcode) {
        SalinanBuku salinan = new SalinanBuku(idSalinan, barcode, this);
        daftarSalinan.add(salinan);
        return salinan;
    }

    // Stok = jumlah eksemplar yang sedang TERSEDIA
    public long getStokTersedia() {
        return daftarSalinan.stream().filter(SalinanBuku::validasiKetersediaan).count();
    }

    public String getIdBuku()                   { return idBuku; }
    public String getIsbn()                     { return isbn; }
    public String getJudul()                    { return judul; }
    public String getPenulis()                  { return penulis; }
    public String getPenerbit()                 { return penerbit; }
    public int getTahunTerbit()                 { return tahunTerbit; }
    public List<SalinanBuku> getDaftarSalinan() { return daftarSalinan; }
}
