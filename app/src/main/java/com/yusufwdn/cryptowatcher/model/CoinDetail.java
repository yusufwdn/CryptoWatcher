package com.yusufwdn.cryptowatcher.model;

import com.google.gson.annotations.SerializedName;

public class CoinDetail {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("image")
    private Image image;

    // JSON 'market_data' adalah sebuah objek, jadi kita buat class untuknya
    @SerializedName("market_data")
    private MarketData marketData;

    // Inner class untuk menampung URL gambar
    public static class Image {
        @SerializedName("large")
        private String large;

        public String getLarge() {
            return large;
        }
    }

    // Inner class untuk handle market_data
    public static class MarketData {
        @SerializedName("current_price")
        private CurrentPrice currentPrice;

        @SerializedName("market_cap")
        private MarketCap marketCap;

        @SerializedName("high_24h")
        private High24h high24h;

        @SerializedName("low_24h")
        private Low24h low24h;

        // Inner class lagi karena harga & market cap punya banyak mata uang
        public static class CurrentPrice {
            @SerializedName("usd")
            private double usd;

            public double getUsd() { return usd; }
        }

        public static class MarketCap {
            @SerializedName("usd")
            private long usd;
            public long getUsd() { return usd; }
        }

        public static class High24h {
            @SerializedName("usd")
            private double usd;
            public double getUsd() { return usd; }
        }

        public static class Low24h {
            @SerializedName("usd")
            private double usd;
            public double getUsd() { return usd; }
        }

        public CurrentPrice getCurrentPrice() { return currentPrice; }

        public MarketCap getMarketCap() { return marketCap; }

        public High24h getHigh24h() { return high24h; }

        public Low24h getLow24h() { return low24h; }

    }

    // Getters untuk class utama
    public String getId() { return id; }

    public String getName() { return name; }

    public String getSymbol() { return symbol.toUpperCase(); }

    public Image getImage() { return image; }

    public MarketData getMarketData() { return marketData; }
}
