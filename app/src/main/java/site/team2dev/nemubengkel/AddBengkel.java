package site.team2dev.nemubengkel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class AddBengkel extends AppCompatActivity {
    private ImageView photo;
    private final int CODE_IMG_GALLERY=1;
    private final String SAMPLE_CROPPED_IMG_NAME="SampleCropImage";
    private Spinner kategori;
    private String description="";
    private double latitude=0;
    private double longitude=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bengkel);

        photo=(ImageView)findViewById(R.id.photo);
        Glide
                .with(getApplicationContext())
                .load(getString(R.string.base_url)+"asset/images/bengkel_null.png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);

        kategori=(Spinner)findViewById(R.id.et_kategori);
        String[] arraySpinner = new String[] {"Mobil", "Sepeda Motor"};
        ArrayAdapter<String> Mkategori=new ArrayAdapter<String>(AddBengkel.this,R.layout.list_spinner, arraySpinner);
        Mkategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(Mkategori);
    }

    public void goBack(View view) {
        finish();
    }

    public void addBengkel(View view) {
//        TODO....
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

//               Toast.makeText(this, ""+data.getDoubleExtra("lon", 0), Toast.LENGTH_LONG).show();
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
}
