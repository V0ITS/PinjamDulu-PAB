package com.kelompok.pinjambarang.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kelompok.pinjambarang.model.Pinjaman

@Composable
fun PinjamScreen(
    state: PinjamUiState,
    duaPanel: Boolean,
    onNamaBarangChange: (String) -> Unit,
    onNamaPeminjamChange: (String) -> Unit,
    onTambah: () -> Unit,
    onKembali: (Int) -> Unit,
    onHapus: (Int) -> Unit,
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).imePadding(),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (duaPanel) {
                Row(
                    modifier = Modifier.widthIn(max = 1200.dp).fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        JudulAplikasi()
                        FormPinjaman(state, onNamaBarangChange, onNamaPeminjamChange, onTambah)
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                    ) {
                        item { JudulDaftar(state.daftarPinjaman.size) }
                        if (state.daftarPinjaman.isEmpty()) {
                            item { DataKosong() }
                        }
                        items(state.daftarPinjaman, key = { it.id }) { pinjaman ->
                            KartuPinjaman(pinjaman, onKembali, onHapus)
                        }
                    }
                }
            } else {
                // Form dan daftar berada dalam satu scroll agar aman saat keyboard tampil.
                LazyColumn(
                    modifier = Modifier.widthIn(max = 600.dp).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item { JudulAplikasi() }
                    item { FormPinjaman(state, onNamaBarangChange, onNamaPeminjamChange, onTambah) }
                    item { JudulDaftar(state.daftarPinjaman.size) }
                    if (state.daftarPinjaman.isEmpty()) {
                        item { DataKosong() }
                    }
                    items(state.daftarPinjaman, key = { it.id }) { pinjaman ->
                        KartuPinjaman(pinjaman, onKembali, onHapus)
                    }
                }
            }
        }
    }
}

@Composable
private fun JudulAplikasi() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            "Pinjam Barang",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() },
        )
        Text("Catat barang yang dipinjam teman.")
        Text("Data sementara selama aplikasi berjalan.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun FormPinjaman(
    state: PinjamUiState,
    onNamaBarangChange: (String) -> Unit,
    onNamaPeminjamChange: (String) -> Unit,
    onTambah: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Tambah pinjaman", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = state.namaBarang,
            onValueChange = onNamaBarangChange,
            label = { Text("Nama barang") },
            singleLine = true,
            isError = state.pesanError != null && state.namaBarang.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.namaPeminjam,
            onValueChange = onNamaPeminjamChange,
            label = { Text("Nama peminjam") },
            singleLine = true,
            isError = state.pesanError != null && state.namaPeminjam.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )
        state.pesanError?.let { pesan ->
            Text(
                text = pesan,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
        Button(onClick = onTambah, modifier = Modifier.fillMaxWidth()) {
            Text("Tambah pinjaman")
        }
    }
}

@Composable
private fun JudulDaftar(jumlah: Int) {
    Text("Daftar pinjaman ($jumlah)", style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun DataKosong() {
    Text("Belum ada pinjaman. Isi nama barang dan peminjam, lalu tekan Tambah pinjaman.")
}

@Composable
private fun KartuPinjaman(
    pinjaman: Pinjaman,
    onKembali: (Int) -> Unit,
    onHapus: (Int) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(pinjaman.namaBarang, style = MaterialTheme.typography.titleMedium)
            Text("Peminjam: ${pinjaman.namaPeminjam}")
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = if (pinjaman.sudahKembali) "Sudah kembali" else "Masih dipinjam",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
            // Tombol ditumpuk supaya tetap muat pada panel sempit dan font besar.
            if (!pinjaman.sudahKembali) {
                TextButton(onClick = { onKembali(pinjaman.id) }) { Text("Tandai kembali") }
            }
            TextButton(onClick = { onHapus(pinjaman.id) }) { Text("Hapus") }
        }
    }
}

@Preview(name = "Sempit", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Font besar", widthDp = 360, heightDp = 800, fontScale = 1.5f, showBackground = true)
@Composable
private fun PinjamScreenPreview() {
    ContohLayar(duaPanel = false)
}

@Preview(name = "Lebar", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
private fun PinjamLebarPreview() {
    ContohLayar(duaPanel = true)
}

@Composable
private fun ContohLayar(duaPanel: Boolean) {
    MaterialTheme {
        PinjamScreen(
            state = PinjamUiState(daftarPinjaman = listOf(Pinjaman(1, "Payung", "Rina"))),
            duaPanel = duaPanel,
            onNamaBarangChange = {},
            onNamaPeminjamChange = {},
            onTambah = {},
            onKembali = {},
            onHapus = {},
        )
    }
}
