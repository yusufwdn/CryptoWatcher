package com.yusufwdn.cryptowatcher.network;

import com.yusufwdn.cryptowatcher.model.Coin;
import com.yusufwdn.cryptowatcher.model.CoinDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Melakukan GET HTTP ke endpoint "coins/markets"
    // Hasilnya akan mendapatkan list coins dari CoinGecko
    @GET("coins/markets")
    Call<List<Coin>> getMarkets(
            // Meanmbahkan query parameters pada request
            @Query("vs_currency") String currency,
            @Query("per_page") int perPage,
            @Query("page") int page,

            // @Header menambahkan API Key yang kita miliki ke header request
            @Header("x-cg-demo-api-key") String apiKey
    );

    // Melakukan GET HTTP ke endpoint "coins/markets"
    // Hasilnya akan mendapatkan list coins dari CoinGecko berdasarkan filter dari parameter ids
    // untuk mendapatkan data koin secara spesifik
    @GET("coins/markets")
    Call<List<Coin>> getCoinsByIds(
            // Meanmbahkan query parameters pada request
            @Query("vs_currency") String currency,
            @Query("ids") String ids, // filter berdasarkan ID (bisa multiple ID)

            // @Header menambahkan API Key yang kita miliki ke header request
            @Header("x-cg-demo-api-key") String apiKey
    );

    // Melakukan GET HTTP ke endpoint "coins/{id}"
    // Endpoint ini digunakan untuk mengambil detail satu koin berdasarkan ID
    @GET("coins/{id}")
    Call<CoinDetail> getCoinDetail(
            // Menambahkan path parameter coinId
            @Path("id") String coinId,

            // @Header menambahkan API Key yang kita miliki ke header request
            @Header("x-cg-demo-api-key") String apiKey
    );
}
