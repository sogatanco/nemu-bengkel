package site.team2dev.nemubengkel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class ListBengkel extends AppCompatActivity {
    private RequestQueue requestQueue;
    public List<Bengkel> bengkels;
    public RecyclerView.Adapter listAdapter;
    public RecyclerView recyclerView;

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
    }
}
