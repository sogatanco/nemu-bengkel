package site.team2dev.nemubengkel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE=0;

private static final String PREFER_NAME="AndroidExamplePref";

private static final String IS_USER_LOGIN="IsUserLoggedIn";

public static final String KEY_NAME="name";

public static final String KEY_EMAIL="email";

public UserSessionManager(Context context){
    this._context=context;
    pref=context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
    editor=pref.edit();
}

public void createUserLoginSession(String name, String email){
    editor.putBoolean(IS_USER_LOGIN, true);
    editor.putString(KEY_NAME, name);
    editor.putString(KEY_EMAIL, email);
    editor.commit();
}

public boolean checkLogin(){
    if (!this.isUserLoggedIn()) {

        Intent i=new Intent(_context, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);

        return true;
    }
    return false;
}

public HashMap<String, String> getUserDetails(){

    HashMap<String, String> user=new HashMap<>();

    user.put(KEY_NAME,pref.getString(KEY_NAME, null));

    user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

    return user;
}

public void logoutUser(){
    editor.clear();
    editor.commit();

    Intent i=new Intent(_context, LoginActivity.class);

    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    _context.startActivity(i);
}

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
