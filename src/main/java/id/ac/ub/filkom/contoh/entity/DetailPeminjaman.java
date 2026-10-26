package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Entity: satu baris transaksi yang menunjuk ke satu SalinanBuku yang dipinjam. */
@Entity
@Table(name = "detail_peminjaman")
public class DetailPeminjaman {

    @Id
    @Column(name = "id_detail", length = 25)
    private String idDetail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_transaksi", nullable = false)
    private TransaksiPinjam transaksi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_salinan", nullable = false)
    private SalinanBuku salinanBuku;

    protected DetailPeminjaman() { }   // wajib ada untuk JPA

    public DetailPeminjaman(String idDetail, SalinanBuku salinanBuku) {
        this.idDetail = idDetail;
        this.salinanBuku = salinanBuku;
    }

    public String getInfoDetail() {
        return salinanBuku.getBarcode() + " - " + salinanBuku.getBuku().getJudul();
    }

    // Dipanggil oleh TransaksiPinjam.tambahDetail()
    void setTransaksi(TransaksiPinjam transaksi) {
        this.transaksi = transaksi;
    }

    public String getIdDetail()           { return idDetail; }
    public TransaksiPinjam getTransaksi() { return transaksi; }
    public SalinanBuku getSalinanBuku()   { return salinanBuku; }
}
