package site.team2dev.nemubengkel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailBengkel extends AppCompatActivity {
    private UserSessionManager session;
    private RequestQueue requestQueue;
    private ImageView imageView;
    private TextView pemilik, startdate, ulasan, description, waktu;
    private RatingBar rating;
    ArrayList img;
    ArrayList layanan;
    ImageView gambar1, gambar2, gambar3, gambar4, gambar5, gambar6;
    ImageView[] imageViews;

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

        gambar1=(ImageView)findViewById(R.id.gambar1);
        gambar2=(ImageView)findViewById(R.id.gambar2);
        gambar3=(ImageView)findViewById(R.id.gambar3);
        gambar4=(ImageView)findViewById(R.id.gambar4);
        gambar5=(ImageView)findViewById(R.id.gambar5);
        gambar6=(ImageView)findViewById(R.id.gambar6);
        imageViews= new ImageView[]{gambar1, gambar2, gambar3, gambar4, gambar5, gambar6};



        Intent i=getIntent();


//        Toast.makeText(this, i.getStringExtra("id"), Toast.LENGTH_LONG).show();

        this.setTitle(i.getStringExtra("namaBengkel"));
        Glide
                .with(this)
                .load(i.getStringExtra("urlImage"))
                .centerCrop()
                .into(imageView);

        getDetailBengkel(i.getStringExtra("id"));

    }

    private void getDetailBengkel(String id){
        final String URL=getString(R.string.base_url)+"api/general/bengkel?token=1234567&id="+id;

        JsonObjectRequest detail=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    JSONObject data=array.getJSONObject(0);
                   int jrating;
                    if(data.getString("total_rating").equals("null")){
                        jrating=0;
                    }else{
                        jrating=Integer.parseInt(data.getString("total_rating"));
                    }
                    pemilik.setText(data.getString("bk_pemilik"));
                    startdate.setText(data.getString("bk_startdate"));
                    rating.setRating((float)jrating/data.getInt("j_ulasan"));
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


}
