package com.yusufwdn.cryptowatcher.model;

import com.google.gson.annotations.SerializedName;

// Blueprint atau model atas data yang didapatkan dari API
public class Coin {
    // @SerializeName digunakan untuk pemetaan nama field
    // Contohnya nanti "current_price" di JSON akan dimasukkan ke variabel "currentPrice" di model ini
    @SerializedName("id")
    private String id;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("current_price")
    private double currentPrice;

    @SerializedName("price_change_percentage_24h")
    private double priceChangePercentage24h;

    // Getter
    public String getId() {
        return id;
    }

    // Set symbol menjadi uppercase
    public String getSymbol() {
        return symbol.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }
}
