package com.kelompok.pinjambarang.model

// Model pada MVVM (M12): merepresentasikan satu catatan peminjaman barang.
// data class menyediakan copy(); properti val membuat nilai tidak bisa ditetapkan ulang.
data class Pinjaman(
    // Identitas unik untuk memilih catatan saat dikembalikan/dihapus dan sebagai key daftar UI.
    val id: Int,
    // Nama barang yang dipinjamkan, misalnya Payung.
    val namaBarang: String,
    // Nama teman yang meminjam barang tersebut.
    val namaPeminjam: String,
    // Catatan baru berstatus masih dipinjam; true berarti barang sudah dikembalikan.
    val sudahKembali: Boolean = false,
)
