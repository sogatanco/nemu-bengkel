package site.team2dev.nemubengkel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Descripsi extends AppCompatActivity {
    private EditText descripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripsi);

        descripsi=(EditText)findViewById(R.id.description);
    }
    public void simpan(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("description",descripsi.getText().toString());
        setResult(AddBengkel.RESULT_OK,returnIntent);
        finish();
    }

    public void batal(View view) {
        finish();
    }
}

