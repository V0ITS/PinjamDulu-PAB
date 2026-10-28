package com.kelompok.pinjambarang.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import com.kelompok.pinjambarang.viewmodel.PinjamViewModel

@Composable
fun PinjamRoute(viewModel: PinjamViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    // Kebijakan M11: expanded width dan minimal medium height.
    val duaPanel = sizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
    ) && sizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND,
    )

    PinjamScreen(
        state = state,
        duaPanel = duaPanel,
        onNamaBarangChange = viewModel::ubahNamaBarang,
        onNamaPeminjamChange = viewModel::ubahNamaPeminjam,
        onTambah = viewModel::tambahPinjaman,
        onKembali = viewModel::tandaiKembali,
        onHapus = viewModel::hapusPinjaman,
    )
}
