package com.example.kari.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.kari.models.RepairOrder;
import com.example.kari.models.DetailingOrder;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ServiceDB";
    private static final int DATABASE_VERSION = 2;

    // Таблица заявок на ремонт
    private static final String TABLE_REPAIR = "repair_orders";
    private static final String KEY_ID = "id";
    private static final String KEY_OWNER_NAME = "owner_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CAR_MODEL = "car_model";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_PROBLEM = "problem_description";
    private static final String KEY_STATUS = "status";

    // Таблица заявок на детайлинг
    private static final String TABLE_DETAILING = "detailing_orders";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DEVICE_MODEL = "device_model";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REPAIR_TABLE = "CREATE TABLE " + TABLE_REPAIR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_OWNER_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_CAR_MODEL + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT DEFAULT '12:00',"
                + KEY_PROBLEM + " TEXT,"
                + KEY_STATUS + " TEXT DEFAULT 'active')";
        db.execSQL(CREATE_REPAIR_TABLE);

        String CREATE_DETAILING_TABLE = "CREATE TABLE " + TABLE_DETAILING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_OWNER_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_DEVICE_MODEL + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT DEFAULT '12:00',"
                + KEY_STATUS + " TEXT DEFAULT 'active')";
        db.execSQL(CREATE_DETAILING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Добавляем столбец времени к существующим таблицам
            db.execSQL("ALTER TABLE " + TABLE_REPAIR + " ADD COLUMN " + KEY_TIME + " TEXT DEFAULT '12:00'");
            db.execSQL("ALTER TABLE " + TABLE_DETAILING + " ADD COLUMN " + KEY_TIME + " TEXT DEFAULT '12:00'");
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPAIR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILING);
            onCreate(db);
        }
    }

    // Методы для работы с заявками на ремонт
    public boolean addRepairOrder(String ownerName, String phone, String carModel, String date, String time, String problem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OWNER_NAME, ownerName);
        values.put(KEY_PHONE, phone);
        values.put(KEY_CAR_MODEL, carModel);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        values.put(KEY_PROBLEM, problem);

        long result = db.insert(TABLE_REPAIR, null, values);
        return result != -1;
    }

    public boolean addDetailingOrder(String ownerName, String phone, String email, String deviceModel, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OWNER_NAME, ownerName);
        values.put(KEY_PHONE, phone);
        values.put(KEY_EMAIL, email);
        values.put(KEY_DEVICE_MODEL, deviceModel);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);

        long result = db.insert(TABLE_DETAILING, null, values);
        return result != -1;
    }

    // МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАЯВОК
    public boolean updateRepairOrder(RepairOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OWNER_NAME, order.getOwnerName());
        values.put(KEY_PHONE, order.getPhone());
        values.put(KEY_CAR_MODEL, order.getCarModel());
        values.put(KEY_DATE, order.getDate());
        values.put(KEY_TIME, order.getTime());
        values.put(KEY_PROBLEM, order.getProblem());
        values.put(KEY_STATUS, order.getStatus());

        int result = db.update(TABLE_REPAIR, values, KEY_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        return result > 0;
    }

    public boolean updateDetailingOrder(DetailingOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OWNER_NAME, order.getOwnerName());
        values.put(KEY_PHONE, order.getPhone());
        values.put(KEY_EMAIL, order.getEmail());
        values.put(KEY_DEVICE_MODEL, order.getDeviceModel());
        values.put(KEY_DATE, order.getDate());
        values.put(KEY_TIME, order.getTime());
        values.put(KEY_STATUS, order.getStatus());

        int result = db.update(TABLE_DETAILING, values, KEY_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        return result > 0;
    }

    // МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАЯВОК
    public boolean deleteRepairOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_REPAIR, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteDetailingOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_DETAILING, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ЗАЯВОК ПО ID
    public RepairOrder getRepairOrderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPAIR, null, KEY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            RepairOrder order = new RepairOrder();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            order.setOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_OWNER_NAME)));
            order.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
            order.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CAR_MODEL)));
            order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
            order.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
            order.setProblem(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROBLEM)));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
            cursor.close();
            return order;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public DetailingOrder getDetailingOrderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETAILING, null, KEY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            DetailingOrder order = new DetailingOrder();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            order.setOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_OWNER_NAME)));
            order.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
            order.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            order.setDeviceModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEVICE_MODEL)));
            order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
            order.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
            cursor.close();
            return order;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Методы для статистики
    public int getTotalRepairCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REPAIR, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTotalDetailingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DETAILING, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getActiveRepairsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REPAIR + " WHERE " + KEY_STATUS + " = 'active'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getActiveDetailingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DETAILING + " WHERE " + KEY_STATUS + " = 'active'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getCompletedRepairsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REPAIR + " WHERE " + KEY_STATUS + " = 'completed'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getCompletedDetailingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DETAILING + " WHERE " + KEY_STATUS + " = 'completed'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Получение всех заявок
    public List<RepairOrder> getAllRepairOrders() {
        List<RepairOrder> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPAIR, null);

        if (cursor.moveToFirst()) {
            do {
                RepairOrder order = new RepairOrder();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                order.setOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_OWNER_NAME)));
                order.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                order.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CAR_MODEL)));
                order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                order.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
                order.setProblem(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROBLEM)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    public List<DetailingOrder> getAllDetailingOrders() {
        List<DetailingOrder> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DETAILING, null);

        if (cursor.moveToFirst()) {
            do {
                DetailingOrder order = new DetailingOrder();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                order.setOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_OWNER_NAME)));
                order.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                order.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
                order.setDeviceModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEVICE_MODEL)));
                order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                order.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }
}