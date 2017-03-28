package com.lotus.ieee;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Bharadwaj on 29/09/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DBPath="/data/data/com.lotus.ieee/databases/";
    private static String DBName= "ieeede.db";
    public SQLiteDatabase sqLiteDatabase;
    private final Context mycontext;

    public DatabaseHelper(Context context) {
        super(context, DBName, null, 1);
        this.mycontext=context;
    }

    public void createdatabase() throws IOException {
        boolean dbExist=checkDatabase();
        if(dbExist){

        }else{
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB=null;
        try{
            String myPath=DBPath+DBName;
            checkDB=SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch (SQLiteException e){

        }
        if (checkDB!=null){
                checkDB.close();
        }
        return checkDB!=null;
    }

    private void copyDatabase() throws IOException{
        InputStream myInput=mycontext.getAssets().open(DBName);
        String OutFileName=DBPath+DBName;
        OutputStream myOutput=new FileOutputStream(OutFileName);
        byte[] buffer=new byte[1024];
        int length;
        while((length=myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();myOutput.close();myInput.close();
    }

    public void openDatabase() throws IOException{
        String mypath=DBPath+DBName;
        sqLiteDatabase=SQLiteDatabase.openDatabase(mypath,null,SQLiteDatabase.OPEN_READWRITE);
        Log.d("Successfully opened", mypath);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InsertAfterQR(String name,String pno,String passno,String answer,String eventname){

        String input=Answer_Check(eventname);
        int score=ScoreCount(input,answer);
        sqLiteDatabase.execSQL("insert into info values('"+name+"','"+pno+"','"+passno+"','"+answer+"','"+score+"');");
    }
    int count=0;
    public int ScoreCount(String ins,String ans){
        for(int i=0;i<ans.length();i++){
            if(ins.charAt(i)==ans.charAt(i)){
                count++;
            }
        }
        return count;
    }
    public String Answer_Check(String eventname){
        Cursor cursor=sqLiteDatabase.rawQuery("select answer from EventAnswer where Eventname='"+eventname+"';",null);
        if(cursor.moveToFirst()){
            return cursor.getString(2);
        }
        return null;
    }
    public String EventQues(int x,String eventname){
        //x =0 means all the events will be displayed
        //Eventname----Password----Answer
        Cursor cursor=sqLiteDatabase.rawQuery("select * from Events;",null);
        if(x==0){
            if(cursor.moveToFirst()){
                return cursor.getString(0);

            }else{

            }
        }else if(x==1){
            Cursor c=sqLiteDatabase.rawQuery("select * from Events where Eventname='"+eventname+"';",null);
            return c.getString(2);
        }
        return null;
    }

    public String Questions(String eventname,int i,int j){
        Cursor c=sqLiteDatabase.rawQuery("select * from Questions where ename='"+eventname+"';",null);
        if(c.moveToFirst()){
            c.moveToPosition(i);
            return c.getString(j);
        }
        c.close();
        return null;
    }
    public int count(String eventname,int value){
        int k=0;
        Cursor c=sqLiteDatabase.rawQuery("select count(*) from info where Total >="+value+" and Event ='"+eventname+"';",null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }
    public String Shortlist(int value,int position,int place,String eventname){
        Cursor c=sqLiteDatabase.rawQuery("select * from info where Total >="+value+" and Event ='"+eventname+"';",null);
        try {
            if (c.moveToFirst()) {
                c.moveToPosition(position);
                return c.getString(place);
            }
        }catch (Exception e){
            Log.d("Error",""+e);
        }
        return null;
    }
}
