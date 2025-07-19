package com.yusufwdn.cryptowatcher.db;

import android.provider.BaseColumns;

// Kelas ini mendefinisikan database (tabel dan kolom)
// Menggunakan final agar kelas ini tidak bisa diturunkan
public final class WatchlistContract {
    // Menghindari instansiasi kelas
    private WatchlistContract() {}

    // Inner class untuk table watchlist
    // Mengimplementasikan BaseColumn agar otomatis tabel memiliki kolom _ID
    public static class WatchlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "watchlist";

        public static final String COLUMN_NAME_COIN_ID = "coin_id";

        public static final String COLUMN_NAME_COIN_NAME = "coin_name";

        public static final String COLUMN_NAME_COIN_SYMBOL = "coin_symbol";

        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
    }
}
