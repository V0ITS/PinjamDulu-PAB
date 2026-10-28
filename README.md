# Pinjam Barang — PinjamDulu PAB

Aplikasi Android sederhana untuk mencatat barang yang dipinjam teman. Dibuat dengan **Kotlin, Jetpack Compose, MVVM, StateFlow, dan Unidirectional Data Flow (UDF)**.

## Anggota kelompok

| Nama | NIM |
| --- | --- |
| Aditya Rizki Pratama | 24523098 |
| Muhammad Thaahir Ramadhan | 24523081 |

Ketentuan tugas menyebut tiga mahasiswa per kelompok. Daftar ini mengikuti dua identitas yang telah diberikan; anggota ketiga perlu ditambahkan jika diwajibkan dosen.

## Masalah, pengguna, dan solusi

- **Masalah:** mahasiswa sering lupa siapa yang meminjam barangnya dan apakah barang sudah dikembalikan.
- **Pengguna sasaran:** mahasiswa atau penghuni kos yang meminjamkan barang kepada teman.
- **Solusi:** satu layar berisi formulir nama barang/nama peminjam dan daftar status peminjaman.
- **Berbeda dari contoh tugas:** aplikasi ini menangani peminjaman barang, bukan tugas kuliah, daftar belanja, atau katalog buku.

## Tiga fitur inti

1. **Tambah pinjaman** dengan validasi kedua input wajib diisi (spasi saja ditolak).
2. **Tandai kembali** untuk memperbarui status barang.
3. **Hapus pinjaman** dari daftar.

Data hanya disimpan dalam memori ViewModel. Data bertahan saat rotasi layar, tetapi hilang saat proses aplikasi dihentikan. Tidak ada login, API, database, atau notifikasi.

## Cara menjalankan

1. Clone repository: `git clone https://github.com/V0ITS/PinjamDulu-PAB.git`.
2. Buka folder proyek di Android Studio yang mendukung **Android Gradle Plugin 9.4.1**.
3. Gunakan **JDK 17 atau lebih baru yang didukung Gradle 9.6.0** (proyek diverifikasi dengan JDK 21).
4. Pasang Android SDK Platform 37 melalui SDK Manager (dibutuhkan Adaptive 1.3.0 dari M11). Biarkan Android Studio membuat `local.properties` sesuai lokasi SDK komputer Anda.
5. Tunggu Gradle Sync selesai; koneksi internet dibutuhkan untuk dependensi awal.
6. Jalankan konfigurasi `app` pada emulator/perangkat Android 7.0 (API 24) atau lebih baru.

Pemeriksaan dari terminal Windows:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

macOS/Linux: `chmod +x gradlew`, lalu `./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug`.

APK hasil build: `app/build/outputs/apk/debug/app-debug.apk` (tidak disimpan dalam Git).

## Struktur dan MVVM

```text
app/src/main/java/com/kelompok/pinjambarang/
├── MainActivity.kt                 # Titik masuk dan tema aplikasi
├── model/Pinjaman.kt               # Model satu peminjaman
├── viewmodel/PinjamViewModel.kt     # Validasi dan perubahan state
└── ui/
    ├── PinjamRoute.kt              # ViewModel, lifecycle, dan kebijakan dua panel
    ├── PinjamUiState.kt            # Seluruh state layar
    └── PinjamScreen.kt             # Composable, form, daftar, layout adaptif
```

`MutableStateFlow` bersifat privat di ViewModel. UI menerima `StateFlow` read-only melalui `asStateFlow()` dan mengamatinya menggunakan `collectAsStateWithLifecycle()`. UI mengirim callback, bukan mengubah daftar sendiri. Penyimpanan langsung di ViewModel mengikuti contoh sederhana **M13 bagian 5 dan 10.6**. M12 memperkenalkan repository, tetapi M13 secara eksplisit memakai data di memori ViewModel; proyek mengikuti bentuk sederhana M13 dengan tetap menjaga pembagian tanggung jawab MVVM dari M12.

## Contoh alur UDF

```text
Pengguna mengetik Payung dan Rina
→ callback perubahan input memanggil fungsi ViewModel
→ ViewModel memperbarui field dalam PinjamUiState
→ pengguna menekan Tambah pinjaman
→ onTambah memanggil tambahPinjaman()
→ ViewModel memvalidasi input dan membuat Pinjaman
→ _uiState.update { it.copy(...) } menghasilkan state baru
→ StateFlow mengirim state terbaru
→ collectAsStateWithLifecycle() memperbarui state Compose
→ daftar menampilkan Payung / Rina / Masih dipinjam
```

Jika salah satu input kosong, ViewModel mengisi `pesanError` dan tidak menambah data. Ketika data terakhir dihapus, pesan daftar kosong muncul kembali.

## UI/UX dan dua ukuran layout

- Mengikuti kebijakan **M11 bagian 8.5 dan 9.2**: lebar jendela minimal **840 dp** dan tinggi minimal **480 dp** menampilkan form di kiri serta daftar di kanan menggunakan `Row`.
- Ukuran lain menampilkan form dan daftar bertumpuk dalam satu `LazyColumn`, termasuk jendela lebar yang pendek.
- `currentWindowAdaptiveInfoV2().windowSizeClass` dibaca di route dan diteruskan sebagai Boolean `duaPanel`, sehingga layar mudah di-preview.
- Form pada layar lebar dapat di-scroll; daftar menggunakan `LazyColumn`. Insets sistem dan keyboard ditangani melalui `Scaffold` dan `imePadding()`.
- Label Bahasa Indonesia, tombol Material 3, status berbentuk teks, dan pesan validasi yang jelas.

User story: **Sebagai mahasiswa yang meminjamkan barang, saya ingin mencatat peminjam dan status kembali agar tidak lupa siapa yang masih membawa barang saya.**

| Requirement UI | Cara memeriksa |
| --- | --- |
| Pengguna dapat mengenali barang, peminjam, dan status melalui label yang jelas | Tambah Payung/Rina, lalu periksa judul kartu, peminjam, serta status |
| Data dan draf bertahan ketika ukuran jendela berubah | Isi data dan sebagian draf, ubah jendela sempit ke lebar, lalu cocokkan isinya |

Pemetaan materi:
- **M11:** hierarki tipografi, komponen berulang, label dan umpan balik, konten scroll, window size class, dua panel, preview font besar.
- **M12:** model/UI/ViewModel terpisah, `viewModel()` lifecycle-aware, state layar milik ViewModel, UI menerima data dan callback.
- **M13:** data class dengan `val`, `StateFlow` read-only, `update` + `copy`, list baru dengan `+`/`map`/`filter`, ID stabil, route, dan UDF. Jumlah daftar dihitung dari `.size`, tidak disimpan sebagai state kedua.

Sketsa: [dua ukuran layar](docs/sketsa-layout.svg).

### Screenshot aplikasi

Screenshot runtime **belum tersedia** karena emulator pada lingkungan pengerjaan belum berhasil menyelesaikan boot secara stabil. Sketsa dua ukuran tersedia di atas; sketsa tidak menggantikan screenshot. Sebelum pengumpulan, jalankan aplikasi dan tambahkan screenshot sempit serta lebar ke `docs/screenshots/`.

## Dokumen tugas

- [Slide presentasi (5 halaman)](docs/Slide-Presentasi.pdf)
- [Pemeriksaan dan skenario demo](docs/PEMERIKSAAN.md)
- [Sketsa layar sempit dan lebar](docs/sketsa-layout.svg)
- File penjelasan kode untuk presentasi disimpan **hanya di folder lokal** sesuai permintaan kelompok.

## Rencana kontribusi anggota

Pembagian berikut adalah **rencana tanggung jawab dan pembelajaran**, bukan klaim bahwa commit tiap anggota sudah dilakukan.

| Anggota | Tanggung jawab |
| --- | --- |
| Aditya Rizki Pratama | Model, UiState, ViewModel; memahami validasi, tambah, kembali, hapus, dan unit test |
| Muhammad Thaahir Ramadhan | Composable dan layout adaptif; memahami callback, screenshot, README, dan slide |

Keduanya wajib memahami alur UDF secara utuh. Setiap anggota perlu membuat perubahan nyata, memeriksa perubahan tersebut, lalu commit dan push menggunakan akun GitHub masing-masing. Jangan mengganti author untuk meniru kontribusi anggota lain.

## Rencana presentasi maksimal 7 menit

| Waktu | Materi | Pembicara |
| --- | --- | --- |
| 0:00–2:00 | Masalah, pengguna sasaran, solusi dan fitur | Aditya |
| 2:00–3:30 | Demo tambah, validasi, kembali/hapus, dan dua layout | Thaahir |
| 3:30–5:00 | Jelaskan model, ViewModel, UiState, callback, dan StateFlow | Aditya + Thaahir |
| 5:00–7:00 | Tanya jawab | Keduanya |

## Checklist pengumpulan

- Source, konfigurasi Gradle, dan Gradle Wrapper tersedia.
- `.gitignore` mengecualikan build, `local.properties`, kredensial, dan panduan lokal.
- Nama/NIM, masalah, pengguna, solusi, fitur, langkah menjalankan, rencana kontribusi, dan contoh UDF tersedia.
- Sketsa dua layout dan slide lima halaman tersedia di `docs/`; screenshot runtime masih perlu dilengkapi.
- Status build dan uji dicatat pada `docs/PEMERIKSAAN.md`.
- **Tindakan kelompok:** pastikan ketentuan jumlah anggota, kontribusi commit masing-masing akun, dan pengumpulan tautan sebelum pertemuan ke-13 terpenuhi.
- Pastikan dosen dapat membuka https://github.com/V0ITS/PinjamDulu-PAB; jika private, berikan akses.

## Referensi

- [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation)
- [State dan state hoisting](https://developer.android.com/develop/ui/compose/state)
- [Rekomendasi arsitektur Android](https://developer.android.com/topic/architecture/recommendations)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Layout adaptif](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- Instruksi tugas: `Compose-MVVM-UI-State.pdf`.
- Materi dosen: M11 *UI/UX and Adaptive Layout*, M12 *MVVM dan ViewModel*, dan M13 *UI State, StateFlow, and Unidirectional Data Flow* (PDF pembelajaran disimpan lokal).
