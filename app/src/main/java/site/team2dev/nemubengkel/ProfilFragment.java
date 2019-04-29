package site.team2dev.nemubengkel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ProfilFragment extends Fragment {

UserSessionManager session;
Fungsi fungsi=new Fungsi();

private RequestQueue requestQueue;
private StringRequest stringRequest;
private TextView nama, gender;
private ImageView profil;
private String username;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profil_fragment, null);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session=new UserSessionManager(getActivity().getApplicationContext());
        username=fungsi.getUsername( session.getUserDetails().get(UserSessionManager.KEY_TOKEN));

        requestQueue= Volley.newRequestQueue(getActivity());



        nama=(TextView)getView().findViewById((R.id.nama));
        gender=(TextView)getView().findViewById(R.id.gender);
        profil=(ImageView)getView().findViewById(R.id.profile_image);

        getUserData();



//        JWT jwt=new JWT( session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
//        Claim nama=jwt.getClaim("username");
//        String parsedValue = nama.asString();

//        Toast.makeText(getActivity(),fungsi.getUsername( session.getUserDetails().get(UserSessionManager.KEY_TOKEN)), Toast.LENGTH_LONG).show();




    }

    private void getUserData(){
        final String URL=getString(R.string.base_url)+"api/general/user?token=1234567&email="+username;
        JsonObjectRequest getUserData=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("data");
                            JSONObject data=array.getJSONObject(0);
                            nama.setText(data.getString("us_nama").toUpperCase());
                            gender.setText(data.getString(("us_jk")));
                            if(!data.getString("us_profil").equals("null")){
                                Picasso.get().load(getString(R.string.base_url)+"asset/images/"+data.getString("us_profil")).into(profil);
                            }
//                            Log.d("response",data.getString("us_nama"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });

        requestQueue.add(getUserData);
    }


}
