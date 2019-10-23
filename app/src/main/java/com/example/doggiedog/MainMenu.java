package com.example.doggiedog;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    public static final String DATABASE_NAME = "progress_data";

    public static SQLiteDatabase database;
    Button Play;
    Button Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Variables and widgets.
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        CreateTable();

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main);
        layout.setBackground( getDrawable(R.drawable.pawbackground) );

        Play = (Button) findViewById(R.id.playBtn);
        Play.setOnClickListener(this);

        Exit = (Button) findViewById(R.id.exitBtn);
        Exit.setOnClickListener(this);

        //////////////////////////


    } // end of onCreate.

    public void exitApp(){
        finish();
        System.exit(0);
    } // method to terminate the app

    // database methods.
    private void CreateTable () {
        String str = "CREATE TABLE IF NOT EXISTS savess(id TEXT, name TEXT, date TEXT, log TEXT, progresspoints TEXT, money TEXT, hunger TEXT, thirst TEXT, fatigue TEXT);";
        database.execSQL(str);
    }

    public static boolean InsertData(GameProgress gameProgress, int id) {
        ContentValues cv = new ContentValues();

        String ids = "" + id;
        String name = gameProgress.getDogName();
        String date = "" + gameProgress.getDateOfBirth();
        String lastlog = gameProgress.getLastLog();
        String progresspoints = "" + gameProgress.getProgressPoints();
        String money = "" + gameProgress.getMoney();
        String hunger = "" + gameProgress.getHunger();
        String thirst = "" + gameProgress.getThirst();
        String fatigue = "" + gameProgress.getFatigue();

        cv.put("id",ids);
        cv.put("name",name);
        cv.put("date",date);
        cv.put("log",lastlog);
        cv.put("progresspoints",progresspoints);
        cv.put("money",money);
        cv.put("hunger",hunger);
        cv.put("thirst",thirst);
        cv.put("fatigue",fatigue);

        long res = database.insert("savess",null,cv);

        if (res == -1)
            return false;
        else
            return true;
    }


    public static boolean UpdateData(String ids, String lastlog, String progresspoints, String money, String hunger, String thirst, String fatigue) {
        ContentValues cv = new ContentValues();

        cv.put("id",ids);
        cv.put("progresspoints",progresspoints);
        cv.put("money",money);
        cv.put("hunger",hunger);
        cv.put("thirst",thirst);
        cv.put("fatigue",fatigue);
        cv.put("log",lastlog);
        database.update("savess",cv,"id = ?",new String[] {ids});

        return true;
    }

    public static Cursor GetAllData() {
        Cursor c = database.rawQuery("SELECT * FROM savess",null);
        return c;
    }

    // checks if the slot "id" exists. returns position if it is, otherwise returns -1;
    public static int CheckIfSaveExist (Cursor c,int id) {
        String sid = "" + id;
        if(c.getCount() == 0)
            return -1;
        c.moveToFirst();
        do {

            if(c.getString(0).equals(sid))
                return c.getPosition();

        }while(c.moveToNext());
        return -1;
    }
    ///////////

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {


        if(v == Play) { // play, the user can choose his personal progress.
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(MainMenu.this);
            View dView = getLayoutInflater().inflate(R.layout.progress_choice,null);

            Cursor c = GetAllData();
            // buttons of each progress.
            Button choice_1 = (Button) dView.findViewById(R.id.progress_1);
            Button choice_2 = (Button) dView.findViewById(R.id.progress_2);
            Button choice_3 = (Button) dView.findViewById(R.id.progress_3);
            Button choice_4 = (Button) dView.findViewById(R.id.progress_4);
            Button choice_5 = (Button) dView.findViewById(R.id.progress_5);
            Button choice_6 = (Button) dView.findViewById(R.id.progress_6);
            Button choice_7 = (Button) dView.findViewById(R.id.progress_7);
            Button choice_8 = (Button) dView.findViewById(R.id.progress_8);
            Button choice_9 = (Button) dView.findViewById(R.id.progress_9);
            Button choice_10 = (Button) dView.findViewById(R.id.progress_10);
            /////

            // reads the data from the database if a progress exists sets the text of the button to display some information, if it doesn't displays "Empty".

            /*for each button checks if there is an existing save, if there is sets text to the saves name.
             * access the name by id in the database*/

            if(CheckIfSaveExist(c,1) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,1));
                choice_1.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_1.setText("Empty");
            }

            if(CheckIfSaveExist(c,2) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,2));
                choice_2.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_2.setText("Empty");
            }

            if(CheckIfSaveExist(c,3) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,3));
                choice_3.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_3.setText("Empty");
            }

            if(CheckIfSaveExist(c,4) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,4));
                choice_4.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_4.setText("Empty");
            }

            if(CheckIfSaveExist(c,5) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,5));
                choice_5.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_5.setText("Empty");
            }

            if(CheckIfSaveExist(c,6) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,6));
                choice_6.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_6.setText("Empty");
            }

            if(CheckIfSaveExist(c,7) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,7));
                choice_7.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_7.setText("Empty");
            }

            if(CheckIfSaveExist(c,8) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,8));
                choice_8.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_8.setText("Empty");
            }

            if(CheckIfSaveExist(c,9) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,9));
                choice_9.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_9.setText("Empty");
            }

            if(CheckIfSaveExist(c,10) != -1) {

                c.moveToPosition(CheckIfSaveExist(c,10));
                choice_10.setText("" + c.getString(1) + "   level: " + c.getString(4) + "   " + c.getString(2));

            } else {
                choice_10.setText("Empty");
            }
            ////


            // starts to show the progress table.
            dBuilder.setView(dView);
            AlertDialog dialog = dBuilder.create();
            dialog.show();

        } // (Play button)

        if(v == Exit) { // Exits the app.
            exitApp();
        }

    }

    /* in each button of progress slot, when clicked, first checks if there is a progress going
     * if the slot is null, creates a new data object and inserts it into the database and moves to
     * the initial activity, if there is already progress in the selected slot loads the game with its data */

    public void choice1(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,1) == -1) { // in case the progress slot is empty, creates a new progress in this slot.
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",1);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 1);
            finish();
            startActivity(intent);
        }
    }

    public void choice2(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,2) == -1) {
             intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",2);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 2);
            finish();
            startActivity(intent);
        }
    }

    public void choice3(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,3) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",3);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 3);
            finish();
            startActivity(intent);
        }
    }

    public void choice4(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,4) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",4);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 4);
            finish();
            startActivity(intent);
        }
    }

    public void choice5(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,5) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",5);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 5);
            finish();
            startActivity(intent);
        }
    }

    public void choice6(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,6) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",6);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 6);
            finish();
            startActivity(intent);
        }
    }

    public void choice7(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,7) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",7);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 7);
            finish();
            startActivity(intent);
        }
    }

    public void choice8(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,8) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",8);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 8);
            finish();
            startActivity(intent);
        }
    }

    public void choice9(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,9) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",9);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 9);
            finish();
            startActivity(intent);
        }
    }

    public void choice10(View view) {
        Cursor c = GetAllData();
        Intent intent;
        if(CheckIfSaveExist(c,10) == -1) {
            intent = new Intent(this,NameInitialization.class);
            intent.putExtra("id",10);
            finish();
            startActivity(intent);
        } else { // in case there is already a user data in this slot.
            intent = new Intent(this, Game.class);
            intent.putExtra("id", 10);
            finish();
            startActivity(intent);
        }
    }
} // end of Activity.



//             Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();

// database file: view -> tool windows -> device file explorer -> com.example.doggiedog