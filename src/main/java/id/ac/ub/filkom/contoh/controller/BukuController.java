package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.service.BukuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Controller: terima request, panggil service, pilih template. */
@Controller
public class BukuController {

    private final BukuService bukuService;

    public BukuController(BukuService bukuService) {
        this.bukuService = bukuService;
    }

    // GET http://localhost:8080/buku?kata=java
    @GetMapping("/buku")
    public String daftarBuku(@RequestParam(required = false) String kata,
                             Model model) {
        model.addAttribute("daftarBuku", bukuService.cariBuku(kata));
        model.addAttribute("kata", kata);
        return "buku";               // -> templates/buku.html
    }

    // POST dari form tambah buku
    @PostMapping("/buku")
    public String tambahBuku(@RequestParam String idBuku,
                             @RequestParam String isbn,
                             @RequestParam String judul,
                             @RequestParam String penulis,
                             @RequestParam String penerbit,
                             @RequestParam int tahunTerbit,
                             RedirectAttributes ra) {
        try {
            bukuService.tambahBuku(idBuku, isbn, judul, penulis, penerbit, tahunTerbit);
            ra.addFlashAttribute("pesan", "Buku " + judul + " ditambahkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/buku";     // pola Post-Redirect-Get
    }

    // GET http://localhost:8080/buku/B001 -> detail buku + daftar salinan
    @GetMapping("/buku/{idBuku}")
    public String detailBuku(@PathVariable String idBuku, Model model) {
        model.addAttribute("buku", bukuService.ambilBuku(idBuku));
        return "buku-detail";
    }

    @PostMapping("/buku/{idBuku}/salinan")
    public String tambahSalinan(@PathVariable String idBuku,
                                @RequestParam String idSalinan,
                                @RequestParam String barcode,
                                RedirectAttributes ra) {
        try {
            bukuService.tambahSalinan(idBuku, idSalinan, barcode);
            ra.addFlashAttribute("pesan", "Salinan " + barcode + " ditambahkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/buku/" + idBuku;
    }

    @PostMapping("/buku/{idBuku}/hapus")
    public String hapusBuku(@PathVariable String idBuku, RedirectAttributes ra) {
        try {
            bukuService.hapusBuku(idBuku);
            ra.addFlashAttribute("pesan", "Buku " + idBuku + " beserta seluruh salinannya dihapus");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/buku";
    }
}
