package com.yusufwdn.cryptowatcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufwdn.cryptowatcher.adapter.CoinAdapter;
import com.yusufwdn.cryptowatcher.model.Coin;
import com.yusufwdn.cryptowatcher.network.ApiService;
import com.yusufwdn.cryptowatcher.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CoinAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private CoinAdapter coinAdapter;
    private ProgressBar progressBar;
    private List<Coin> fullCoinList = new ArrayList<>();
    private final String API_KEY = BuildConfig.COINGECKO_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hubungkan variabel dengan Toolbar di layout
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set toolbar
        setSupportActionBar(toolbar);

        // Menghubungkan variabel recyclerView dengan komponen view yang ada di layout
        recyclerView = findViewById(R.id.rvCoinList);
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView dan Adapater
        setupRecyclerView();

        // Get data dari API
        fetchCoins();
    }

    // Handle fungsi klik pada list data
    @Override
    public void onItemClick(String coinId) {
        // Membuat intent untuk DetailAcitivy
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        // Menyisipkan ID dari coin ke dalam intent agar bisa diterima oleh DetailActivity
        intent.putExtra("COIN_ID", coinId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Inisialisasi SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Tambahkan listener untuk memantau input teks dari pengguna
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tidak perlu melakukan apa-apa saat tombol submit ditekan
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Panggil method filter setiap kali teks berubah
                filter(newText);
                return true;
            }
        });

        return true;
    }

    // Method ini untuk menangani klik pada item menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_watchlist) {
            // Buka WatchlistActivity
            Intent intent = new Intent(this, WatchlistActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        // Inisialisasi adapter dengan list kosong
        coinAdapter = new CoinAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(coinAdapter);
        // Tidak perlu set LayoutManager di sini karena sudah diset di XML

        // Set listenerClick-nya di sini
        coinAdapter.setOnItemClickListener(this);
    }

    private void fetchCoins() {
        // Tampilkan progress bar sebagai indikasi bahwa data sedang dimuat
        progressBar.setVisibility(View.VISIBLE);

        // Dapatkan service API dari RetrofitClient
        ApiService apiService = RetrofitClient.getApiService();

        // Pemanggilan API
        apiService.getMarkets("usd", 100, 1, API_KEY).enqueue(new Callback<List<Coin>>() {
            // Jika request berhasil
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                // Hide lagi progressbarnya
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Simpan daftar asli ke fullCoinList
                    fullCoinList.clear();
                    fullCoinList.addAll(response.body());
                } else {
                    // Jika response tidak sukses (misal: error 404, 500)
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }

                coinAdapter.filterList(fullCoinList);
            }

            // Jika request gagal
            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        // Buat list baru untuk menampung hasil filter
        List<Coin> filteredList = new ArrayList<>();

        // Loop setiap item di daftar koin asli
        for (Coin item : fullCoinList) {
            // Cek apakah nama atau simbol koin mengandung teks pencarian
            // toLowerCase() digunakan agar pencarian tidak case-sensitive (tidak peduli huruf besar/kecil)
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getSymbol().toLowerCase().contains(text.toLowerCase())) {
                // Kalo cocok, tambahkan ke list hasil filter
                filteredList.add(item);
            }
        }

        // Update adapter dengan list yang sudah difilter
        coinAdapter.filterList(filteredList);
    }
}