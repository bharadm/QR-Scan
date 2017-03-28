package com.lotus.ieee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.IOException;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler{
    public ZXingScannerView mscanner;
    DatabaseHelper databaseHelper;
    EditText editText;
    ArrayAdapter<String> spiny;
    String items[]=new String[]{"readyaimfire","something"};
    Spinner spinner;
    int k1=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        spiny=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        spiny.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(spiny);
        editText=(EditText)findViewById(R.id.et_value);

        databaseHelper=new DatabaseHelper(this);
        try{
            databaseHelper.createdatabase();
        }catch (IOException ioe){
            throw new Error("Unable to create database");
        }
        try {
            databaseHelper.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bt_submit(View view){
        mscanner=new ZXingScannerView(this);
        setContentView(mscanner);
        mscanner.setResultHandler(this);
        mscanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Successfully scanned ! ");
        builder.setMessage("Participant Registered.");
        builder.show();
        builder.setCancelable(true);
        String_purify(result.getText());
    }
    TextView tv[][]=new TextView[20][5];

    public void bt_shortlist(View view){
//        grid.removeAllViews();
        k1=Integer.parseInt(editText.getText().toString());
        Toast.makeText(MainActivity.this, "k1 is "+String.valueOf(k1), Toast.LENGTH_SHORT).show();
        String sel=spinner.getSelectedItem().toString();
        Toast.makeText(MainActivity.this, "Number :"+String.valueOf(k1)+" Selected : "+sel, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Shortlist.class).putExtra("spinner",sel).putExtra("value",k1));
        finish();

    }
    String matter[]=new String[5];
    public void String_purify(String message){
        for(int i=0,k=0,l=0;i<message.length();i++){
            if(message.charAt(i)!='%'){
                //i++;
            }else if(message.charAt(i)=='#'){
                break;
            }else {
                matter[k]=message.substring(l,i);
                Toast.makeText(MainActivity.this,matter[k], Toast.LENGTH_SHORT).show();
                k++;
                l=i+1;
            }
        }
        try {
            databaseHelper.InsertAfterQR(matter[0], matter[1], matter[2], matter[3],matter[4]);
            Toast.makeText(MainActivity.this, ""+matter[0]+"--"+matter[1]+"--"+matter[2]+"--"+matter[3]+"--"+matter[4], Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Data has been entered successfully \n Wait for 5 seconds to enter again. :)", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
        }

}
