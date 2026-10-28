package com.kelompok.pinjambarang.ui

import com.kelompok.pinjambarang.model.Pinjaman

// Satu sumber data untuk seluruh tampilan layar.
data class PinjamUiState(
    val namaBarang: String = "",
    val namaPeminjam: String = "",
    val daftarPinjaman: List<Pinjaman> = emptyList(),
    val pesanError: String? = null,
)
