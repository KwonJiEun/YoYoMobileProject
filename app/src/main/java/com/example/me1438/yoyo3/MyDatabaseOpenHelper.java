package com.example.me1438.yoyo3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by me1438 on 2018-06-09.
 */

public class MyDatabaseOpenHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "yoyoDB";
    public static final String TABLE_NAME = "yoyoTable";

    public static final String COLUMN_NUMBER = "number"; //핸드폰번호
    public static final String COLUMN_PW = "pw";  //비밀번호
    public static final String COLUMN_EMAIL = "emailAddress";  //이메일주소

    //1이면 허용 0이면 비허용
    public static final String COLUMN_ISLOCATION = "isLocation"; //위치찾기
    public static final String COLUMN_ISPICTURE = "isPicture";  //사진찍기
    public static final String COLUMN_ISALARM = "isAlarm";  //알람울리기
    public static final String COLUMN_ISSENSOR = "isSensor";

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
                COLUMN_NUMBER + " text primary key," +
                COLUMN_PW + " text," +
                COLUMN_EMAIL + " text," +
                COLUMN_ISLOCATION + " char(1)," +
                COLUMN_ISPICTURE + " char(1)," +
                COLUMN_ISALARM + " char(1)," +
                COLUMN_ISSENSOR + " char(1)" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void addProduct(String number, String pw, String emailAddress, String isLocation, String isPICTURE, String isALARM, String isSensor) {
        ContentValues values = new ContentValues();

        //핸드폰번호(키), 비밀번호, 이메일
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_PW, pw);
        values.put(COLUMN_EMAIL, emailAddress);
        values.put(COLUMN_ISLOCATION, isLocation);
        values.put(COLUMN_ISPICTURE, isPICTURE);
        values.put(COLUMN_ISALARM, isALARM);
        values.put(COLUMN_ISSENSOR, isSensor);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateProduct(String number, String pw, String emailAddress) {
        //권한 바꾸는건 다른 함수 만들어서
        //이부분 코드 복사에서 권한설정->1로. 권한해제->0으로
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(pw.equals("") && !emailAddress.equals("")){
            //이메일변경
            values.put(COLUMN_EMAIL, emailAddress);
            db.update( TABLE_NAME , values, COLUMN_NUMBER + "= \'" + number + "\'", null );
        }
        else if(!pw.equals("") && emailAddress.equals("")){
            //비밀번호변경
            values.put(COLUMN_PW, pw);
            db.update(TABLE_NAME , values, COLUMN_NUMBER + "= \'" + number + "\'", null );
        }
        else if(!pw.equals("") && !emailAddress.equals("")){
            //둘다변경
            values.put(COLUMN_EMAIL, emailAddress);
            values.put(COLUMN_PW, pw);
            db.update(TABLE_NAME , values, COLUMN_NUMBER + "= \'" + number + "\'", null );
        }
        db.close();
    }

    public Cursor selectQuery(String phoneNum) {
        String query = "select * from "+ TABLE_NAME + " where "+ COLUMN_NUMBER +"= \'"+phoneNum+"\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;  //커서 객체 반환함
    }

    public void dropTable(){
        String query = "drop table "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
    }
}
