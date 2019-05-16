package site.team2dev.nemubengkel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE=0;

private static final String PREFER_NAME="Nebeng";

private static final String IS_USER_LOGIN="IsUserLoggedIn";

public static final String KEY_TOKEN="token";

public UserSessionManager(Context context){
    this._context=context;
    pref=context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
    editor=pref.edit();
}

public void createUserLoginSession( String token){
    editor.putBoolean(IS_USER_LOGIN, true);
    editor.putString(KEY_TOKEN, token);
    editor.commit();
}


public HashMap<String, String> getUserDetails(){

    HashMap<String, String> user=new HashMap<>();

    user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

    return user;
}

public void logoutUser(){
    editor.clear();
    editor.commit();

    Intent i=new Intent(_context, LoginActivity.class);
    _context.startActivity(i);

}

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
