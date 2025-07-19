package com.yusufwdn.cryptowatcher.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Ini adalah class "pabrik" yang membuat dan mengkonfigurasi Retrofit.
// Kita hanya perlu satu instance dari class ini untuk seluruh aplikasi.
public class RetrofitClient {
    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            // HttpLoggingInterceptor digunakan untuk menampilkan log dari request dan response API
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Membangun instance Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set base URL dari CoinGecko
                    .client(client) // Set client dengan tambahan logging interceptor
                    .addConverterFactory(GsonConverterFactory.create()) // Set GSON sebagai converter
                    .build();
        }
        // Membuat implementasi dari ApiService interface
        return retrofit.create(ApiService.class);
    }
}