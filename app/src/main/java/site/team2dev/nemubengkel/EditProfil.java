package site.team2dev.nemubengkel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
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
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;


public class EditProfil extends AppCompatActivity {

    UserSessionManager session;
    Fungsi fungsi=new Fungsi();
    private RequestQueue requestQueue;

    private final int CODE_IMG_GALLERY=1;
    private final String SAMPLE_CROPPED_IMG_NAME="SampleCropImage";

    private ImageView imageView;
    private TextView email, save;
    private EditText nama, job, hp;
    private Spinner jk;
    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

//        set Tollbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        set Item spinner
        String[] arraySpinner = new String[] {"Choose . . .",
                "Male", "Female"
        };
        jk=(Spinner)findViewById(R.id.et_jk);
        ArrayAdapter<String> jenisKelamin=new ArrayAdapter<String>(EditProfil.this,R.layout.list_spinner, arraySpinner);
        jenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jk.setAdapter(jenisKelamin);

//        get Username from session
        session=new UserSessionManager(getApplicationContext());

//        volley
        requestQueue= Volley.newRequestQueue(this);

//        compoonent
        imageView=(ImageView)findViewById(R.id.photo);
        email=(TextView)findViewById(R.id.et_email);
        nama=(EditText)findViewById(R.id.et_nama);
        job=(EditText)findViewById(R.id.et_job);
        save=(TextView)findViewById(R.id.toolbar_save);
        hp=(EditText)findViewById(R.id.et_hp);



        getDataProfil();


    }



//    cancel button
    public void goBack(View view) {
        finish();
    }

//    get data user from database
    public void getDataProfil(){
        final String URL=getString(R.string.base_url)+"api/general/user?token=1234567&email="+fungsi.getUsername( session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray  array = response.getJSONArray("data");
                    JSONObject data=array.getJSONObject(0);
                    Glide
                            .with(getApplicationContext())
                            .load(getString(R.string.base_url)+"asset/images/"+fungsi.isImgProfilNull(data.getString("us_profil")))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                    email.setText(data.getString("us_email"));
                    nama.setText(fungsi.isNull(data.getString("us_nama")));
                    jk.setSelection(fungsi.setJk(data.getString("us_jk")), true);
                    job.setText(fungsi.isNull(data.getString("us_pekerjaan")));
                    hp.setText((fungsi.isNull(data.getString("us_nomorhp"))));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(getApplicationContext(), "Check your Connectiton !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


//    upload image
    public void uploadImage(View view) {
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
                imageView.setImageURI(imageUri);
            }
        }else if (requestCode== UCrop.REQUEST_CROP && resultCode==RESULT_OK){
             Uri imageUriResultCrop= UCrop.getOutput(data);
             if(imageUriResultCrop!=null){
                imageView.setImageURI(imageUriResultCrop);
             }
        }

    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName+=".png";

        UCrop uCrop=UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),destinationFileName)));

        uCrop.withAspectRatio(1,1);
//        uCrop.withAspectRatio(3,4);
//        uCrop.useSourceImageAspectRatio();
//        uCrop.withAspectRatio(2,3);
//        uCrop.withAspectRatio(16,9);

        uCrop.withMaxResultSize(450,450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(EditProfil.this);
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




    //    save Image
    public void simpanEdit(View view) {
        save.setTextColor(getResources().getColor(R.color.text1));
        save.setClickable(false);
        bitmap=fungsi.imageView2Bitmap(imageView);
        final String URL=getString(R.string.base_url)+"api/editprofil";
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("respon", response);
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
                save.setTextColor(getResources().getColor(R.color.colorPrimary));
                save.setClickable(true);
                Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                hashMap.put("profil", fungsi.imageToSting(bitmap));
                hashMap.put("nama",nama.getText().toString());
                hashMap.put("jk",fungsi.isJKNull(jk.getSelectedItem().toString()));
                hashMap.put("hp",hp.getText().toString());
                hashMap.put("pekerjaan", job.getText().toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);



    }


    public void sampohAkun(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAkun();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    private void deleteAkun(){

        final String URL=getString(R.string.base_url)+"api/editprofil/delete";
        StringRequest delete=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                finish();
                session.logoutUser();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                return hashMap;
            }
        };
        requestQueue.add(delete);

    }


    public void changePass(View view) {
        Intent intent=new Intent(this, ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
