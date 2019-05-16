package site.team2dev.nemubengkel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    Fungsi fungsi = new Fungsi();
    UserSessionManager session;
    private EditText passnow, newpass, konfir;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session=new UserSessionManager(getApplicationContext());

        requestQueue= Volley.newRequestQueue(this);

        passnow=(EditText)findViewById(R.id.pass_lama);
        newpass=(EditText)findViewById(R.id.pass_baru);
        konfir=(EditText)findViewById(R.id.konfirmsi);

        fungsi.lengthPass(newpass);
    }

    public void batal(View view) {
        finish();
    }

    public void simpan(View view) {
        EditText[] editTexts={passnow, newpass, konfir};

        fungsi.emptyChecker(editTexts);
        fungsi.matchPass(newpass, konfir);

        if(!fungsi.isHasError(editTexts)){
            final  String URL=getString(R.string.base_url)+"api/editprofil/password";
            StringRequest updatePass=new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    finish();
                    session.logoutUser();
                    Toast.makeText(getApplicationContext(), "password Updated",Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Failed TO update",Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap=new HashMap<String, String>();
                    hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                    hashMap.put("lama", passnow.getText().toString());
                    hashMap.put("baru", newpass.getText().toString());
                    return hashMap;
                }
            };
            requestQueue.add(updatePass);
        }
    }
}
