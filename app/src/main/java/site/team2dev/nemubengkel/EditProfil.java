package site.team2dev.nemubengkel;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class EditProfil extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String[] arraySpinner = new String[] {
                "Male", "Female"
        };
        Spinner jk=(Spinner)findViewById(R.id.et_jk);
        ArrayAdapter<String> jenisKelamin=new ArrayAdapter<String>(EditProfil.this,R.layout.list_spinner, arraySpinner);
        jenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jk.setAdapter(jenisKelamin);
    }
}
