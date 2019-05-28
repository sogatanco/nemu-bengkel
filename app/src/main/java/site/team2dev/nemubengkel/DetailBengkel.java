package site.team2dev.nemubengkel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBengkel extends AppCompatActivity {
    private UserSessionManager session;
    private RequestQueue requestQueue;
    Fungsi fungsi=new Fungsi();
    private ImageView imageView;
    private TextView pemilik, startdate, ulasan, description, waktu;
    private RatingBar rating, addrating;
    ArrayList img;
    ArrayList layanan;
    ImageView gambar1, gambar2, gambar3, gambar4, gambar5, gambar6;
    ImageView[] imageViews;
    EditText isiulasan;

    public List<Ulasan> ulasans;
    public RecyclerView.Adapter listAdapter;
    public RecyclerView recyclerView;

    double lat;
    double longi;
    String idbengkel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:082285658594"));
                if (ActivityCompat.checkSelfPermission(DetailBengkel.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        requestQueue= Volley.newRequestQueue(this);
        session=new UserSessionManager(getApplicationContext());

        img=new ArrayList<Integer>();

        imageView=(ImageView)findViewById(R.id.iv_your_image);
        pemilik=(TextView)findViewById(R.id.pemilik);
        startdate=(TextView)findViewById(R.id.startdate);
        rating=(RatingBar)findViewById(R.id.rating);
        ulasan=(TextView)findViewById(R.id.ulasan);
        description=(TextView)findViewById(R.id.isides);
        waktu=(TextView)findViewById(R.id.waktu);

        addrating=(RatingBar)findViewById(R.id.addrating);
        isiulasan=(EditText)findViewById(R.id.komentar);


        gambar1=(ImageView)findViewById(R.id.gambar1);
        gambar2=(ImageView)findViewById(R.id.gambar2);
        gambar3=(ImageView)findViewById(R.id.gambar3);
        gambar4=(ImageView)findViewById(R.id.gambar4);
        gambar5=(ImageView)findViewById(R.id.gambar5);
        gambar6=(ImageView)findViewById(R.id.gambar6);
        imageViews= new ImageView[]{gambar1, gambar2, gambar3, gambar4, gambar5, gambar6};



        Intent i=getIntent();
        idbengkel=i.getStringExtra("id");

//        Toast.makeText(this, i.getStringExtra("id"), Toast.LENGTH_LONG).show();

        this.setTitle(i.getStringExtra("namaBengkel"));
        Glide
                .with(this)
                .load(i.getStringExtra("urlImage"))
                .centerCrop()
                .into(imageView);

        getDetailBengkel(idbengkel);


        recyclerView=(RecyclerView)findViewById(R.id.listulasan);
        ulasans=new ArrayList<>();
        getDataUlasan(idbengkel);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        listAdapter=new UlasanAdapter(ulasans,this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getDetailBengkel(String id){
        final String URL=getString(R.string.base_url)+"api/general/bengkel?token=1234567&id="+id;

        JsonObjectRequest detail=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    JSONObject data=array.getJSONObject(0);
                   float jrating;
                    if(data.getString("total_rating").equals("null")){
                        jrating=0;
                    }else{
                        jrating=Float.valueOf(data.getString("total_rating"));
                    }
                    lat=data.getDouble("bk_lat");
                    longi=data.getDouble("bk_long");
                    pemilik.setText(data.getString("bk_pemilik"));
                    startdate.setText(data.getString("bk_startdate"));
                    rating.setRating((float)jrating/data.getInt("j_ulasan"));
                    addrating.setRating((float)jrating/data.getInt("j_ulasan"));
                    ulasan.setText(data.getString("j_ulasan")+" ulasan");
                    description.setText(data.getString("bk_deskripsi"));
                    layanan=new ArrayList<String>(Arrays.asList(data.getString("bk_layanan").substring(1, data.getString("bk_layanan").length()-1).split(", ")));
                    addImg(layanan);
                    waktu.setText(data.getString("bk_availabletime"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(detail);
    }

    private void addImg(ArrayList<String> layanans){
        img=new ArrayList<Integer>();
        if(layanans.contains("tempel ban")){
            img.add(R.drawable.tambal_ban);
        }
        if(layanans.contains("jual sparepart")){
            img.add(R.drawable.sparepart);
        }
        if(layanans.contains("service mesin")){
            img.add(R.drawable.service_mesin);
        }
        if(layanans.contains("ganti oli")){
            img.add(R.drawable.gantioli);
        }
        if(layanans.contains("service radiator")){
            img.add(R.drawable.radiator);
        }
        if(layanans.contains("modifikasi")){
            img.add(R.drawable.modifikasi);
        }

        for (int i=0; i<img.size(); i++){
            imageViews[i].setImageResource((Integer) img.get(i));
        }
    }


    public void penunjuk(View view) {
        Intent intent = new Intent(this,Routes.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lat",lat);
        intent.putExtra("long",longi);
        this.startActivity(intent);
    }

    public void submitcoment(View view) {
        if(session.isUserLoggedIn()){
            if(!fungsi.isEmpty(isiulasan)){
                final String URL=getString(R.string.base_url)+"api/ulasan";
                StringRequest simpanulasan=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                        isiulasan.setText("");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap=new HashMap<String, String>();
                        hashMap.put("idbengkel", idbengkel);
                        hashMap.put("ulasan",isiulasan.getText().toString());
                        hashMap.put("rating", String.valueOf(addrating.getRating()));
                        hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                        return hashMap;
                    }
                };
                requestQueue.add(simpanulasan);
            }
            else{
                Toast.makeText(this, "Ulasan can't be empty !", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "Failure, you need to login !", Toast.LENGTH_LONG).show();
        }

    }

    public void getDataUlasan(String id){
        final String URL=getString(R.string.base_url)+"api/general/ulasan?token=1234567&idbengkel="+id;
        JsonObjectRequest dataulasan=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    for (int i=0;i<array.length();i++) {
                        JSONObject data = array.getJSONObject(i);
                        Ulasan ulasan=new Ulasan();
                        ulasan.setUser(data.getString("ul_user"));
                        ulasan.setDate(data.getString("ul_time"));
                        ulasan.setRating(data.getString("ul_rating"));
                        ulasan.setUlasan(data.getString("ul_ulasan"));
                        ulasans.add(ulasan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(dataulasan);
    }
}
