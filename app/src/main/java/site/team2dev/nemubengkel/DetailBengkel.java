package site.team2dev.nemubengkel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DetailBengkel extends AppCompatActivity {
    private UserSessionManager session;
    private String token;
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
                finish();
            }
        });

        session=new UserSessionManager(getApplicationContext());
        if(this.session.isUserLoggedIn()){
            token=session.getUserDetails().get(UserSessionManager.KEY_TOKEN);
        }
        else {
            token="1234567";
        }
        Intent i=getIntent();
        this.setTitle(i.getStringExtra("namaBengkel"));

        ImageView imageView=(ImageView)findViewById(R.id.iv_your_image);
        Glide
                .with(this)
                .load(i.getStringExtra("urlImage"))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(imageView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_bengkel, menu);
        return true;
    }
}
