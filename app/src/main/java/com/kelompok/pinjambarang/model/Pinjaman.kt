package com.kelompok.pinjambarang.model

// Model: satu catatan peminjaman barang.
data class Pinjaman(
    val id: Int,
    val namaBarang: String,
    val namaPeminjam: String,
    val sudahKembali: Boolean = false,
)
