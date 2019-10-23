package com.example.doggiedog;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Attributes;

import static com.example.doggiedog.MainMenu.InsertData;
import static com.example.doggiedog.MainMenu.database;

// this activity will ask the user for a new name to the dog and create a new save slot and insert it into the database.

public class NameInitialization extends AppCompatActivity implements View.OnClickListener {

    View dView;
    EditText getname;
    Button okay;
    String name;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {             // Activity: NameInitialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_initialization);

        dView = getLayoutInflater().inflate(R.layout.ask_name,null);
        okay = findViewById(R.id.okaybtn);
        okay.setOnClickListener(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);

    }
    @Override
    public void onClick(View v) {
        if(v == okay) {
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(NameInitialization.this);

            // starts to show the name input window.
            dBuilder.setView(dView);
            AlertDialog dialog = dBuilder.create();
            dialog.show();
        }
    }
    public void cool(View view) {
        getname = dView.findViewById(R.id.getnametxtview);
        name = getname.getText().toString().trim();
        InsertData(new GameProgress(name),id);
        finish();
        Intent intent = new Intent(this,Game.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

} // end of activity.
