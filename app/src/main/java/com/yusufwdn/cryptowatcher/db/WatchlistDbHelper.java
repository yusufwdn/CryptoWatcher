package com.yusufwdn.cryptowatcher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchlistDbHelper extends SQLiteOpenHelper {
    // versi database default-nya = 1. Jika mengubah skema, perlu menaikan versinya
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Watchlist.db";

    // Buat tabel
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WatchlistContract.WatchlistEntry.TABLE_NAME + "(" +
                    WatchlistContract.WatchlistEntry._ID + " INTEGER PRIMARY KEY," +
                    WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_ID + " TEXT UNIQUE," +
                    WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_NAME + " TEXT," +
                    WatchlistContract.WatchlistEntry.COLUMN_NAME_COIN_SYMBOL + " TEXT," +
                    WatchlistContract.WatchlistEntry.COLUMN_NAME_IMAGE_URL + " TEXT)";

    // Perintah SQL untuk menghapus tabel.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WatchlistContract.WatchlistEntry.TABLE_NAME;

    public WatchlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method ini dipanggil saat database dibuat untuk pertama kalinya.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Method ini dipanggil saat database di-upgrade (versi berubah).
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
