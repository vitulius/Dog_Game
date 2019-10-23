package com.example.doggiedog;

// This class holds all the information about the progress of the user in the game.

import java.text.DateFormat;
import java.util.Calendar;



public class GameProgress {

    private String DogName;
    private String DateOfBirth;
    private String LastLog;
    private int ProgressPoints;
    private int money;
    private int hunger;
    private int thirst;
    private int fatigue;

    // Initial constructor.
    public GameProgress(String name) {
        Calendar cal = Calendar.getInstance();
        this.DateOfBirth = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cal.getTime());
        this.DogName = name;
        this.LastLog = name;
        this.ProgressPoints = 0;
        this.money = 0;
        this.hunger = 100;
        this.thirst = 100;
        this.fatigue = 100;

    }


    // getters.
    public String getDogName() {
        return this.DogName;
    } // returns dogs name.

    public String getDateOfBirth () {
        return this.DateOfBirth;
    } // returns date of birth.

    public String getLastLog() {
        return this.LastLog;
    }

    public int getProgressPoints () {
        return this.ProgressPoints;
    } // returns users progress points.

    public int getMoney() {
        return this.money;
    } // returns users money;

    public int getHunger() {
        return this.hunger;
    } // returns users hunger percentage;

    public int getThirst() {
        return this.thirst;
    } // returns users thirst percentage;

    public int getFatigue() {
        return this.fatigue;
    } // returns users fatigue percentage;
    ///
}
