package site.team2dev.nemubengkel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class EditBengkel extends AppCompatActivity {
    private final int CODE_IMG_GALLERY=1;
    private final String SAMPLE_CROPPED_IMG_NAME="SampleCropImage";

    Fungsi fungsi=new Fungsi();
    private RequestQueue requestQueue;
    UserSessionManager session;

    int id;
    private String description="";
    ArrayList layanans=new ArrayList<String>();
    private String timestart;
    private String timefinish;
    private double latitude;
    private double longitude;


    EditText namaBengkel, nomorTelpon;
    ImageView photo;
    Spinner kategori;
    TextView tombolSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bengkel);

        session=new UserSessionManager(getApplicationContext());
        requestQueue= Volley.newRequestQueue(this);

        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
//        Toast.makeText(this, ""+id, Toast.LENGTH_LONG).show();


        namaBengkel=(EditText)findViewById(R.id.et_bengkel);
        nomorTelpon=(EditText)findViewById(R.id.et_tlpon);
        photo=(ImageView) findViewById(R.id.photo);
        tombolSave=(TextView)findViewById(R.id.toolbar_save);

        kategori=(Spinner)findViewById(R.id.et_kategori);
        String[] arraySpinner = new String[] { "Sepeda Motor", "Mobil"};
        ArrayAdapter<String> Mkategori=new ArrayAdapter<String>(EditBengkel.this,R.layout.list_spinner, arraySpinner);
        Mkategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(Mkategori);

        getDataBengkel();


    }

    public void goBack(View view) {
        finish();
    }

    public void getDataBengkel(){
        final String URL=getString(R.string.base_url)+"api/bengkel?token="+session.getUserDetails().get(UserSessionManager.KEY_TOKEN)+"&id="+String.valueOf(id);
        JsonObjectRequest getbengkel=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    JSONObject data=array.getJSONObject(0);
                    Log.d("respon",  data.getString("bk_id"));
                    Glide
                            .with(getApplicationContext())
                            .load(getString(R.string.base_url)+"asset/images/"+data.getString("bk_foto"))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(photo);
                    namaBengkel.setText(data.getString("bk_namabengkel"));
                    nomorTelpon.setText(data.getString("bk_telpon"));
                    kategori.setSelection(data.getInt("bk_kategori")-1, true);
                    description=data.getString("bk_deskripsi");
                    layanans=new ArrayList<String>(Arrays.asList(data.getString("bk_layanan").substring(1, data.getString("bk_layanan").length()-1).split(", ")));
                    String[] time=data.getString("bk_availabletime").split("-");
                    timestart=time[0].trim();
                    timefinish=time[1].trim();
                    latitude=data.getDouble("bk_lat");
                    longitude=data.getDouble("bk_long");

                    Log.d("r", time[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
            }
        });
        requestQueue.add(getbengkel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CODE_IMG_GALLERY && resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            if(imageUri!=null){
                startCrop(imageUri);
                photo.setImageURI(imageUri);
            }
        }
        if (requestCode== UCrop.REQUEST_CROP && resultCode==RESULT_OK){
            Uri imageUriResultCrop= UCrop.getOutput(data);
            if(imageUriResultCrop!=null){
                photo.setImageURI(imageUriResultCrop);
            }
        }
        if (requestCode == 3) {
            if(resultCode == EditBengkel.RESULT_OK){
                description=data.getStringExtra("description");
            }
        }

        if (requestCode == 5) {
            if(resultCode == EditBengkel.RESULT_OK){
                layanans.clear();
                layanans=data.getStringArrayListExtra("layanans");
            }
        }
        if (requestCode == 6) {
            if(resultCode == EditBengkel.RESULT_OK){
                timestart=data.getStringExtra("timestart");
                timefinish=data.getStringExtra("timefinish");
//                Toast.makeText(this, data.getStringExtra("timestart")+data.getStringExtra("timefinish"), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 4) {
            if(resultCode == EditBengkel.RESULT_OK){
                latitude=data.getDoubleExtra("lat",0);
                longitude=data.getDoubleExtra("lon",0);

            }
        }
    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName+=".png";

        UCrop uCrop=UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),destinationFileName)));

//        uCrop.withAspectRatio(1,1);
//        uCrop.withAspectRatio(3,4);
//        uCrop.useSourceImageAspectRatio();
//       uCrop.withAspectRatio(3,2);
        uCrop.withAspectRatio(18,9);

        uCrop.withMaxResultSize(900,450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(EditBengkel.this);
    }

    @SuppressLint("ResourceAsColor")
    private UCrop.Options getCropOptions() {
        UCrop.Options options=new UCrop.Options();

        options.setCompressionQuality(70);

//        compress
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

//        UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

//        color
        options.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));

        options.setToolbarTitle("Crop Image");

        return options;
    }

    public void addPhoto(View view) {
        startActivityForResult(new Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"),CODE_IMG_GALLERY);
    }

    public void toDeskripsi(View view) {
        Intent i = new Intent(this, Descripsi.class);
        i.putExtra("description", description);
        startActivityForResult(i, 3);
    }

    public void goToLayanan(View view) {
        Intent i = new Intent(this, Layanan.class);
        i.putExtra("layanan", layanans);
        startActivityForResult(i, 5);
    }

    public void goTime(View view) {
        Intent i = new Intent(this, AvailableTime.class);
        i.putExtra("timestart", timestart);
        i.putExtra("timefinish", timefinish);
        startActivityForResult(i, 6);
    }

    public void goToLokasi(View view) {
        Intent i = new Intent(this, Lokasi.class);
        i.putExtra("latitude",latitude);
        i.putExtra("longitude",longitude);
        startActivityForResult(i, 4);
    }


    public void editBengkel(View view) {
        tombolSave.setTextColor(getResources().getColor(R.color.text1));
        tombolSave.setClickable(false);
        Bitmap bitmap=fungsi.imageView2Bitmap(photo);

        final String URL=getString(R.string.base_url)+"api/bengkel";
        StringRequest saveEdit=new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tombolSave.setTextColor(getResources().getColor(R.color.colorPrimary));
                tombolSave.setClickable(true);
                Toast.makeText(getApplicationContext(),"Failure, check limit or connection", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                hashMap.put("nama", namaBengkel.getText().toString());
                hashMap.put("desc", description);
                hashMap.put("foto", fungsi.imageToSting(bitmap));
                hashMap.put("kategori", String.valueOf(kategori.getSelectedItemPosition()+1));
                hashMap.put("long", String.valueOf(longitude));
                hashMap.put("lat", String.valueOf(latitude));
                hashMap.put("layanan", layanans.toString());
                hashMap.put("time", timestart+"-"+timefinish);
                hashMap.put("telpon", nomorTelpon.getText().toString());
                hashMap.put("id", String.valueOf(id));
                return hashMap;
            }
        };
        requestQueue.add(saveEdit);
    }
}
