package com.kelompok.pinjambarang.ui

import com.kelompok.pinjambarang.model.Pinjaman

// M13: satu objek state menyatukan seluruh data yang diperlukan layar.
// ViewModel menghasilkan salinan baru melalui copy(), lalu UI menampilkan state tersebut.
data class PinjamUiState(
    // Teks yang sedang diketik pada kolom nama barang; awalnya kosong.
    val namaBarang: String = "",
    // Teks yang sedang diketik pada kolom nama peminjam.
    val namaPeminjam: String = "",
    // Daftar read-only bagi UI. Awalnya kosong dan belum disimpan ke database.
    val daftarPinjaman: List<Pinjaman> = emptyList(),
    // null berarti tidak ada pesan validasi yang perlu ditampilkan.
    val pesanError: String? = null,
)
