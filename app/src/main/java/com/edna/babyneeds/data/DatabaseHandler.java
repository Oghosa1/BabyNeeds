package com.edna.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.edna.babyneeds.model.Items;
import com.edna.babyneeds.util.Constants;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STORAGE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " INTEGER,"
                + Constants.KEY_COLOR + " TEXT,"
                + Constants.KEY_QUANTITY + " INTEGER,"
                + Constants.KEY_SIZE + " INTEGER,"
                + Constants.KEY_DATE_ADDED + " LONG);";

        db.execSQL(CREATE_STORAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    // TODO: CRUD Operation

    // TODO: Add an Item
    public void addItem(Items items) {
        SQLiteDatabase lDatabase = this.getWritableDatabase();

        ContentValues lContentValues = new ContentValues();

        lContentValues.put(Constants.KEY_GROCERY_ITEM, items.getItemName());
        lContentValues.put(Constants.KEY_COLOR, items.getItemColor());
        lContentValues.put(Constants.KEY_QUANTITY, items.getItemQuant());
        lContentValues.put(Constants.KEY_SIZE, items.getItemSize());
        lContentValues.put(Constants.KEY_DATE_ADDED, System.currentTimeMillis());

        // Insert into row
        lDatabase.insert(Constants.TABLE_NAME, null, lContentValues);

        Log.d("Data", "addItem: Item Added" + items.getItemName());
    }

    // TODO: Get an Item
    public Items getItem(int id) {
        SQLiteDatabase lDatabase = this.getReadableDatabase();
        Cursor lCursor = lDatabase.query(Constants.TABLE_NAME, new String[]{

                        Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_SIZE,
                        Constants.KEY_QUANTITY,
                        Constants.KEY_COLOR,
                        Constants.KEY_DATE_ADDED},
                Constants.KEY_ID + "=?",

                new String[]{
                        String.valueOf(id)}, null, null, null, null);
        if (lCursor != null)
            lCursor.moveToFirst();

        Items lItems = new Items();
        if (lCursor != null) {
            lItems.setId(Integer.parseInt(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_ID))));
            lItems.setItemName(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            lItems.setItemColor(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_COLOR)));
            lItems.setItemQuant(Integer.parseInt(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_QUANTITY))));

            //Converting Timestamp to something readable
            DateFormat lDateFormat = DateFormat.getDateInstance();
            String formatted = lDateFormat.format(new Date(lCursor.getLong(lCursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
            lItems.setDateItemAdded(formatted);
        }

        return lItems;
    }

    // TODO: Get all Items
    public List<Items> getAllItems() {
        SQLiteDatabase lDatabase = this.getReadableDatabase();

        List<Items> lItemsList = new ArrayList<>();
        Cursor lCursor = lDatabase.query(Constants.TABLE_NAME, new String[]{
                        Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_COLOR,
                        Constants.KEY_SIZE,
                        Constants.KEY_QUANTITY,
                        Constants.KEY_DATE_ADDED},
                null, null, null, null, Constants.KEY_DATE_ADDED + " DESC ");
        if (lCursor.moveToFirst())
            do {

                Items lItems = new Items();
                lItems.setId(Integer.parseInt(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_ID))));
                lItems.setItemName(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                lItems.setItemColor(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_COLOR)));
                lItems.setItemQuant(Integer.parseInt(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_QUANTITY))));
                lItems.setItemSize(Integer.parseInt(lCursor.getString(lCursor.getColumnIndex(Constants.KEY_SIZE))));
                //Converting Timestamp to something readable
                DateFormat lDateFormat = DateFormat.getDateInstance();
                String formatted = lDateFormat.format(new Date(lCursor.getLong(lCursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
                lItems.setDateItemAdded(formatted);

                // TODO: Add to array list
                lItemsList.add(lItems);

            } while (lCursor.moveToNext());

//        lCursor.close();
        return lItemsList;

    }

    // TODO: Add Update Item
    public int updateItem(Items items) {
        SQLiteDatabase lDatabase = this.getWritableDatabase();

        ContentValues lContentValues = new ContentValues();

        lContentValues.put(Constants.KEY_GROCERY_ITEM, items.getItemName());
        lContentValues.put(Constants.KEY_COLOR, items.getItemColor());
        lContentValues.put(Constants.KEY_QUANTITY, items.getItemQuant());
        lContentValues.put(Constants.KEY_SIZE, items.getItemSize());
        lContentValues.put(Constants.KEY_DATE_ADDED, System.currentTimeMillis());

        // Update row
        return lDatabase.update(Constants.TABLE_NAME, lContentValues, Constants.KEY_ID + "=?", new String[]{String.valueOf(items.getId())});

    }

    // TODO: Add Delete Item
    public void deleteItem(int id) {
        SQLiteDatabase lDatabase = this.getWritableDatabase();
        lDatabase.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});

        lDatabase.close(); // close database in order to avoid memory leak

    }

    // TODO: Get Item Count
    public int getItemCount() {
        String CountQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase lDatabase = getReadableDatabase();
        Cursor lCursor = lDatabase.rawQuery(CountQuery, null);

        return lCursor.getCount();
    }
}
