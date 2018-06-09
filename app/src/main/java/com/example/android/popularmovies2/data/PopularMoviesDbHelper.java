package com.example.android.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "popularMoviesDb.db";

    // If you change the database schema, increment the database version
    private static final int VERSION = 2;

    PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + PopularMoviesContract.PopularMoviesEntry.TABLE_NAME + " (" +
                PopularMoviesContract.PopularMoviesEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.PopularMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
