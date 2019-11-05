package com.example.wildkarrde;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class cookie_storage {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public void store_cookie(String rawdata, Context inpcontext){
        preferences = inpcontext.getSharedPreferences("com.example.wildkarrde", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("Cookie", rawdata);
        editor.apply();
    }

    public String get_cookie(Context inpcontext)
    {
        preferences = inpcontext.getSharedPreferences("com.example.wildkarrde", MODE_PRIVATE);

        String cookie = preferences.getString("Cookie", null);

        if(cookie!=null){
            return cookie;
        }
        else
        {
            return "no cookie!";
        }
    }

    public void delete_cookie(Context inpcontext)
    {
        preferences = inpcontext.getSharedPreferences("com.example.wildkarrde", MODE_PRIVATE);

        editor = preferences.edit();

        editor.remove("Cookie");
        editor.commit();
    }
    
}
