
# Cara Setup Proyek

Berikut adalah langkah-langkah yang diperlukan untuk men-setup dan menjalankan proyek ini di Android Studio.

---

## Prasyarat

Pastikan perangkatmu sudah ter-install:

- **Android Studio** (Disarankan versi Hedgehog atau yang lebih baru)  
- **Git** (untuk meng-clone repositori)  
- **Android Emulator** atau perangkat fisik dengan **API level 24 (Android 7.0 Nougat)** atau lebih tinggi

---

## Langkah-langkah Instalasi

### 1. Clone Repositori

Buka terminal atau command prompt, lalu jalankan perintah berikut untuk mengunduh proyek dari GitHub:

```bash
git clone https://github.com/yusufwdn/CryptoWatcher.git
```

---

### 2. Buka Proyek di Android Studio

- Buka Android Studio  
- Pilih **Open** atau **Open an Existing Project**  
- Arahkan ke folder proyek yang baru saja kamu clone, lalu klik **OK**  
- Tunggu hingga Android Studio selesai melakukan proses *Gradle Sync*

---

### 3. Dapatkan API Key dari CoinGecko

Aplikasi ini membutuhkan API key gratis dari CoinGecko untuk bisa mengambil data.

- Buka situs [CoinGecko API](https://www.coingecko.com/en/api)  
- Klik **"Get your free API key"** dan daftar akun jika belum punya  
- Setelah masuk ke *Dashboard*, buat API key baru

---

### 4. Konfigurasi API Key (Langkah Paling Penting)

Karena alasan keamanan, API key tidak disimpan di dalam repositori. Kamu harus membuatnya secara manual.

- Di dalam direktori utama proyek di Android Studio, cari file bernama `local.properties`  
- Jika file tersebut tidak ada, klik kanan pada direktori root proyek → **New** → **File**  
- Beri nama `local.properties` dan tambahkan baris berikut (ganti `KEY_KAMU_DARI_COINGECKO` dengan API key kamu):

```properties
# File ini untuk menyimpan data sensitif lokal
apiKey=KEY_KAMU_DARI_COINGECKO
```

> 🔒 Langkah ini sangat penting karena file `build.gradle.kts` dikonfigurasi untuk membaca key dari file ini.  
> Tanpa langkah ini, aplikasi tidak akan bisa berjalan.

---

### 5. Build dan Jalankan Aplikasi

- Setelah API key dikonfigurasi, klik tombol **Run 'app'** (ikon panah hijau) di Android Studio  
- Pilih emulator atau perangkat fisik yang tersedia  
- Aplikasi akan di-*build* dan di-*install* secara otomatis

---

✅ Sekarang aplikasi siap untuk dijalankan dan diuji.
