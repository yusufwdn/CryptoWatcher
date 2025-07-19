package com.yusufwdn.cryptowatcher;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.yusufwdn.cryptowatcher.db.WatchlistContract;
import com.yusufwdn.cryptowatcher.db.WatchlistDbHelper;
import com.yusufwdn.cryptowatcher.model.CoinDetail;
import com.yusufwdn.cryptowatcher.network.ApiService;
import com.yusufwdn.cryptowatcher.network.RetrofitClient;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivDetailCoinIcon;
    private TextView tvDetailCoinName, tvDetailCoinPrice, tvMarketCap, tv24hHigh, tv24hLow;
    private Button btnAddToWatchlist;
    private String coinId;
    private CoinDetail currentCoin;
    private boolean isInWatchlist = false;
    private WatchlistDbHelper dbHelper;
    private final String API_KEY = BuildConfig.COINGECKO_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);

        // Menampilkan tombol kembali di Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Inisialisasi dbHelper
        dbHelper = new WatchlistDbHelper(this);

        // Hubungkan variabel dengan view di layout activity_detail
        ivDetailCoinIcon = findViewById(R.id.ivDetailCoinIcon);
        tvDetailCoinName = findViewById(R.id.tvDetailCoinName);
        tvDetailCoinPrice = findViewById(R.id.tvDetailCoinPrice);
        tvMarketCap = findViewById(R.id.tvMarketCap);
        tv24hHigh = findViewById(R.id.tv24hHigh);
        tv24hLow = findViewById(R.id.tv24hLow);
        btnAddToWatchlist = findViewById(R.id.btnAddToWatchlist);

        // Ambil ID koin dari Intent
        coinId = getIntent().getStringExtra("COIN_ID");

        // Jika ID koin tidak null, ambil datanya
        if (coinId != null) {
            fetchCoinDetails(coinId);
        }

        // Tambahkan listener untuk tombol
        btnAddToWatchlist.setOnClickListener(v -> {
            if (isInWatchlist) {
                removeFromWatchlist();
            } else {
                addToWatchlist();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Triggers the back button action using the new dispatcher
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void fetchCoinDetails(String id) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCoinDetail(id, API_KEY).enqueue(new Callback<CoinDetail>() {
            @Override
            public void onResponse(Call<CoinDetail> call, Response<CoinDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Simpan data koin
                    currentCoin = response.body();
                    displayCoinDetails(currentCoin);

                    // Cek status watchlist
                    checkWatchlistStatus();
                } else {
                    Toast.makeText(DetailActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CoinDetail> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkWatchlistStatus() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID + " = ? ";
        String[] selectionArgs = { coinId };

        Cursor cursor = db.query(
            WatchlistContract.WatchlistEntry.TABLE_NAME,   // Nama tabel
            null,                                          // Kolom yang ingin diambil (null = semua)
            selection,                                     // Kolom untuk klausa WHERE
            selectionArgs,                                 // Nilai untuk klausa WHERE
            null, null, null
        );

        isInWatchlist = cursor.getCount() > 0;
        cursor.close();
        db.close();

        // update tampilan tombol berdasarkan statusnya
        updateWatchlistButton();
    }

    private void updateWatchlistButton() {
        if (isInWatchlist) {
            btnAddToWatchlist.setText("Remove from Watchlist");
            btnAddToWatchlist.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            btnAddToWatchlist.setText("Add to Watchlist");
            btnAddToWatchlist.setBackgroundColor(getResources().getColor(R.color.purple));
        }
    }

    private void addToWatchlist() {
        if (currentCoin == null) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID, currentCoin.getId());
        values.put(WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_NAME, currentCoin.getName());
        values.put(WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_SYMBOL, currentCoin.getSymbol());
        values.put(WatchlistContract.WatchlistEntry.COLUMN_NAME_IMAGE_URL, currentCoin.getImage().getLarge());

        long newRowId = db.insert(WatchlistContract.WatchlistEntry.TABLE_NAME, null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(this, "Ditambahkan ke Watchlist!", Toast.LENGTH_SHORT).show();
            isInWatchlist = true;
            updateWatchlistButton();
        } else {
            Toast.makeText(this, "Gagal menambahkan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromWatchlist() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID + " = ?";
        String[] selectionArgs = { coinId };

        int deletedRows = db.delete(WatchlistContract.WatchlistEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(this, "Dihapus dari Watchlist.", Toast.LENGTH_SHORT).show();
            isInWatchlist = false;
            updateWatchlistButton();
        } else {
            Toast.makeText(this, "Gagal menghapus.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayCoinDetails(CoinDetail detail) {
        // Format angka agar mudah dibaca
        DecimalFormat priceFormat = new DecimalFormat("$#,##0.00");
        DecimalFormat marketFormat = new DecimalFormat("$#,##0");

        // Isi semua view dengan data dari object CoinDetail
        tvDetailCoinName.setText(String.format("%s (%s)", detail.getName(), detail.getSymbol()));

        if (detail.getMarketData() != null) {
            tvDetailCoinPrice.setText(priceFormat.format(detail.getMarketData().getCurrentPrice().getUsd()));
            tvMarketCap.setText("Market Cap: " + marketFormat.format(detail.getMarketData().getMarketCap().getUsd()));
            tv24hHigh.setText("24h High: " + priceFormat.format(detail.getMarketData().getHigh24h().getUsd()));
            tv24hLow.setText("24h Low: " + priceFormat.format(detail.getMarketData().getLow24h().getUsd()));
        }

        Glide.with(this).load(detail.getImage().getLarge()).into(ivDetailCoinIcon);
    }
}