package site.team2dev.nemubengkel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, password1, password2;
    private RequestQueue requestQueue;
    private static String URL="";
    private StringRequest stringRequest;
    Fungsi fungsi = new Fungsi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        URL=getString(R.string.base_url)+"api/register";
        email=(EditText)findViewById(R.id.regemail);
        password1=(EditText)findViewById(R.id.regpassword1);
        password2=(EditText)findViewById(R.id.regpassword2);

        requestQueue= Volley.newRequestQueue(this);

        fungsi.checkEmail(email);
        fungsi.lengthPass(password1);

    }



    public void goLogin(View view) {
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }




    public void addUser(View view) {

        EditText[] editTexts={email,password2, password1};

        fungsi.emptyChecker(editTexts);
        fungsi.matchPass(password1, password2);

        if(!fungsi.isHasError(editTexts)){
            stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Field ", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap=new HashMap<String, String>();
                    hashMap.put("email",email.getText().toString());
                    hashMap.put("pass",password1.getText().toString());
                    hashMap.put("token", "1234567");
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        }

    }
}
