package site.team2dev.nemubengkel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListBengkel extends AppCompatActivity {
    private RequestQueue requestQueue;
    public List<Bengkel> bengkels;
    public RecyclerView.Adapter listAdapter;
    public RecyclerView recyclerView;
    Fungsi fungsi=new Fungsi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bengkel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        requestQueue= Volley.newRequestQueue(this);

        recyclerView=(RecyclerView)findViewById(R.id.list_dafar_bengkel);
        bengkels=new ArrayList<>();
        getDataBengkel();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        listAdapter=new ListAdapter(this,bengkels);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getDataBengkel(){
        final String URL=getString(R.string.base_url)+"/api/general/bengkel?token=1234567";
        JsonObjectRequest getBengkel=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("data");

                            for (int i=0;i<array.length();i++){
                                JSONObject data=array.getJSONObject(i);

                                Bengkel bengkel=new Bengkel();
                                bengkel.setNamaBengkel(data.getString("bk_namabengkel"));
                                bengkel.setRating(data.getString("total_rating"));

                                bengkel.setUrlImage(getString(R.string.base_url)+"asset/images/"+fungsi.isImgBengkelNull(data.getString("bk_foto")));


                                bengkel.setApproved(data.getInt("bk_approved"));
                                bengkel.setKategori(data.getInt("bk_kategori"));
                                bengkel.setIdbengkel(data.getInt("bk_id"));
                                bengkel.setUlasan(data.getInt("j_ulasan"));
                                bengkels.add(bengkel);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("eror", error.toString());
            }
        });


        requestQueue.add(getBengkel);

    }

}
