package site.team2dev.nemubengkel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ListBengkel extends AppCompatActivity {
    public List<Bengkel> bengkels;
    public RecyclerView.Adapter listAdapter;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bengkel);
    }
}
