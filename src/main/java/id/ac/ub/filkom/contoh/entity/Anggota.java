package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity: mahasiswa yang terdaftar sebagai anggota perpustakaan. */
@Entity
@Table(name = "anggota")
public class Anggota {

    @Id
    @Column(name = "id_anggota", length = 10)
    private String idAnggota;

    @Column(name = "nim", length = 20, nullable = false, unique = true)
    private String nim;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anggota", length = 10, nullable = false)
    private StatusAnggota statusAnggota;

    @Column(name = "jumlah_pinjaman_aktif")
    private int jumlahPinjamanAktif;

    @Column(name = "batas_pinjaman")
    private int batasPinjaman;

    protected Anggota() { }            // wajib ada untuk JPA

    public Anggota(String idAnggota, String nim, String nama, String email, int batasPinjaman) {
        this.idAnggota = idAnggota;
        this.nim = nim;
        this.nama = nama;
        this.email = email;
        this.batasPinjaman = batasPinjaman;
        this.statusAnggota = StatusAnggota.AKTIF;
        this.jumlahPinjamanAktif = 0;
    }

    public String getInfoAnggota() {
        return nim + " - " + nama;
    }

    public boolean validasiStatus() {
        return statusAnggota == StatusAnggota.AKTIF;
    }

    public boolean validasiKuota() {
        return validasiKuota(1);
    }

    // Kuota cukup jika pinjaman aktif + jumlah buku baru tidak melebihi batas
    public boolean validasiKuota(int jumlahBaru) {
        return jumlahPinjamanAktif + jumlahBaru <= batasPinjaman;
    }

    // delta positif saat meminjam, negatif saat mengembalikan
    public void ubahJumlahPinjaman(int delta) {
        int jumlahBaru = jumlahPinjamanAktif + delta;
        if (jumlahBaru < 0 || jumlahBaru > batasPinjaman) {
            throw new IllegalStateException("Jumlah pinjaman aktif di luar batas: " + jumlahBaru);
        }
        this.jumlahPinjamanAktif = jumlahBaru;
    }

    public void ubahStatus(StatusAnggota statusBaru) {
        this.statusAnggota = statusBaru;
    }

    public String getIdAnggota()            { return idAnggota; }
    public String getNim()                  { return nim; }
    public String getNama()                 { return nama; }
    public String getEmail()                { return email; }
    public StatusAnggota getStatusAnggota() { return statusAnggota; }
    public int getJumlahPinjamanAktif()     { return jumlahPinjamanAktif; }
    public int getBatasPinjaman()           { return batasPinjaman; }
}
