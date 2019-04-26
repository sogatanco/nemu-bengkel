package site.team2dev.nemubengkel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class UtamaActivity extends AppCompatActivity {

    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

        session= new UserSessionManager(getApplicationContext());

        TextView name=(TextView)findViewById(R.id.name);
        TextView email=(TextView)findViewById(R.id.email);

        Toast.makeText(getApplicationContext(), "User Login Status: "+session.isUserLoggedIn(),Toast.LENGTH_LONG).show();

        if(session.checkLogin())
            finish();

        HashMap<String, String> user=session.getUserDetails();

        String iname=user.get(UserSessionManager.KEY_NAME);

        String iemail=user.get(UserSessionManager.KEY_EMAIL);

        name.setText(Html.fromHtml("Nama: <b>"+iname+"</b>"));
        email.setText(Html.fromHtml("Email:<b>"+iemail+"</b>"));



    }

    public void logout(View view) {
        session.logoutUser();
    }
}
