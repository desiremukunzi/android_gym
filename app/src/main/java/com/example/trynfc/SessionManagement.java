package com.example.trynfc;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    private final String NAME = "name";
    private SharedPreferences app_prefs;

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

//    public void saveSession(User user){
//        //save session of user whenever user is logged in
//        int id = user.getId();
//
//        editor.putInt(SESSION_KEY,id).commit();
//    }

    public int getSession(){
        //return user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).commit();
    }


    public void putName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginorout);
        edit.commit();
    }

    public String getName() {
        return app_prefs.getString(NAME, "");
    }
}