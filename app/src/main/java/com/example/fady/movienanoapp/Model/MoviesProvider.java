package com.example.fady.movienanoapp.Model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Fady on 2017-04-12.
 */

public class MoviesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.movies.provider.movies";
    static final String URL = "content://" + PROVIDER_NAME + "/details";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int DETAILS = 1;
    static final int DETAILS_ID = 2;

    private static HashMap<String, String> MovieMap;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "details", DETAILS);
        uriMatcher.addURI(PROVIDER_NAME, "details/#", DETAILS_ID);
    }

    public static final String database_name = "MyOwndatabase.db";
    public static final String table_name = "MyOwntable";
    public static final String ID = "Id";
    public static final String POSTER = "poster";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String RATE = "rate";
    public static final String DATE = "date";
    public static final String OVERVIEW = "overview";
    public static final int version = 2;
    String Sql_query = " CREATE TABLE " + table_name + "( "
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + POSTER + " TEXT ,"
            + ORIGINAL_TITLE + " TEXT ,"
            + RATE + " TEXT ,"
            + DATE + " TEXT ,"
            + OVERVIEW + " TEXT "
            + ")";
    private SQLiteDatabase db;
    MySqlite mydata;

    public class MySqlite extends SQLiteOpenHelper {

        public MySqlite(Context context) {
            super(context, database_name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Sql_query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = "DROP TABLE IF EXISTS " + table_name;
            db.execSQL(query);
            onCreate(db);
        }

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mydata = new MySqlite(context);
        // permissions to be writable
        db = mydata.getWritableDatabase();
        if (db == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(table_name);
        switch (uriMatcher.match(uri)) {
            case DETAILS:
                qb.setProjectionMap(MovieMap);
                break;
            case DETAILS_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {

            sortOrder = ORIGINAL_TITLE;
        }
        Cursor cursor = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(table_name, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

