package com.example.doggiedog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.doggiedog.MainMenu.CheckIfSaveExist;
import static com.example.doggiedog.MainMenu.GetAllData;
import static com.example.doggiedog.MainMenu.UpdateData;
import static com.example.doggiedog.MainMenu.database;

public class Game extends AppCompatActivity {

    static final int APPLE_COST = 5;
    static final int PEACH_COST = 5;
    static final int BACON_COST = 7;
    static final int WATER_COST = 15;
    static final int ENERGYDRINK_COST = 15;
    static final int DOGFOOD_COST = 25;
    static final int STEAK_COST = 40;
    static final int BONE_COST = 150;
    static final int ADRENALINE_COST = 150;
    static final int WATERMELON_COST = 150;
    static final int POTION_COST = 300;


    int id; // defines the id that's the game operates on.
    int progresspoints; // progresspoints of the current user;
    int money; // stores the amount of money the current user has.
    int sec = 0;
    float currX,currY; // used to determine the position of the dog.

    String lastlog; // date and time of the last logout;
    String currlog; // date and time of the current game exec;

    boolean isdead = false;
    boolean dogissleeping = false;
    boolean dogismoving = false; // defines if the dog is on the move or not.
    boolean petting = false;

    ConstraintLayout background;
    TextView foodprc;
    TextView waterprc;
    TextView sleepprc;
    TextView moneytxt;
    Cursor c;
    ProgressBar food;
    ProgressBar water;
    ProgressBar sleep;

    GestureDetector gesture;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // setting pointers
         ImageView dog = (ImageView) findViewById(R.id.imgvU);
         background = (ConstraintLayout) findViewById(R.id.background);
         food = (ProgressBar) findViewById(R.id.foodbar);
         water = (ProgressBar) findViewById(R.id.waterbar);
         sleep = (ProgressBar) findViewById(R.id.sleepbar);
         foodprc = (TextView) findViewById(R.id.foodprctxt);
         waterprc = (TextView) findViewById(R.id.waterprctxt);
         sleepprc = (TextView) findViewById(R.id.sleepprctxt);
         moneytxt = (TextView) findViewById(R.id.moneytxt);
         c = GetAllData();

         XGestureDetector xGestureDetector = new XGestureDetector();
         gesture = new GestureDetector(Game.this,xGestureDetector);


        // inverts horizontally the progress bars.
        food.setRotation(180);
        water.setRotation(180);
        sleep.setRotation(180);


        // getting screen height and width.
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y;


        //setting dogs initial position.
        currY = height/2 -90;
        currX = width/2 -90;
        dog.setImageResource(R.drawable.sitting);
        AnimationDrawable sit = (AnimationDrawable) dog.getDrawable();
        sit.start();



        // loads the users data from the database into the activity.
        LoadInformation();

        // at this point the game is ready to play and the time count starts.
        StartCount();

    }

    // this method loads the information of the user from the database. ***
    private void LoadInformation() {
        GetId();
        c.moveToPosition( CheckIfSaveExist(c,id) ); // sets the cursor to point on the current users id.


        lastlog = c.getString(3);
        food.setProgress( Integer.parseInt( c.getString(6)));
        water.setProgress( Integer.parseInt( c.getString(7)));
        sleep.setProgress( Integer.parseInt( c.getString(8)));

        foodprc.setText("" + food.getProgress() + "%");
        waterprc.setText("" + water.getProgress() + "%");
        sleepprc.setText("" + sleep.getProgress() + "%");


        money = Integer.parseInt(c.getString(5));
        moneytxt.setText("" + money);

        progresspoints = Integer.parseInt(c.getString(4));

    }

    // gets the id of the current playing user.
    private void GetId() {
        Intent intent = getIntent();
        this.id = intent.getIntExtra("id",-1);
    }

    // this method moves the dog on the screen. (executes animations)
    private void movedog(float destX,float destY) {
        dogismoving = true;
        final ImageView dog = (ImageView) findViewById(R.id.imgvU);

        // creates the walking path of the dog.
        Path path = new Path();
        path.moveTo(currX,currY);
        path.lineTo(destX,destY);
        path.moveTo(destX,destY);


        //executes the walking animation.
        ObjectAnimator animator = ObjectAnimator.ofFloat(dog, View.X,View.Y ,path);
        animator.setDuration(distance(destX,destY)*2);
        animator.start();

        // stores the new position of the dog as the current position.
        currX = destX;
        currY = destY;

        // if the dog didn't reach destination, blocks the option to tap again. the option to tap on a new location unlocks only when the walking animation is over.
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dogismoving = false;

                if(dogissleeping)
                    sleepanimation();
                else
                    sitanimation();
            }
        });
    }

    // this method executes the sitting animation
    private void sitanimation() {
        ImageView dog = (ImageView) findViewById(R.id.imgvU);
        dog.setImageResource(R.drawable.sit_action);
        AnimationDrawable sit = (AnimationDrawable) dog.getDrawable();
        sit.setOneShot(true);
        sit.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dogismoving == false) {
                    ImageView dog = (ImageView) findViewById(R.id.imgvU);
                    dog.setImageResource(R.drawable.sitting);
                    AnimationDrawable sit = (AnimationDrawable) dog.getDrawable();
                    sit.setOneShot(false);
                    sit.start();
                }
            }
        },500);

    }

    // this method makes the sleeping animation of the dog.
    private void sleepanimation() {
        ImageView dog = (ImageView) findViewById(R.id.imgvU);
        dog.setImageResource(R.drawable.sleep);
        AnimationDrawable sleep = (AnimationDrawable) dog.getDrawable();
        sleep.setOneShot(false);
        sleep.start();
    }

    // this method calculates the walk angle based on xy coordinates to determine what animation to use.
    private double Angle(float destX,float destY) {
        //first quarter
        if(destX >= currX && destY <= currY) {
            return Math.abs( Math.atan( (destY-currY) / (destX-currX) ) * 180 / Math.PI );
        }

        //second quarter
        if(destX <= currX && destY <= currY) {
            return 180 - Math.abs( Math.atan( (destY-currY) / (destX-currX) ) * 180 / Math.PI );
        }

        //third quarter
        if(destX <= currX && destY >= currY) {
            return 180 + Math.abs( Math.atan( (destY-currY) / (destX-currX) ) * 180 / Math.PI );
        }

        //fourth quarter
        if(destX >= currX && destY >= currY) {
            return 360 - Math.abs( Math.atan( (destY-currY) / (destX-currX) ) * 180 / Math.PI );
        }
        return 420;
    }

    // this method calculates the distance between the current position to the destination.
    private int distance(float destX,float destY) {
        int x = (int) Math.sqrt(Math.pow(currX - destX, 2) + Math.pow(currY - destY, 2));
        return x;
    }

    // starts the sub - thread (Time functionality)
    public void StartCount() {
        XRunnable runnable = new XRunnable();
        new Thread(runnable).start();
    }

    // opens pause menu
    public void pause(View view) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(Game.this);
        View dView = getLayoutInflater().inflate(R.layout.pause_menu,null);




        dBuilder.setView(dView);
        AlertDialog dialog = dBuilder.create();
        dialog.show();
    }

    // deletes the user progress slot
    public void DeleteUser(View view) {
        String x = "" + id;
        database.delete("savess", "id = ?",new String[] {x});
        Intent toMainMenu = new Intent(Game.this, MainMenu.class);
        startActivity(toMainMenu);
        finish();
    }

    // exits to main menu
    public void ExitUser(View view) {
        Intent toMainMenu = new Intent(Game.this, MainMenu.class);
        startActivity(toMainMenu);
        finish();
    }




    // opens the shop window
    public void Shop(View view) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(Game.this);
        View dView = getLayoutInflater().inflate(R.layout.shop,null);
        dBuilder.setView(dView);
        AlertDialog dialog = dBuilder.create();
        dialog.show();
    }


// SHOP PRODUCTS
/*

apple: +2f +1w
peach: +2w +1f
bacon: +5f
water: +10w
energy drink: +5w +5s
dog food: +20f -5w
steak: +30f
bone: +100f
adrenaline injection: +100s -10w
watermelon: +100w
super potion: +100f +100w +100s

*/
    public void apple(View view) {
        if(money >= APPLE_COST) {
            money -= APPLE_COST;

            food.setProgress( food.getProgress() + 2 );
            water.setProgress( water.getProgress() + 1 );

            Toast.makeText(this, "+2f +1w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + APPLE_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void peach(View view) {
        if(money >= PEACH_COST) {
            money -= PEACH_COST;

            food.setProgress( food.getProgress() + 1 );
            water.setProgress( water.getProgress() + 2 );

            Toast.makeText(this, "+2w +1f", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + PEACH_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void bacon(View view) {
        if(money >= BACON_COST) {
            money -= BACON_COST;

            food.setProgress( food.getProgress() + 5 );

            Toast.makeText(this, "+5f", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + BACON_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void water(View view) {
        if(money >= WATER_COST) {
            money -= WATER_COST;

            water.setProgress( water.getProgress() + 10 );

            Toast.makeText(this, "+10w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + WATER_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void energydrink(View view) {
        if(money >= ENERGYDRINK_COST) {
            money -= ENERGYDRINK_COST;

            sleep.setProgress( sleep.getProgress() + 5 );
            water.setProgress( water.getProgress() + 5 );

            Toast.makeText(this, "+5s +5w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + ENERGYDRINK_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void dogfood(View view) {
        if(money >= DOGFOOD_COST) {
            money -= DOGFOOD_COST;

            food.setProgress( food.getProgress() + 20 );
            water.setProgress( water.getProgress() - 5 );

            Toast.makeText(this, "+20f -5w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + DOGFOOD_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void steak(View view) {
        if(money >= STEAK_COST) {
            money -= STEAK_COST;

            food.setProgress( food.getProgress() + 30 );


            Toast.makeText(this, "30f", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + STEAK_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void bone(View view) {
        if(money >= BONE_COST) {
            money -= BONE_COST;

            food.setProgress( food.getProgress() + 100 );

            Toast.makeText(this, "+100f", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + BONE_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void adrenaline(View view) {
        if(money >= ADRENALINE_COST) {
            money -= ADRENALINE_COST;

            sleep.setProgress( sleep.getProgress() + 100 );
            water.setProgress( water.getProgress() - 10 );

            Toast.makeText(this, "+100s -10w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + ADRENALINE_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void watermelon(View view) {
        if(money >= WATERMELON_COST) {
            money -= WATERMELON_COST;

            water.setProgress( water.getProgress() + 100 );

            Toast.makeText(this, "+100w", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + WATERMELON_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }

    public void potion(View view) {
        if(money >= POTION_COST) {
            money -= POTION_COST;

            food.setProgress( food.getProgress() + 100 );
            water.setProgress( water.getProgress() + 100 );
            sleep.setProgress( sleep.getProgress() + 100 );

            Toast.makeText(this, "+100f +100w +100s", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "The price is " + POTION_COST + ".\nyou don't have enough.", Toast.LENGTH_SHORT).show();
        }
    }
    //////////////


    // a separate code to count time in order to update the bars.
    class XRunnable implements Runnable {
        @Override
        public void run() {
            while(1 != 0) {

                if(isdead)
                    break;

                   runOnUiThread(new Runnable() { // executes code in the user interface thread.
                       @Override
                       public void run() {

                           if(sec % 3 == 0 && sec != 0) { // decreases each bar after each 288 seconds. (100 times in 8 hours)
                               food.setProgress(food.getProgress() - 1);
                               water.setProgress(water.getProgress() - 1);
                               if(!dogissleeping)
                                   sleep.setProgress( sleep.getProgress() - 1 );
                           }


                           if(dogissleeping && sec % 60 == 0) { // if the dog sleeps increases sleep bar for each minute.
                               sleep.setProgress(sleep.getProgress() + 1);
                           }

                           // If the user is petting the dog increasing money for each second.
                           if(petting)
                               money++;


                           moneytxt.setText("" + money);
                           foodprc.setText("" + food.getProgress() + "%");
                           waterprc.setText("" + water.getProgress() + "%");
                           sleepprc.setText("" + sleep.getProgress() + "%");


                           // Death condition: if one of the bars reach 0 OR the average is below or equal to 30
                           if(food.getProgress() == 0 || water.getProgress() == 0 || sleep.getProgress() == 0 || (food.getProgress() + water.getProgress() + sleep.getProgress()) / 3 <= 30){
                               isdead = true;
                           }
                       }
                   });

                try {
                    Thread.sleep(1000);
                    sec ++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }////

            // at this point the dog is dead and the count stops.
            Intent intent = new Intent(Game.this, Death.class);
            intent.putExtra("id",id);
            intent.putExtra("name", c.getString(1));
            startActivity(intent);
            finish();
        }
    }


    class XGestureDetector implements android.view.GestureDetector.OnGestureListener ,android.view.GestureDetector.OnDoubleTapListener {


        // Executed when the screen is double tapped.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if( (e.getY() - 100) >= 1002 ){
                if (!dogissleeping) {
                    background.setBackground(getDrawable(R.drawable.sleepbackground));
                    dogissleeping = true;
                } else {
                    background.setBackground(getDrawable(R.drawable.testbackground));
                    dogissleeping = false;
                }
            }
            return false;
        }


        // Executed when the screen is tapped.
        @Override
        public boolean onDown(MotionEvent e) {


            if(  !dogismoving && (e.getY() - 100) >= 1002) {
                float destx = e.getX() - 100;
                float desty = e.getY() - 100;
                double angle = Angle(destx, desty);

                //if the initial coords are on the dog starts petting.
                if(destx <= currX + 100 && destx >= currX - 100 && desty <= currY + 100 && desty >= currY - 100) {
                    petting = true;
                    return false;
                }


                // Right walk animation
                if (angle >= 0 && angle <= 45 || angle >= 315 && angle <= 360) {
                    ImageView dog = (ImageView) findViewById(R.id.imgvU);
                    dog.setImageResource(R.drawable.walk_right);
                    AnimationDrawable animation = (AnimationDrawable) dog.getDrawable();
                    animation.start();
                    movedog(destx, desty);
                }

                // Up walk animation
                if (angle >= 45 && angle <= 135) {
                    ImageView dog = (ImageView) findViewById(R.id.imgvU);
                    dog.setImageResource(R.drawable.walk_up);
                    AnimationDrawable animation = (AnimationDrawable) dog.getDrawable();
                    animation.start();
                    movedog(destx, desty);
                }

                // Left walk animation
                if (angle >= 135 && angle <= 225) {
                    ImageView dog = (ImageView) findViewById(R.id.imgvU);
                    dog.setImageResource(R.drawable.walk_left);
                    AnimationDrawable animation = (AnimationDrawable) dog.getDrawable();
                    animation.start();
                    movedog(destx, desty);
                }

                // Down walk animation
                if (angle >= 225 && angle <= 315) {
                    ImageView dog = (ImageView) findViewById(R.id.imgvU);
                    dog.setImageResource(R.drawable.walk_down);
                    AnimationDrawable animation = (AnimationDrawable) dog.getDrawable();
                    animation.start();
                    movedog(destx, desty);
                }
            }
            return false;
        }






        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    // Managing gestures.
    public boolean onTouchEvent(MotionEvent event) {

        if( event.getAction() == MotionEvent.ACTION_UP) {
            petting = false;
        }


        gesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Code executed when activity finishes.
    protected void onStop () {
        Calendar cal = Calendar.getInstance();
        lastlog = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(cal.getTime());

        UpdateData("" + id, "" + lastlog, "" + progresspoints, "" + money, "" + food.getProgress(), "" + water.getProgress(), "" + sleep.getProgress());
        finish();
        super.onStop();
    }




/*
*       length = 14
*       Minutes  s.substring(12,14)
*       Hours    s.substring(9,11)
*       Days     s.substring(0,1)
*       Months   s.substring(2,3)
*       Years    s.substring(4,8)
*
*       length = 15
*       if( s.charAt(1) == '.' )       // if this is true the days is a single digit, if not months are single digit.
*       Minutes  s.substring(13,15)
*       Hours    s.substring(10,12)
*       Days     s.substring(0,1)
*       Months   s.substring(2,4)
*       Years    s.substring(5,9)
*
*
*       else
*       length = 15
*       Minutes  s.substring(13,15)
*       Hours    s.substring(10,12)
*       Days     s.substring(0,2)
*       Months   s.substring(3,4)
*       Years    s.substring(5,9)
*
*
*
*       length = 16
*       Minutes  s.substring(14,16)
*       Hours    s.substring(11,13)
*       Days     s.substring(0,2)
*       Months   s.substring(3,5)
*       Years    s.substring(6,10)
*
*
* */











/*
        progress bar colors:

        start #43a047 green
        center #fdff0c yellow
        end #fb0c0c red
*/




//  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
// screen dimentions:
// height: 2094
// width: 1080

}
