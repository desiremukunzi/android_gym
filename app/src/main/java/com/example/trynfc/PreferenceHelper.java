package com.example.trynfc;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

        private final String INTRO = "intro";
        private final String NAME = "name";
        private final String HOBBY = "id";
        private final String POSTID = "post_id";
        private SharedPreferences app_prefs;
        private Context context;


    public static final String LOGGED_IN_PREF = "logged_in_status";

        public PreferenceHelper(Context context) {
            app_prefs = context.getSharedPreferences("shared",
                    Context.MODE_PRIVATE);
            this.context = context;
        }

        public void putIsLogin(boolean loginorout) {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putBoolean(INTRO, loginorout);
            edit.commit();
        }

        public boolean getIsLogin() {
            return app_prefs.getBoolean(INTRO, false);
        }

        public void putName(String loginorout) {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putString(NAME, loginorout);
            edit.commit();
        }

        public String getName() {
            return app_prefs.getString(NAME, "");
        }

        public void putHobby(String loginorout) {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putString(HOBBY, loginorout);
            edit.commit();
        }

        public String getHobby() {
            return app_prefs.getString(HOBBY, "");
        }


    public void putPostId(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(POSTID, loginorout);
        edit.commit();
    }

    public void clearPostId() {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.clear();
        edit.commit();
    }

    public String getPostId() {
        return app_prefs.getString(POSTID, "");
    }

}
