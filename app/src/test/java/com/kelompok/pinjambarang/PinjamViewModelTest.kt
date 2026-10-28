package com.kelompok.pinjambarang

import com.kelompok.pinjambarang.viewmodel.PinjamViewModel
import org.junit.Assert.*
import org.junit.Test

class PinjamViewModelTest {
    @Test
    fun awalKosongDanInputKosongDitolak() {
        val vm = PinjamViewModel()
        assertTrue(vm.uiState.value.daftarPinjaman.isEmpty())
        vm.tambahPinjaman()
        assertNotNull(vm.uiState.value.pesanError)
        assertTrue(vm.uiState.value.daftarPinjaman.isEmpty())
        vm.ubahNamaBarang("   ")
        vm.ubahNamaPeminjam("Rina")
        vm.tambahPinjaman()
        assertNotNull(vm.uiState.value.pesanError)
        assertTrue(vm.uiState.value.daftarPinjaman.isEmpty())
    }

    @Test
    fun tambahMembersihkanSpasiDanForm() {
        val vm = PinjamViewModel()
        vm.ubahNamaBarang(" Payung ")
        vm.tambahPinjaman()
        assertNotNull(vm.uiState.value.pesanError)
        vm.ubahNamaPeminjam(" Rina ")
        vm.tambahPinjaman()
        val state = vm.uiState.value
        assertEquals("Payung", state.daftarPinjaman.single().namaBarang)
        assertEquals("Rina", state.daftarPinjaman.single().namaPeminjam)
        assertFalse(state.daftarPinjaman.single().sudahKembali)
        assertEquals("", state.namaBarang)
        assertEquals("", state.namaPeminjam)
        assertNull(state.pesanError)
    }

    @Test
    fun kembaliDanHapusHanyaMengubahDataTerpilih() {
        val vm = PinjamViewModel()
        repeat(2) {
            vm.ubahNamaBarang("Payung")
            vm.ubahNamaPeminjam("Rina")
            vm.tambahPinjaman()
        }
        val sebelum = vm.uiState.value
        val pertama = sebelum.daftarPinjaman[0].id
        val kedua = sebelum.daftarPinjaman[1].id
        assertNotEquals(pertama, kedua)
        vm.tandaiKembali(pertama)
        assertTrue(vm.uiState.value.daftarPinjaman[0].sudahKembali)
        assertFalse(vm.uiState.value.daftarPinjaman[1].sudahKembali)
        assertFalse(sebelum.daftarPinjaman[0].sudahKembali)
        vm.hapusPinjaman(pertama)
        assertEquals(kedua, vm.uiState.value.daftarPinjaman.single().id)
        vm.hapusPinjaman(kedua)
        assertTrue(vm.uiState.value.daftarPinjaman.isEmpty())
    }
}
