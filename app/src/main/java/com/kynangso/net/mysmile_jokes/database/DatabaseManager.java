package com.kynangso.net.mysmile_jokes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kynangso.net.mysmile_jokes.models.Category;
import com.kynangso.net.mysmile_jokes.models.Story;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    Context context;
    String[] tableNameList;

    private static final String DATABASE_NAME = "stories_database.sqlite";
    public final static String DATABASE_PATH = "/data/data/com.kynangso.net.mysmile_jokes/databases/";
    public static final int DATABASE_VERSION = 1 + 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void createDatabase() {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.d("AAA", "db exists");
        }
        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    private void copyDataBase() throws IOException {

        InputStream mInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public ArrayList<Category> readCategories() {
        tableNameList = new String[]{
                "tblTrangQuynh",
                "tblVoVa",
                "tblBomNhau",
                "tblHocDuong",
                "tblNhaBinh",
                "tblTieuLam",
                "tblDanGian",
                "tblConGai",
                "tblAnimals",
                "tblVNVoDoi",
                "tblLove",
                "tblMuonMau",
                "tblYHoc",
                "tblAdult"
        };
        SQLiteDatabase sql = getReadableDatabase();
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = sql.rawQuery("SELECT * FROM tblTotal", null);
        int count = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = new Category(id, name, tableNameList[count]);
            categories.add(category);
            count++;

        }
        sql.close();
        return categories;
    }

    public ArrayList<Story> getDataFromTable(String tableName) {
        ArrayList<Story> stories = new ArrayList<>();
        stories.clear();
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String enTitle = "No title";
            String viTitle = cursor.getString(1);
            String enContent = "No content";
            String viContent = cursor.getString(2);
            Story story = new Story(id, enTitle, viTitle, enContent, viContent);
            stories.add(story);
        }
        sql.close();
        Log.d("getdata", "get: " + stories.size());
        return stories;
    }

    public boolean isFavorite(Story story) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM tblFavorites WHERE title = '" + story.getmViTitle() + "'", null);
        if (cursor.moveToFirst()) {
            Log.d("Favorite", "return: true");
            return true;
        } else {
            Log.d("Favorite", "return: false");
            return false;
        }
    }

    public void addToFavorite(Story story) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", story.getmViTitle());
        contentValues.put("content", story.getmViContent());
        sql.insert("tblFavorites", null, contentValues);
        sql.close();
    }

    public void deleteToFavorite(Story story) {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM tblFavorites WHERE title = '" + story.getmViTitle() + "'");
        Log.d("log", "name: " + story.getmViTitle());
        sql.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}