package site.team2dev.nemubengkel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button submit;
    private RequestQueue requestQueue;
    private static final String URL="http://nebeng.foways.com/api/login";
    private StringRequest stringRequest;

    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session=new UserSessionManager(getApplicationContext());

    }

    public void goRegister(View view) {
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void cekLogin(View view) {
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        requestQueue= Volley.newRequestQueue(this);
        if(email.getText().toString().trim().length()==0 || password.getText().toString().trim().length()==0){
            Toast.makeText(this,"Fill username and password:",Toast.LENGTH_LONG).show();
        }
        else{
            stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        session.createUserLoginSession( jsonObject.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("keys","login");
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"not match", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap=new HashMap<String, String>();
                    hashMap.put("email", email.getText().toString());
                    hashMap.put("pass", password.getText().toString());
                    hashMap.put("token","1234567");
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        }


    }
}
