package com.example.winmart.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.winmart.Object.Products;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProducts extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Products_Database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Products";
    private static final String COLUMN_PRODUCT_ID = "ProductID";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_PRODUCT_QUANTITY = "ProductQuantity";
    private static final String COLUMN_PRODUCT_UNIT = "ProductUnit";
    private static final String COLUMN_PRODUCT_BARCODE = "ProductBarcode";
    private static final String COLUMN_PRODUCT_PRICE = "ProductPrice";
    private static final String COLUMN_PRODUCT_STATUS = "ProductStatus";
    private static final String COLUMN_PRODUCT_MANUFACTURING = "ProductManufacturing";
    private static final String COLUMN_PRODUCT_EXPIRATION = "ProductExpiration";

    public DatabaseProducts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_QUANTITY + " TEXT, " +
                COLUMN_PRODUCT_UNIT + " TEXT, " +
                COLUMN_PRODUCT_BARCODE + " TEXT, " +
                COLUMN_PRODUCT_PRICE + " TEXT, " +
                COLUMN_PRODUCT_STATUS + " TEXT, " +
                COLUMN_PRODUCT_MANUFACTURING + " DATE, " +
                COLUMN_PRODUCT_EXPIRATION + " DATE)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public boolean insert( String name, String quantity, String unit, String barcode, String price, String status, String manufact, String expiration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_UNIT, unit);
        values.put(COLUMN_PRODUCT_BARCODE, barcode);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_STATUS, status);
        values.put(COLUMN_PRODUCT_MANUFACTURING, manufact.toString());
        values.put(COLUMN_PRODUCT_EXPIRATION, expiration.toString());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1; // Trả về true nếu insert thành công, ngược lại trả về false
    }

    // Trong lớp DatabaseProducts
    public ArrayList<Products> getAllProducts() {
        ArrayList<Products> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    String productID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                    String productname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                    String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
                    String productUnit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_UNIT));
                    String productBarcode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BARCODE));
                    String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                    String productStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_STATUS));
                    String productManufact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_MANUFACTURING));
                    String productExpiration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_EXPIRATION));

                    // Chuyển đổi từ String sang Date
                    Date PManu = Date.valueOf(productManufact);
                    Date PExpi = Date.valueOf(productExpiration);

                    // Tạo đối tượng Products từ dữ liệu trong cơ sở dữ liệu và thêm vào danh sách
                    Products product = new Products(productID, productname, productQuantity, productUnit
                            , productBarcode, productPrice, productStatus, PManu, PExpi);
                    productList.add(product);
                } catch (Exception e) {
                    // Xử lý lỗi hoặc ghi log
                    Log.e("TAG DatabaseError", "Error converting data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public boolean deleteProduct(Products products) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PRODUCT_BARCODE + " = ?";
        String[] selectionArgs = {String.valueOf(products.getProductBarcode())};
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        return deletedRows > 0;
    }

    public List<Products> getProductsByBarcodes(List<String> barcodes) {
        List<Products> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // Chuyển đổi danh sách barcodes thành chuỗi dấu hỏi
            String placeholders = new String(new char[barcodes.size()]).replace("\0", "?").trim();
            String selection = COLUMN_PRODUCT_BARCODE + " IN (" + placeholders + ")";
            String[] selectionArgs = barcodes.toArray(new String[0]);

            Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                    String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
                    String productUnit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_UNIT));
                    String productBarcode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BARCODE));
                    String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));

                    Products product = new Products(null, productName, productQuantity, productUnit, productBarcode, productPrice, null, null, null);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            db.close();
        }

        return productList;
    }
}
