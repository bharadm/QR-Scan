package com.lotus.ieee;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Shortlist extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    int k1;
    LinearLayout.LayoutParams ly;
    GridLayout grid;
    Button tv[][]=new Button[20][6];
    String eventname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortlist);
        grid=(GridLayout)findViewById(R.id.layout_grid);
        ly=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        eventname=getIntent().getStringExtra("spinner");
        k1=getIntent().getIntExtra("value", 0);
        databaseHelper=new DatabaseHelper(this);
        assert grid != null;
        grid.removeAllViews();
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
        Toast.makeText(Shortlist.this, "k1 is " + String.valueOf(k1), Toast.LENGTH_SHORT).show();
        int limit=0;
        String []heading=new String[]{"Name","ContactN","PassN","Answer","EventName","Total"};
        limit=databaseHelper.count(eventname,k1);
        Toast.makeText(Shortlist.this, "limit value "+String.valueOf(limit), Toast.LENGTH_SHORT).show();
        for(int i=0;i<6;i++){
            tv[0][i]=new Button(this);
            tv[0][i].setText(""+heading[i]);
            tv[0][i].setLayoutParams(ly);
            grid.addView(tv[0][i]);
        }
        for(int i=0;i<limit;i++){
            for(int j=0;j<6;j++){
                tv[i+1][j]=new Button(this);
                tv[i+1][j].setText(""+databaseHelper.Shortlist(k1,i,j,eventname));
                tv[i+1][j].setLayoutParams(ly);
                grid.addView(tv[i+1][j]);
            }
        }
    }
    public void bt_return(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
