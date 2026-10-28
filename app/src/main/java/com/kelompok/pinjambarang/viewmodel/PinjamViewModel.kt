package com.kelompok.pinjambarang.viewmodel

import androidx.lifecycle.ViewModel
import com.kelompok.pinjambarang.model.Pinjaman
import com.kelompok.pinjambarang.ui.PinjamUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PinjamViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PinjamUiState())
    val uiState: StateFlow<PinjamUiState> = _uiState.asStateFlow()
    private var idBerikutnya = 1

    fun ubahNamaBarang(teks: String) {
        _uiState.update { it.copy(namaBarang = teks, pesanError = null) }
    }

    fun ubahNamaPeminjam(teks: String) {
        _uiState.update { it.copy(namaPeminjam = teks, pesanError = null) }
    }

    fun tambahPinjaman() {
        // ID dibuat di luar update: lambda update harus bebas efek samping.
        val idBaru = idBerikutnya++
        _uiState.update { state ->
            val barang = state.namaBarang.trim()
            val peminjam = state.namaPeminjam.trim()
            if (barang.isBlank() || peminjam.isBlank()) {
                state.copy(pesanError = "Nama barang dan nama peminjam wajib diisi.")
            } else {
                state.copy(
                    namaBarang = "",
                    namaPeminjam = "",
                    daftarPinjaman = state.daftarPinjaman + Pinjaman(idBaru, barang, peminjam),
                    pesanError = null,
                )
            }
        }
    }

    fun tandaiKembali(id: Int) {
        _uiState.update { state ->
            state.copy(
                daftarPinjaman = state.daftarPinjaman.map { pinjaman ->
                    if (pinjaman.id == id) pinjaman.copy(sudahKembali = true)
                    else pinjaman
                },
            )
        }
    }

    fun hapusPinjaman(id: Int) {
        _uiState.update { state ->
            state.copy(daftarPinjaman = state.daftarPinjaman.filter { it.id != id })
        }
    }
}
