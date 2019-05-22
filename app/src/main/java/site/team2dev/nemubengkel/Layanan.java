package site.team2dev.nemubengkel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Layanan extends AppCompatActivity {
//    "tempel_ban", "jual_sparepart", "service_mesin", "ganti_oli","service_radiator","modifikasi"}
    CheckBox tempel_ban, jual_sparepart, service_mesin, ganti_oli, service_radiator, modifikasi;
    ArrayList<String> layanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);

        tempel_ban=(CheckBox)findViewById(R.id.tempel_ban);
        jual_sparepart=(CheckBox)findViewById(R.id.jual_sparepart);
        service_mesin=(CheckBox)findViewById(R.id.service_mesin);
        ganti_oli=(CheckBox)findViewById(R.id.ganti_oli);
        service_radiator=(CheckBox)findViewById(R.id.service_radiator);
        modifikasi=(CheckBox)findViewById(R.id.modifikasi);


        Intent intent=getIntent();
        setCheckedLayanan( intent.getStringArrayListExtra("layanan"));


    }

    private void setCheckedLayanan(ArrayList<String> layanans){
       if(layanans.contains("tempel ban")){
           tempel_ban.setChecked(true);
       }
        if(layanans.contains("jual sparepart")){
            jual_sparepart.setChecked(true);
        }
        if(layanans.contains("service mesin")){
            service_mesin.setChecked(true);
        }
        if(layanans.contains("ganti oli")){
            ganti_oli.setChecked(true);
        }
        if(layanans.contains("service radiator")){
            service_radiator.setChecked(true);
        }
        if(layanans.contains("modifikasi")){
            modifikasi.setChecked(true);
        }
    }

    private void getChecked(){
        layanan = new ArrayList<String>();
        if(tempel_ban.isChecked()){
            layanan.add("tempel ban");
        }
        if(jual_sparepart.isChecked()){
            layanan.add("jual sparepart");
        }
        if(service_mesin.isChecked()){
            layanan.add("service mesin");
        }
        if(ganti_oli.isChecked()){
            layanan.add("ganti oli");
        }
        if(service_radiator.isChecked()){
            layanan.add("service radiator");
        }
        if(modifikasi.isChecked()){
            layanan.add("modifikasi");
        }
    }

    public void batal(View view) {
        finish();
    }

    public void simpan(View view) {
        getChecked();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("layanans", layanan);
        setResult(AddBengkel.RESULT_OK,returnIntent);
        finish();
    }
}
