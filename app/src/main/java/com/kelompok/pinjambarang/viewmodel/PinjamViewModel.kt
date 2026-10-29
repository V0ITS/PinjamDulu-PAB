package com.kelompok.pinjambarang.viewmodel

import androidx.lifecycle.ViewModel
import com.kelompok.pinjambarang.model.Pinjaman
import com.kelompok.pinjambarang.ui.PinjamUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// M12: logika aplikasi terpisah dari Compose dan bertahan saat perubahan konfigurasi.
// Data hanya di memori; tidak dipulihkan jika proses aplikasi berakhir.
class PinjamViewModel : ViewModel() {
    // M13: hanya ViewModel yang boleh mengubah state melalui MutableStateFlow privat ini.
    private val _uiState = MutableStateFlow(PinjamUiState())
    // UI menerima StateFlow read-only agar perubahan selalu melalui fungsi event (UDF).
    val uiState: StateFlow<PinjamUiState> = _uiState.asStateFlow()
    // Penghitung identitas catatan selama instance ViewModel ini hidup.
    private var idBerikutnya = 1

    // Event dari onValueChange kolom barang; hapus pesan lama saat pengguna memperbaiki input.
    fun ubahNamaBarang(teks: String) {
        // update mengganti state secara atomik; copy mempertahankan properti lainnya.
        _uiState.update { it.copy(namaBarang = teks, pesanError = null) }
    }

    // Event dari kolom peminjam memakai pola pembaruan state yang sama.
    fun ubahNamaPeminjam(teks: String) {
        _uiState.update { it.copy(namaPeminjam = teks, pesanError = null) }
    }

    // Event tombol tambah: validasi formulir, lalu terbitkan state baru untuk UI.
    fun tambahPinjaman() {
        // ID dibuat di luar update: lambda update harus bebas efek samping.
        // Lambda dapat dicoba ulang saat ada pembaruan bersamaan. Input tidak valid tetap
        // memakai satu nomor; ID boleh meloncat karena bukan nomor urut tampilan.
        val idBaru = idBerikutnya++
        _uiState.update { state ->
            // Hilangkan spasi awal/akhir sebelum memvalidasi dan menyimpan nama.
            val barang = state.namaBarang.trim()
            val peminjam = state.namaPeminjam.trim()
            // Tolak kolom kosong atau hanya spasi; pertahankan input dan daftar yang ada.
            if (barang.isBlank() || peminjam.isBlank()) {
                state.copy(pesanError = "Nama barang dan nama peminjam wajib diisi.")
            } else {
                // Setelah berhasil, kosongkan formulir dan hilangkan pesan validasi.
                state.copy(
                    namaBarang = "",
                    namaPeminjam = "",
                    // Operator + membuat daftar baru tanpa memodifikasi daftar state lama.
                    daftarPinjaman = state.daftarPinjaman + Pinjaman(idBaru, barang, peminjam),
                    pesanError = null,
                )
            }
        }
    }

    // Event pengembalian hanya mengubah catatan dengan ID yang dipilih pengguna.
    fun tandaiKembali(id: Int) {
        _uiState.update { state ->
            state.copy(
                // map menghasilkan daftar baru; catatan lain tetap memakai nilai semula.
                daftarPinjaman = state.daftarPinjaman.map { pinjaman ->
                    if (pinjaman.id == id) pinjaman.copy(sudahKembali = true)
                    else pinjaman
                },
            )
        }
    }

    // Event hapus mengeluarkan catatan terpilih dari state, lalu UI memperbarui daftar.
    fun hapusPinjaman(id: Int) {
        _uiState.update { state ->
            // filter mempertahankan semua catatan yang ID-nya berbeda dari target.
            state.copy(daftarPinjaman = state.daftarPinjaman.filter { it.id != id })
        }
    }
}
