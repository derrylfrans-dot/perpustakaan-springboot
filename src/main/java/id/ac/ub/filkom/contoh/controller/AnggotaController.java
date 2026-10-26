package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.service.AnggotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AnggotaController {

    private final AnggotaService anggotaService;

    public AnggotaController(AnggotaService anggotaService) {
        this.anggotaService = anggotaService;
    }

    @GetMapping("/anggota")
    public String daftarAnggota(Model model) {
        model.addAttribute("daftarAnggota", anggotaService.daftarAnggota());
        return "anggota";
    }

    @PostMapping("/anggota")
    public String tambahAnggota(@RequestParam String idAnggota,
                                @RequestParam String nim,
                                @RequestParam String nama,
                                @RequestParam String email,
                                @RequestParam int batasPinjaman,
                                RedirectAttributes ra) {
        try {
            anggotaService.tambahAnggota(idAnggota, nim, nama, email, batasPinjaman);
            ra.addFlashAttribute("pesan", "Anggota " + nama + " terdaftar");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/anggota";
    }

    @PostMapping("/anggota/{idAnggota}/status")
    public String ubahStatus(@PathVariable String idAnggota, RedirectAttributes ra) {
        try {
            var anggota = anggotaService.ubahStatus(idAnggota);
            ra.addFlashAttribute("pesan", anggota.getNama() + " sekarang " + anggota.getStatusAnggota());
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("pesanError", e.getMessage());
        }
        return "redirect:/anggota";
    }
}
