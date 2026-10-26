package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.service.PerpustakaanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerpustakaanController {

    private final PerpustakaanService perpustakaanService;

    public PerpustakaanController(PerpustakaanService perpustakaanService) {
        this.perpustakaanService = perpustakaanService;
    }

    @GetMapping("/perpustakaan")
    public String daftarPerpustakaan(Model model) {
        model.addAttribute("daftarPerpustakaan", perpustakaanService.daftarPerpustakaan());
        model.addAttribute("daftarRak", perpustakaanService.daftarRak());
        model.addAttribute("rakTanpaPerpustakaan", perpustakaanService.daftarRakTanpaPerpustakaan());
        return "perpustakaan";
    }

    @PostMapping("/perpustakaan")
    public String tambahPerpustakaan(@RequestParam String idPerpustakaan,
                                     @RequestParam String nama,
                                     @RequestParam String lokasi,
                                     RedirectAttributes ra) {
        try {
            perpustakaanService.tambahPerpustakaan(idPerpustakaan, nama, lokasi);
            ra.addFlashAttribute("pesan", "Perpustakaan " + nama + " ditambahkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/perpustakaan";
    }

    @PostMapping("/perpustakaan/{idPerpustakaan}/hapus")
    public String hapusPerpustakaan(@PathVariable String idPerpustakaan, RedirectAttributes ra) {
        try {
            perpustakaanService.hapusPerpustakaan(idPerpustakaan);
            ra.addFlashAttribute("pesan", "Perpustakaan " + idPerpustakaan
                    + " dihapus; rak-raknya tetap ada sebagai inventaris kampus");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/perpustakaan";
    }

    @PostMapping("/rak")
    public String tambahRak(@RequestParam String idRak,
                            @RequestParam String kodeRak,
                            @RequestParam String lokasi,
                            @RequestParam(required = false) String idPerpustakaan,
                            RedirectAttributes ra) {
        try {
            perpustakaanService.tambahRak(idRak, kodeRak, lokasi, idPerpustakaan);
            ra.addFlashAttribute("pesan", "Rak " + kodeRak + " ditambahkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/perpustakaan";
    }

    @PostMapping("/rak/{idRak}/pindah")
    public String pindahRak(@PathVariable String idRak,
                            @RequestParam(required = false) String idPerpustakaan,
                            RedirectAttributes ra) {
        try {
            perpustakaanService.pindahRak(idRak, idPerpustakaan);
            ra.addFlashAttribute("pesan", "Rak " + idRak + " dipindahkan");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/perpustakaan";
    }
}
