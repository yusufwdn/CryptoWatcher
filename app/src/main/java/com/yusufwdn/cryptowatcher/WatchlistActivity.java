package com.yusufwdn.cryptowatcher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufwdn.cryptowatcher.adapter.CoinAdapter;
import com.yusufwdn.cryptowatcher.db.WatchlistContract;
import com.yusufwdn.cryptowatcher.db.WatchlistDbHelper;
import com.yusufwdn.cryptowatcher.model.Coin;
import com.yusufwdn.cryptowatcher.network.ApiService;
import com.yusufwdn.cryptowatcher.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CoinAdapter coinAdapter;
    private WatchlistDbHelper dbHelper;
    private TextView tvEmptyWatchlist;
    private ProgressBar progressBar;
    private final String API_KEY = BuildConfig.COINGECKO_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarWatchlist);
        setSupportActionBar(toolbar);

        // Menampilkan tombol kembali di Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rvWatchlist);
        tvEmptyWatchlist = findViewById(R.id.tvEmptyWatchlist);
        progressBar = findViewById(R.id.progressBarWatchlist);
        dbHelper = new WatchlistDbHelper(this);

        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat data setiap kali activity ini ditampilkan,
        // agar daftar selalu update jika ada perubahan.
        loadWatchlistData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Triggers the back button action using the new dispatcher
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        coinAdapter = new CoinAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(coinAdapter);

        // Ketika list diklik, tampilkan halaman detail (sama seperti di main activity)
        coinAdapter.setOnItemClickListener(coinId -> {
            Intent intent = new Intent(WatchlistActivity.this, DetailActivity.class);
            intent.putExtra("COIN_ID", coinId);
            startActivity(intent);
        });
    }

    private void loadWatchlistData() {
        // 1. Ambil semua ID koin dari database SQLite
        List<String> coinIds = getAllWatchlistedCoinIds();

        if (coinIds.isEmpty()) {
            // Jika kosong, tampilkan pesan dan sembunyikan list
            tvEmptyWatchlist.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        // Jika ada, sembunyikan pesan dan tampilkan list
        tvEmptyWatchlist.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        // 2. Ubah List<String> menjadi satu String dipisahkan koma
        String ids = String.join(",", coinIds);

        // 3. Panggil API untuk mengambil data terbaru dari koin-koin tersebut
        fetchDataFromApi(ids);
    }

    private List<String> getAllWatchlistedCoinIds() {
        List<String> coinIds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
            WatchlistContract.WatchlistEntry.TABLE_NAME,
            new String[]{WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID},
            null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            coinIds.add(cursor.getString(cursor.getColumnIndexOrThrow(WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID)));
        }
        cursor.close();
        db.close();
        return coinIds;
    }

    private void fetchDataFromApi(String ids) {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCoinsByIds("usd", ids, API_KEY).enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    coinAdapter.filterList(response.body());
                } else {
                    Toast.makeText(WatchlistActivity.this, "Gagal memuat data watchlist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WatchlistActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}