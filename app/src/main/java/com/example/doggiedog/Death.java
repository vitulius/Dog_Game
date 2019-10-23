package com.example.doggiedog;

import android.content.Intent;
import android.support.v4.app.NotificationCompatExtras;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.doggiedog.MainMenu.database;

public class Death extends AppCompatActivity {

    TextView txt;
    Button btn;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death);

        txt = (TextView) findViewById(R.id.deathtxt);
        btn = (Button) findViewById(R.id.deathbtn);
        Intent intent = getIntent();

        // Getting user basic information of the dead dog.
        id = intent.getIntExtra("id",-1);
        String name = intent.getStringExtra("name");

        txt.setText("" + name + " is dead :(\n Better luck next time.");
    }

    public void deathbutton(View view) {
        String x = "" + id;
        database.delete("savess", "id = ?",new String[] {x});
        Intent toMainMenu = new Intent(Death.this, MainMenu.class);
        startActivity(toMainMenu);
        finish();
    }
}
