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

// View pada MVVM: merender state dan mengirim event melalui callback (UDF).
// Logika validasi/perubahan data berada di ViewModel sehingga UI mudah dipreview.
@Composable
fun PinjamScreen(
    // Seluruh nilai formulir, daftar, dan pesan yang akan ditampilkan.
    state: PinjamUiState,
    // Keputusan layout dari ukuran jendela yang dihitung di PinjamRoute.
    duaPanel: Boolean,
    // Callback mengetik mengirim nilai terbaru; callback tombol mengirim aksi atau ID catatan.
    onNamaBarangChange: (String) -> Unit,
    onNamaPeminjamChange: (String) -> Unit,
    onTambah: () -> Unit,
    onKembali: (Int) -> Unit,
    onHapus: (Int) -> Unit,
) {
    // Scaffold menyediakan inset agar konten tidak tertutup status bar/navigation bar.
    Scaffold { innerPadding ->
        // Box memusatkan konten secara horizontal. imePadding memberi ruang untuk keyboard.
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).imePadding(),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (duaPanel) {
                // M11: pada jendela lebar, letakkan form dan daftar berdampingan.
                // Batasi lebar konten agar tidak terlalu melebar pada perangkat besar.
                Row(
                    modifier = Modifier.widthIn(max = 1200.dp).fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    // weight(1f) membagi ruang secara seimbang dengan daftar di sebelah kanan.
                    // Scroll form terpisah dari scroll daftar pada mode dua panel.
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        JudulAplikasi()
                        FormPinjaman(state, onNamaBarangChange, onNamaPeminjamChange, onTambah)
                    }
                    // LazyColumn menyusun item sesuai kebutuhan area tampilan, cocok untuk daftar.
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                    ) {
                        // Jumlah di judul selalu dihitung dari state, bukan disimpan terpisah.
                        item { JudulDaftar(state.daftarPinjaman.size) }
                        if (state.daftarPinjaman.isEmpty()) {
                            item { DataKosong() }
                        }
                        // Key stabil membantu Compose mengenali item ketika daftar berubah.
                        items(state.daftarPinjaman, key = { it.id }) { pinjaman ->
                            KartuPinjaman(pinjaman, onKembali, onHapus)
                        }
                    }
                }
            } else {
                // Form dan daftar berada dalam satu scroll agar aman saat keyboard tampil.
                // Spasi antaritem dan padding menjaga keterbacaan di layar sempit (M11).
                LazyColumn(
                    modifier = Modifier.widthIn(max = 600.dp).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item { JudulAplikasi() }
                    item { FormPinjaman(state, onNamaBarangChange, onNamaPeminjamChange, onTambah) }
                    item { JudulDaftar(state.daftarPinjaman.size) }
                    // Petunjuk awal hanya tampil jika belum ada catatan.
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

// Komponen judul dipakai bersama oleh layout satu kolom maupun dua panel.
@Composable
private fun JudulAplikasi() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            "Pinjam Barang",
            style = MaterialTheme.typography.headlineMedium,
            // Semantics menandai judul untuk membantu navigasi pembaca layar.
            modifier = Modifier.semantics { heading() },
        )
        Text("Catat barang yang dipinjam teman.")
        // Beri tahu pengguna bahwa aplikasi ini belum menyimpan data secara permanen.
        Text("Data sementara selama aplikasi berjalan.", style = MaterialTheme.typography.bodySmall)
    }
}

// Form tidak menyimpan state sendiri: value berasal dari state dan perubahan lewat callback.
@Composable
private fun FormPinjaman(
    state: PinjamUiState,
    onNamaBarangChange: (String) -> Unit,
    onNamaPeminjamChange: (String) -> Unit,
    onTambah: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Tambah pinjaman", style = MaterialTheme.typography.titleLarge)
        // Kolom pertama menerapkan alur state turun (value), event naik (onValueChange).
        OutlinedTextField(
            value = state.namaBarang,
            onValueChange = onNamaBarangChange,
            label = { Text("Nama barang") },
            // Nama hanya satu baris; bukan input paragraf.
            singleLine = true,
            // Tandai error hanya jika validasi gagal dan kolom ini masih kosong.
            isError = state.pesanError != null && state.namaBarang.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )
        // Kolom kedua mengirim perubahan nama peminjam ke ViewModel melalui Route.
        OutlinedTextField(
            value = state.namaPeminjam,
            onValueChange = onNamaPeminjamChange,
            label = { Text("Nama peminjam") },
            singleLine = true,
            isError = state.pesanError != null && state.namaPeminjam.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )
        // Safe call ?.let menampilkan pesan hanya ketika pesanError bukan null.
        state.pesanError?.let { pesan ->
            Text(
                text = pesan,
                color = MaterialTheme.colorScheme.error,
                // Pembaca layar dapat mengumumkan pesan baru tanpa memotong ucapan aktif.
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
        // Klik hanya mengirim event; validasi dan penambahan dilakukan ViewModel.
        Button(onClick = onTambah, modifier = Modifier.fillMaxWidth()) {
            Text("Tambah pinjaman")
        }
    }
}

// Interpolasi $jumlah menampilkan banyaknya catatan sesuai state terbaru.
@Composable
private fun JudulDaftar(jumlah: Int) {
    Text("Daftar pinjaman ($jumlah)", style = MaterialTheme.typography.titleLarge)
}

// Empty state menjelaskan tindakan pertama agar pengguna tidak melihat daftar kosong saja.
@Composable
private fun DataKosong() {
    Text("Belum ada pinjaman. Isi nama barang dan peminjam, lalu tekan Tambah pinjaman.")
}

// Satu kartu untuk satu model Pinjaman; ID dikirim ketika tombol aksi ditekan.
@Composable
private fun KartuPinjaman(
    pinjaman: Pinjaman,
    onKembali: (Int) -> Unit,
    onHapus: (Int) -> Unit,
) {
    // Card mengelompokkan informasi dan aksi agar batas setiap catatan terlihat jelas.
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(pinjaman.namaBarang, style = MaterialTheme.typography.titleMedium)
            Text("Peminjam: ${pinjaman.namaPeminjam}")
            // Label status memakai warna dan teks, sehingga maknanya tidak bergantung warna saja.
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    // Teks status berasal langsung dari model yang diterima layar.
                    text = if (pinjaman.sudahKembali) "Sudah kembali" else "Masih dipinjam",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
            // Tombol ditumpuk supaya tetap muat pada panel sempit dan font besar.
            // Setelah kembali, tombol pengembalian tidak perlu ditampilkan lagi.
            if (!pinjaman.sudahKembali) {
                TextButton(onClick = { onKembali(pinjaman.id) }) { Text("Tandai kembali") }
            }
            // Aksi hapus tersedia untuk kedua status; hanya ID kartu ini yang diteruskan.
            TextButton(onClick = { onHapus(pinjaman.id) }) { Text("Hapus") }
        }
    }
}

// Preview Android Studio untuk memeriksa layar sempit dan teks yang diperbesar (M11).
// Satuan widthDp/heightDp adalah dp, bukan piksel fisik layar.
@Preview(name = "Sempit", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Font besar", widthDp = 360, heightDp = 800, fontScale = 1.5f, showBackground = true)
@Composable
private fun PinjamScreenPreview() {
    ContohLayar(duaPanel = false)
}

// Preview lebar menampilkan form dan daftar dalam dua panel.
@Preview(name = "Lebar", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
private fun PinjamLebarPreview() {
    ContohLayar(duaPanel = true)
}

// Data contoh khusus preview: tidak membuat ViewModel dan tidak mengubah data aplikasi.
@Composable
private fun ContohLayar(duaPanel: Boolean) {
    MaterialTheme {
        PinjamScreen(
            state = PinjamUiState(daftarPinjaman = listOf(Pinjaman(1, "Payung", "Rina"))),
            duaPanel = duaPanel,
            // Callback kosong cukup untuk preview tampilan statis.
            onNamaBarangChange = {},
            onNamaPeminjamChange = {},
            onTambah = {},
            onKembali = {},
            onHapus = {},
        )
    }
}
