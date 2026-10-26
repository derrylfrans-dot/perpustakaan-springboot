package id.ac.ub.filkom.contoh.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Unit test method entity yang dipanggil PeminjamanService. Berjalan tanpa database dan server. */
class PeminjamanEntityTest {

    @Test
    void validasiStatus_dan_validasiKuota_anggota() {
        Anggota anggota = new Anggota("A009", "255150407111101", "Derryl Smith Frans",
                "derryl@student.ub.ac.id", 2);
        assertTrue(anggota.validasiStatus());
        assertTrue(anggota.validasiKuota(2));
        assertFalse(anggota.validasiKuota(3));

        anggota.ubahJumlahPinjaman(2);
        assertFalse(anggota.validasiKuota());
        assertThrows(IllegalStateException.class, () -> anggota.ubahJumlahPinjaman(1));

        anggota.ubahStatus(StatusAnggota.NONAKTIF);
        assertFalse(anggota.validasiStatus());
    }

    @Test
    void stokTersedia_mengikuti_status_salinan() {
        Buku buku = new Buku("B009", "978-602-0000-09", "Rekayasa Sistem Informasi", "Penulis", "Penerbit", 2026);
        SalinanBuku salinan1 = buku.tambahSalinan("S091", "BC-B009-1");
        buku.tambahSalinan("S092", "BC-B009-2");
        assertEquals(2, buku.getStokTersedia());

        salinan1.ubahStatus(StatusSalinan.DIPINJAM);
        assertFalse(salinan1.validasiKetersediaan());
        assertEquals(1, buku.getStokTersedia());
    }

    @Test
    void validasiTransaksi_butuh_minimal_satu_detail() {
        Anggota anggota = new Anggota("A009", "255150407111101", "Derryl Smith Frans",
                "derryl@student.ub.ac.id", 3);
        Buku buku = new Buku("B009", "978-602-0000-09", "Rekayasa Sistem Informasi", "Penulis", "Penerbit", 2026);
        SalinanBuku salinan = buku.tambahSalinan("S091", "BC-B009-1");

        LocalDateTime sekarang = LocalDateTime.now();
        TransaksiPinjam transaksi = new TransaksiPinjam("TRX-UJI", anggota, sekarang,
                sekarang.toLocalDate().plusDays(7));
        assertFalse(transaksi.validasiTransaksi());

        transaksi.tambahDetail(new DetailPeminjaman("TRX-UJI-1", salinan));
        assertTrue(transaksi.validasiTransaksi());
        assertEquals(1, transaksi.hitungJumlahItem());
        assertSame(transaksi, transaksi.getDaftarDetail().get(0).getTransaksi());
        assertFalse(transaksi.isTerlambat());
    }
}
