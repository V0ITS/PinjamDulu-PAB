package com.kelompok.pinjambarang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.kelompok.pinjambarang.ui.PinjamRoute

// Titik masuk aplikasi. ComponentActivity menyediakan integrasi lifecycle dengan Compose.
class MainActivity : ComponentActivity() {
    // Android memanggil onCreate ketika Activity dibuat, termasuk setelah rotasi layar.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Jalankan inisialisasi bawaan Activity sebelum menyiapkan tampilan sendiri.
        super.onCreate(savedInstanceState)
        // Izinkan konten memakai area di belakang system bar; inset ditangani di PinjamScreen.
        enableEdgeToEdge()
        // Tetapkan UI berbasis Compose sebagai isi Activity, tanpa layout XML.
        setContent {
            // Tema Material 3 menyediakan warna, tipografi, dan bentuk untuk komponen UI.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    // Warna utama dipakai komponen seperti tombol dan kolom yang mendapat fokus.
                    primary = Color(0xFF246B56),
                    // Warna latar label status peminjaman.
                    secondaryContainer = Color(0xFFD5EDE2),
                ),
            ) {
                // Route menghubungkan ViewModel dengan tampilan dan event pengguna.
                PinjamRoute()
            }
        }
    }
}
