-- Perpustakaan & rak (R04 sengaja tanpa perpustakaan: inventaris independen)
INSERT INTO perpustakaan (id_perpustakaan, nama, lokasi) VALUES ('P01', 'Perpustakaan Pusat UB', 'Jl. Veteran, Malang');
INSERT INTO perpustakaan (id_perpustakaan, nama, lokasi) VALUES ('P02', 'Ruang Baca FILKOM', 'Gedung F FILKOM Lt. 2');

INSERT INTO rak_buku (id_rak, kode_rak, lokasi, id_perpustakaan) VALUES ('R01', 'A-01', 'Lantai 1 sayap kiri', 'P01');
INSERT INTO rak_buku (id_rak, kode_rak, lokasi, id_perpustakaan) VALUES ('R02', 'A-02', 'Lantai 1 sayap kanan', 'P01');
INSERT INTO rak_buku (id_rak, kode_rak, lokasi, id_perpustakaan) VALUES ('R03', 'F-01', 'Dekat meja sirkulasi', 'P02');
INSERT INTO rak_buku (id_rak, kode_rak, lokasi, id_perpustakaan) VALUES ('R04', 'G-01', 'Gudang inventaris', NULL);

-- Buku (katalog) & salinan fisiknya
INSERT INTO buku (id_buku, isbn, judul, penulis, penerbit, tahun_terbit) VALUES ('B001', '978-602-0000-01', 'Pemrograman Java', 'Abdul Kadir', 'Andi', 2020);
INSERT INTO buku (id_buku, isbn, judul, penulis, penerbit, tahun_terbit) VALUES ('B002', '978-602-0000-02', 'Rekayasa Perangkat Lunak', 'Roger S. Pressman', 'McGraw-Hill', 2019);
INSERT INTO buku (id_buku, isbn, judul, penulis, penerbit, tahun_terbit) VALUES ('B003', '978-602-0000-03', 'Basis Data', 'Fathansyah', 'Informatika', 2018);

INSERT INTO salinan_buku (id_salinan, barcode, status, id_buku) VALUES ('S001', 'BC-B001-1', 'TERSEDIA', 'B001');
INSERT INTO salinan_buku (id_salinan, barcode, status, id_buku) VALUES ('S002', 'BC-B001-2', 'TERSEDIA', 'B001');
INSERT INTO salinan_buku (id_salinan, barcode, status, id_buku) VALUES ('S003', 'BC-B001-3', 'TERSEDIA', 'B001');
INSERT INTO salinan_buku (id_salinan, barcode, status, id_buku) VALUES ('S004', 'BC-B003-1', 'TERSEDIA', 'B003');
INSERT INTO salinan_buku (id_salinan, barcode, status, id_buku) VALUES ('S005', 'BC-B003-2', 'TERSEDIA', 'B003');

-- Anggota (A003 NONAKTIF untuk mencoba alur "anggota tidak valid")
INSERT INTO anggota (id_anggota, nim, nama, email, status_anggota, jumlah_pinjaman_aktif, batas_pinjaman) VALUES ('A001', '255150401111001', 'Budi Santoso', 'budi@student.ub.ac.id', 'AKTIF', 0, 3);
INSERT INTO anggota (id_anggota, nim, nama, email, status_anggota, jumlah_pinjaman_aktif, batas_pinjaman) VALUES ('A002', '255150401111002', 'Siti Aminah', 'siti@student.ub.ac.id', 'AKTIF', 0, 1);
INSERT INTO anggota (id_anggota, nim, nama, email, status_anggota, jumlah_pinjaman_aktif, batas_pinjaman) VALUES ('A003', '255150401111003', 'Andi Wijaya', 'andi@student.ub.ac.id', 'NONAKTIF', 0, 3);
