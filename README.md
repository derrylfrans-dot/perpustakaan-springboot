# Sistem Manajemen Peminjaman Buku Perpustakaan (Spring Boot)

Tugas individu mata kuliah **Rekayasa Sistem Informasi** — Implementasi Software (materi 10a),
Fakultas Ilmu Komputer, Universitas Brawijaya.

| | |
|---|---|
| Nama | Derryl Smith Frans |
| NIM | 255150407111101 |
| Dosen | Dr. Achmad Arwan, S.Kom., M.Kom. |

Aplikasi web untuk studi kasus *Sistem Manajemen Peminjaman Buku Perpustakaan*, dikembangkan dari
contoh proyek [arwan07/Book_Springboot](https://github.com/arwan07/Book_Springboot) (entity `Buku`)
menjadi 7 entity sesuai Design Class Diagram: `Perpustakaan`, `RakBuku`, `Buku`, `SalinanBuku`,
`Anggota`, `TransaksiPinjam`, dan `DetailPeminjaman`.

## Fitur

- **Pinjam Buku** (`/pinjam`) — alur sesuai sequence diagram: `cekAnggota` → `cekStokBuku` → `catatPeminjaman`,
  termasuk alur alternatif anggota tidak valid / kuota habis / buku sedang dipinjam.
- **Transaksi** (`/transaksi`) — bukti peminjaman, pengembalian buku, dan pembatalan transaksi.
- **Katalog Buku** (`/buku`) — cari, tambah, hapus buku (komposisi: salinan ikut terhapus) dan kelola salinan.
- **Anggota** (`/anggota`) — daftar anggota baru dan ubah status AKTIF/NONAKTIF.
- **Perpustakaan & Rak** (`/perpustakaan`) — agregasi: rak tetap ada walau perpustakaan dihapus.

## Teknologi

Java 17, Spring Boot 3.3.5 (Spring Web, Thymeleaf, Spring Data JPA), database H2 in-memory, Maven, Docker.

## Menjalankan di Komputer Lokal

```bash
mvn spring-boot:run
```

Atau jalankan kelas `ContohApplication` lewat tombol **Run** di IDE, lalu buka:

- Aplikasi: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:perpustakaan`, user `sa`, password kosong)

Unit test entity:

```bash
mvn test
```
