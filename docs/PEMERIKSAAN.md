# Pemeriksaan tugas Pinjam Barang

## Build dan unit test

Lingkungan pemeriksaan: Windows, JDK 21, Gradle Wrapper 9.6.0, Android Gradle Plugin 9.4.1, compile SDK 37.

| Pemeriksaan | Hasil |
| --- | --- |
| `:app:assembleDebug` | Berhasil; APK tersedia di `app/build/outputs/apk/debug/app-debug.apk` |
| `:app:testDebugUnitTest` | 3 test berhasil, 0 gagal, 0 error |
| `:app:lintDebug` | Berhasil; 0 error, 9 warning |

Warning lint berkaitan dengan rekomendasi versi dependensi/target SDK terbaru, konfigurasi backup, dan ikon launcher. Tidak ada error kompilasi/lint. Aplikasi hanya menyimpan data di memori dan menggunakan target SDK 36; compile SDK 37 diperlukan oleh Adaptive 1.3.0 dari materi M11.

Tiga unit test memeriksa:

1. State awal kosong; input kosong atau hanya spasi tidak menambah data.
2. Input valid dibersihkan dari spasi tepi, catatan ditambah, input dikosongkan, dan pesan error dihapus.
3. Dua catatan bernama sama mempunyai ID berbeda; kembali/hapus hanya memengaruhi ID terpilih, snapshot lama tetap utuh, dan penghapusan terakhir menghasilkan daftar kosong.

## Skenario demo

**Status runtime:** belum terverifikasi. Emulator API 35 dan 37 pada lingkungan pengerjaan mengalami boot tidak stabil/ADB timeout sebelum aplikasi dapat diuji. Build, unit test, dan lint di atas adalah hasil aktual; daftar demo berikut adalah langkah yang masih harus dijalankan, bukan klaim sudah lulus. Screenshot dua ukuran juga belum tersedia.

1. Buka aplikasi dan tunjukkan keadaan daftar kosong.
2. Tekan **Tambah pinjaman** tanpa isi; pesan wajib isi muncul.
3. Isi **Payung** dan **Rina**, lalu tekan **Tambah pinjaman**.
4. Tekan **Tandai kembali**; status menjadi **Sudah kembali**.
5. Ubah ukuran jendela untuk menunjukkan susunan bertumpuk dan berdampingan.
6. Tekan **Hapus**; daftar kosong muncul lagi.

## Pemeriksaan kelompok sebelum presentasi

- Coba label dan aksi dengan TalkBack serta keyboard fisik/Tab/Enter.
- Minta teman mencoba menambah dan menandai barang kembali; catat kesulitan nyata jika ada.
- Setiap anggota memahami alur callback → ViewModel → UiState → StateFlow → UI.
- Setiap anggota membuat commit kontribusi nyata melalui akun sendiri.
- Cocokkan jumlah anggota dengan ketentuan dosen (PDF menyebut tiga orang).
- Pastikan tautan repository dapat dibuka dosen dan dikumpulkan sebelum pertemuan ke-13.

Referensi pemeriksaan: tugas Compose-MVVM-UI-State, M11 bagian 11, M12 bagian 12, dan M13 bagian 14.
