package com.mo2a.example.triviagame.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighscore(int score){
        int lasrScore= preferences.getInt("high_score", 0);
        if(score > lasrScore){
            preferences.edit().putInt("high_score", score).apply();
        }
    }

    public int getHighscore(){
        return preferences.getInt("high_score", 0);
    }

    public void setState(int index){
        preferences.edit().putInt("index_state", index).apply();
    }

    public int getState(){
        return preferences.getInt("index_state", 0);
    }

}
