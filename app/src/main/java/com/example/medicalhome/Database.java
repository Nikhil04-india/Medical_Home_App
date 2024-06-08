package com.example.medicalhome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;
import androidx.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users (username text , email text ,password text)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart (username text , product text ,price float , otype text)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace (username text , fullname text ,contactno text, address text, pincode int, date text , time text, amount float , otype text)";
        sqLiteDatabase.execSQL(qry3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void register(String username , String email , String password){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email",email);
        cv.put("password",password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null,cv);
        db.close();
    }

    public int login(String username , String password){
        int result = 0;
        String[] str = new String [2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username =? and password=?", str);
        if (c.moveToFirst()){
            result=1;
        }
        return result;
    }

    public  void addCart (String username , String product , float price , String otype){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product" , product);
        cv.put("price" ,price);
        cv.put("otype",otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart",null,cv);
        db.close();
    }

    public int checkCart (String username , String product){
        int result =0 ;
        String str[] =new String[2];
        str[0] = username;
        str[1] = product;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from cart where username = ? and product = ?", str);
        if (c.moveToFirst()){
            result =1;
        }
        db.close();
        return  result;
    }

    public void removeCart (String username , String otype){
        String str[] =new String[2];
        str[0] = username;
        str[1] = otype;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "username=? and otype=?", str);
        db.close();
    }

    public ArrayList getCartData(String username , String otype){
     ArrayList<String> arr = new ArrayList<>();
     SQLiteDatabase db = getReadableDatabase();
     String str[] = new String[2];
     str[0] = username;
     str[1] = otype;
     Cursor c = db.rawQuery("select * from cart where username = ? and otype = ?  ", str);
     if (c.moveToFirst()){
         do {
             String product = c.getString(1);
             String price = c.getString(2);
             arr.add(product + "$" + price);
         } while(c.moveToNext());
     }
     db.close();
     return  arr;
    }

    public void addOrder(String username, String fullname, String address, String contact, int pincode, String date, String time, float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("contactno", contact);
        cv.put("pincode", pincode);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("amount", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("orderplace", null, cv);
        if (result == -1) {
            Log.d("Database", "Order insertion failed");
        } else {
            Log.d("Database", "Order inserted successfully");
        }
        db.close();
    }

    public ArrayList<String> getOrderData(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String str[] = new String[1];
        str[0] = username;
        Cursor c = db.rawQuery("SELECT * FROM orderplace WHERE username = ?", str);

        // Log the column names to help debug issues
        String[] columnNames = c.getColumnNames();
        for (String columnName : columnNames) {
            Log.d("Database", "Column name: " + columnName);
        }

        if (c.moveToFirst()) {
            do {
                // Ensure all columns are correctly retrieved
                int fullnameIdx = c.getColumnIndex("fullname");
                int contactIdx = c.getColumnIndex("contactno");
                int addressIdx = c.getColumnIndex("address");
                int pincodeIdx = c.getColumnIndex("pincode");
                int dateIdx = c.getColumnIndex("date");
                int timeIdx = c.getColumnIndex("time");
                int amountIdx = c.getColumnIndex("amount");
                int otypeIdx = c.getColumnIndex("otype");

                if (fullnameIdx >= 0 && contactIdx >= 0 && addressIdx >= 0 && pincodeIdx >= 0 && dateIdx >= 0 && timeIdx >= 0 && amountIdx >= 0 && otypeIdx >= 0) {
                    String fullname = c.getString(fullnameIdx);
                    String contact = c.getString(contactIdx);
                    String address = c.getString(addressIdx);
                    String pincode = c.getString(pincodeIdx);
                    String date = c.getString(dateIdx);
                    String time = c.getString(timeIdx);
                    String amount = c.getString(amountIdx);
                    String otype = c.getString(otypeIdx);

                    arr.add(fullname + "$" + contact + "$" + address + "$" + pincode + "$" + date + "$" + time + "$" + amount + "$" + otype);
                    Log.d("Database", "Order retrieved: " + fullname + ", " + contact + ", " + address + ", " + pincode + ", " + date + ", " + time + ", " + amount + ", " + otype);
                } else {
                    Log.d("Database", "One or more column indices are invalid.");
                }
            } while (c.moveToNext());
        } else {
            Log.d("Database", "No orders found for user: " + username);
        }

        c.close();
        db.close();
        return arr;
    }


    public int checkAppointmentExists(String username , String fullname , String address , String contact , String date , String time){
        int result=0;
        String str [] = new String[6];
        str[0]=  username ;
        str[1]=  fullname;
        str[2]=  address;
        str[3]=  contact;
        str[4]=  date;
        str[5]=  time;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from orderplace where username = ? and fullname = ? and address = ? and contactno = ? and date = ? and time = ?", str );
        if (c.moveToFirst()){
            result=1;
        }
        db.close();
        return result;
    }

}
