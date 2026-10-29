package com.kelompok.pinjambarang.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import com.kelompok.pinjambarang.viewmodel.PinjamViewModel

// Penghubung state dan event: layar menerima data serta callback, bukan mengubah ViewModel langsung.
// @Composable menandai fungsi yang digunakan dalam komposisi UI Compose.
@Composable
fun PinjamRoute(viewModel: PinjamViewModel = viewModel()) {
    // viewModel() memakai instance yang terkait pemilik lifecycle (Activity pada aplikasi ini).
    // M13: amati StateFlow sesuai lifecycle. Perubahan state memicu pembaruan Compose.
    // Delegasi `by` membuat nilai state dapat dibaca tanpa menuliskan .value.
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // M11: gunakan ukuran jendela saat ini, sehingga layout menyesuaikan rotasi/multi-window.
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    // Dua panel hanya jika lebar minimal 840 dp DAN tinggi minimal 480 dp.
    // Jendela yang tidak memenuhi kedua batas memakai satu kolom yang dapat di-scroll.
    val duaPanel = sizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
    ) && sizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND,
    )

    // UDF: state mengalir turun ke UI, sedangkan event UI diteruskan ke fungsi ViewModel.
    PinjamScreen(
        state = state,
        duaPanel = duaPanel,
        // Referensi fungsi (::) dipanggil oleh UI ketika pengguna mengetik/menekan tombol.
        onNamaBarangChange = viewModel::ubahNamaBarang,
        onNamaPeminjamChange = viewModel::ubahNamaPeminjam,
        onTambah = viewModel::tambahPinjaman,
        onKembali = viewModel::tandaiKembali,
        onHapus = viewModel::hapusPinjaman,
    )
}
