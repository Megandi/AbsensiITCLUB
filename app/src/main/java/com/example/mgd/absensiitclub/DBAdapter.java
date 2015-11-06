package com.example.mgd.absensiitclub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import domain.Absensi;


/**
 * Created by mgd on 05/11/15.
 */
public class DBAdapter extends SQLiteOpenHelper {
    private static final String DB_NAME = "uts";
    private static final String TABLE_NAME = "m_absensi";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama";
    private static final String COL_KET = "keterangan";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    private SQLiteDatabase sqLiteDatabase = null;

    public DBAdapter(Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) { createTable(db); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + COL_NAME + " TEXT,"
                + COL_KET + " TEXT);");
    }

    public void save(Absensi absensi) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, absensi.getNama());
        contentValues.put(COL_KET, absensi.getKeterangan());

        sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqLiteDatabase.close();
    }

    public void updateSiswa(Absensi absensi) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, absensi.getNama());
        cv.put(COL_KET, absensi.getKeterangan());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[]{absensi.getId()};
        sqLiteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqLiteDatabase.close();

    }

    public void openDB(){
        if (sqLiteDatabase == null){
            sqLiteDatabase = getWritableDatabase();
        }
    }

    public void closeDB(){
        if(sqLiteDatabase != null){
            if (sqLiteDatabase.isOpen()){
                sqLiteDatabase.close();
            }
        }
    }

    public void deleteAll() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    public void delete(Absensi absensi) {
        sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[]{String.valueOf(absensi.getId())};
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public List<Absensi> getAllData() {
        sqLiteDatabase = getWritableDatabase();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new String[]{
                COL_ID, COL_NAME, COL_KET}, null, null, null, null, null);
        List<Absensi> absensis = new ArrayList<Absensi>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Absensi absensi = new Absensi();
                absensi.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                absensi.setNama(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                absensi.setKeterangan(cursor.getString(cursor.getColumnIndex(COL_KET)));
                absensis.add(absensi);
            }
            sqLiteDatabase.close();
            return absensis;
        } else {
            sqLiteDatabase.close();
            return new ArrayList<Absensi>();

        }

    }


}
