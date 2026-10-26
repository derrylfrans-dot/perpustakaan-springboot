package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import id.ac.ub.filkom.contoh.service.BukuService;
import id.ac.ub.filkom.contoh.service.PeminjamanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller peminjaman. Boundary-nya adalah FormTransaksi (templates/pinjam.html);
 * alur cekAnggota/cekStokBuku/catatPeminjaman dijalankan oleh PeminjamanService.
 */
@Controller
public class PinjamBukuController {

    private final PeminjamanService peminjamanService;
    private final BukuService bukuService;

    public PinjamBukuController(PeminjamanService peminjamanService, BukuService bukuService) {
        this.peminjamanService = peminjamanService;
        this.bukuService = bukuService;
    }

    @GetMapping("/")
    public String beranda() {
        return "redirect:/pinjam";
    }

    // FormTransaksi
    @GetMapping("/pinjam")
    public String formTransaksi(Model model) {
        model.addAttribute("salinanTersedia", bukuService.daftarSalinanTersedia());
        return "pinjam";
    }

    // menekanTombolProsesPinjam() -> memprosesPeminjaman()
    @PostMapping("/pinjam")
    public String memprosesPeminjaman(@RequestParam String nim,
                                      @RequestParam String barcode,
                                      RedirectAttributes ra) {
        try {
            TransaksiPinjam transaksi = peminjamanService.memprosesPeminjaman(nim, barcode);
            ra.addFlashAttribute("pesan", "Peminjaman berhasil dicatat");
            return "redirect:/transaksi/" + transaksi.getIdTransaksi();   // tampilkanBuktiPeminjaman()
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());           // tampilkanPesanError()
            ra.addFlashAttribute("nimInput", nim);
            ra.addFlashAttribute("barcodeInput", barcode);
            return "redirect:/pinjam";
        }
    }

    @GetMapping("/transaksi")
    public String daftarTransaksi(Model model) {
        model.addAttribute("daftarTransaksi", peminjamanService.daftarTransaksi());
        return "transaksi";
    }

    // Bukti peminjaman
    @GetMapping("/transaksi/{idTransaksi}")
    public String detailTransaksi(@PathVariable String idTransaksi, Model model) {
        model.addAttribute("transaksi", peminjamanService.ambilTransaksi(idTransaksi));
        return "transaksi-detail";
    }

    @PostMapping("/transaksi/{idTransaksi}/kembali")
    public String kembalikanBuku(@PathVariable String idTransaksi, RedirectAttributes ra) {
        try {
            peminjamanService.kembalikanBuku(idTransaksi);
            ra.addFlashAttribute("pesan", "Buku pada transaksi " + idTransaksi + " sudah dikembalikan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/transaksi/" + idTransaksi;
    }

    @PostMapping("/transaksi/{idTransaksi}/batal")
    public String batalkanPeminjaman(@PathVariable String idTransaksi, RedirectAttributes ra) {
        try {
            peminjamanService.batalkanPeminjaman(idTransaksi);
            ra.addFlashAttribute("pesan", "Transaksi " + idTransaksi + " dibatalkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/transaksi/" + idTransaksi;
    }
}
