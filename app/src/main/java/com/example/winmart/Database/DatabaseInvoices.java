package com.example.winmart.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.winmart.Object.Invoices;
import com.example.winmart.Object.Products;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInvoices extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Invoices_Database";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "Invoices";
    private static final String COLUMN_INVOICE_ID = "InvoicesID";
    private static final String COLUMN_INVOICE_DATE = "InvoicesDate";
    private static final String COLUMN_INVOICE_PRODUCT_BARCODE = "InvoicesProductBarcode";
    private static final String COLUMN_INVOICE_PRODUCTS = "InvoicesProducts";
    private static final String COLUMN_INVOICE_QUANTITY = "InvoicesQuantity";
    private static final String COLUMN_INVOICE_PRICE = "InvoicesTotalPrice";


    public DatabaseInvoices(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_INVOICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INVOICE_DATE + " TEXT, " +
                COLUMN_INVOICE_PRODUCTS + " TEXT, " +
                COLUMN_INVOICE_PRODUCT_BARCODE + " TEXT, " +
                COLUMN_INVOICE_QUANTITY + " INTERGER, " +
                COLUMN_INVOICE_PRICE + " REAL)";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTableQuery);
        onCreate(sqLiteDatabase);
    }
    // Thêm hóa đơn vào cơ sở dữ liệu
    public long addInvoice(String date, String products, String productBarcode, int quantity, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INVOICE_DATE, date);
        values.put(COLUMN_INVOICE_PRODUCTS, products);
        values.put(COLUMN_INVOICE_PRODUCT_BARCODE, productBarcode);
        values.put(COLUMN_INVOICE_QUANTITY, quantity);
        values.put(COLUMN_INVOICE_PRICE, totalPrice);

        long result = -1;
        try {
            result = db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("Database Error", "Error inserting invoice: " + e.getMessage());
        }
        return result;
    }

    // Thêm phương thức để lấy danh sách hóa đơn theo ngày
    public List<Invoices> getInvoicesByDate(String invoiceDate) {
        List<Invoices> invoiceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_INVOICE_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{invoiceDate});

        if (cursor.moveToFirst()) {
            do {
                try {
                    int invoiceID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_ID));
                    String invoiceProduct = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_PRODUCTS));
                    String invoiceProductBarcode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_PRODUCT_BARCODE));
                    int invoiceQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_QUANTITY));
                    Double invoicePrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_PRICE));

                    // Tạo đối tượng Invoice từ dữ liệu lấy được và thêm vào danh sách
                    Invoices invoice = new Invoices(invoiceID, invoiceDate, invoiceProduct, invoiceProductBarcode, invoiceQuantity, invoicePrice);
                    invoiceList.add(invoice);
                } catch (Exception e) {
                    // Xử lý lỗi hoặc ghi log
                    Log.e("TAG DatabaseError", "Error converting data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return invoiceList;
    }
}
