package com.example.workpermit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUid(String uid) {
        prefs.edit().putString("uid", uid).commit();
    }

    public void setName(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public void setLevel(String level) {
        prefs.edit().putString("level", level).commit();
    }

    public String getUid() {
        String uid = prefs.getString("uid","");
        return uid;
    }

    public String getName() {
        String name = prefs.getString("name","");
        return name;
    }

    public String getLevel() {
        String level = prefs.getString("level","");
        return level;
    }

}
