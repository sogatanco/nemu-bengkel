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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class AddBengkel extends AppCompatActivity {
    private EditText nama, nmrtelpon;
    private TextView tombolSave;
    private ImageView photo;
    private final int CODE_IMG_GALLERY=1;
    private final String SAMPLE_CROPPED_IMG_NAME="SampleCropImage";
    private Spinner kategori;
    private String description="";
    private double latitude=0;
    private double longitude=0;
    private ArrayList<String> layanans;
    private String timestart="00:00";
    private String timefinish="00:00";

    Fungsi fungsi=new Fungsi();
    private RequestQueue requestQueue;
    UserSessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bengkel);

        session=new UserSessionManager(getApplicationContext());
        requestQueue= Volley.newRequestQueue(this);

        tombolSave=(TextView)findViewById(R.id.toolbar_save);
        nama=(EditText)findViewById(R.id.et_bengkel);
        nmrtelpon=(EditText)findViewById(R.id.et_tlpon);

        photo=(ImageView)findViewById(R.id.photo);
        Glide
                .with(getApplicationContext())
                .load(getString(R.string.base_url)+"asset/images/bengkel_null.png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);

        kategori=(Spinner)findViewById(R.id.et_kategori);
        String[] arraySpinner = new String[] { "Sepeda Motor", "Mobil"};
        ArrayAdapter<String> Mkategori=new ArrayAdapter<String>(AddBengkel.this,R.layout.list_spinner, arraySpinner);
        Mkategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(Mkategori);

        layanans=new ArrayList<String>();
//        layanans.add("tempel ban");
//        layanans.add("ganti oli");
//
//        Log.d("layanan", layanans.toString());

    }

    public void goBack(View view) {
        finish();
    }


//    start crop image
    public void addPhoto(View view) {
        startActivityForResult(new Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"),CODE_IMG_GALLERY);
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
            if(resultCode == AddBengkel.RESULT_OK){
                description=data.getStringExtra("description");
            }

        }

        if (requestCode == 4) {
            if(resultCode == AddBengkel.RESULT_OK){
                latitude=data.getDoubleExtra("lat",0);
                longitude=data.getDoubleExtra("lon",0);

            }
        }

        if (requestCode == 5) {
            if(resultCode == AddBengkel.RESULT_OK){
                layanans.clear();
                layanans=data.getStringArrayListExtra("layanans");
            }
        }

        if (requestCode == 6) {
            if(resultCode == AddBengkel.RESULT_OK){
                timestart=data.getStringExtra("timestart");
                timefinish=data.getStringExtra("timefinish");
//                Toast.makeText(this, data.getStringExtra("timestart")+data.getStringExtra("timefinish"), Toast.LENGTH_LONG).show();
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

        uCrop.start(AddBengkel.this);
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

    public void toDeskripsi(View view) {
        Intent i = new Intent(this, Descripsi.class);
        i.putExtra("description", description);
        startActivityForResult(i, 3);
    }


    public void goToLokasi(View view) {
        Intent i = new Intent(this, Lokasi.class);
        i.putExtra("latitude",latitude);
        i.putExtra("longitude",longitude);
        startActivityForResult(i, 4);
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


    public void addBengkel(View view) {
        tombolSave.setTextColor(getResources().getColor(R.color.text1));
        tombolSave.setClickable(false);
        Bitmap bitmap=fungsi.imageView2Bitmap(photo);

//String.valueOf(kategori.getSelectedItemPosition()+1)
//        layanans.toString()

//        Toast.makeText(this, String.valueOf(latitude), Toast.LENGTH_LONG).show();
        final String URL=getString(R.string.base_url)+"api/bengkel";
        StringRequest addBengkel=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                hashMap.put("nama", nama.getText().toString());
                hashMap.put("desc", description);
                hashMap.put("foto", fungsi.imageToSting(bitmap));
                hashMap.put("kategori", String.valueOf(kategori.getSelectedItemPosition()+1));
                hashMap.put("long", String.valueOf(longitude));
                hashMap.put("lat", String.valueOf(latitude));
                hashMap.put("layanan", layanans.toString());
                hashMap.put("time", timestart+"-"+timefinish);
                hashMap.put("telpon", nmrtelpon.getText().toString());
                return hashMap;
            }
        };
        requestQueue.add(addBengkel);
    }
}
