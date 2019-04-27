package site.team2dev.nemubengkel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    UserSessionManager session;
    String menuAktif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        session=new UserSessionManager(getApplicationContext());

//        ambil data dari aktivity sebelumnya
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                menuAktif= null;
            } else {
                menuAktif = extras.getString("keys");
            }
        } else {
            menuAktif= (String) savedInstanceState.getSerializable("keys");
        }

//        menu aktif
        if(menuAktif!=null){
            navigation.getMenu().findItem(R.id.navigation_profil).setChecked(true);
            loadFragment(new ProfilFragment());
        }else {
            loadFragment(new HomeFragment());
        }




    }


    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return  true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment=null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment=new HomeFragment();
                break;

            case R.id.navigation_mobil:
                fragment=new MobilFragment();
                break;

            case R.id.navigation_motor:
                fragment=new MotorFragment();
                break;

            case R.id.navigation_profil:
                if(this.session.isUserLoggedIn()){
                    fragment=new ProfilFragment();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

        }
        return loadFragment(fragment);
    }


//    Profil Fragment


    public void showMenu(View view) {
        PopupMenu popupMenu=new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.profil_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editprofil:
                Toast.makeText(this, "editprofil", Toast.LENGTH_LONG).show();
                return true;

            case R.id.addbengkel:
                Toast.makeText(this, "addbengkel", Toast.LENGTH_LONG).show();
                return true;

            case R.id.logout:
                session.logoutUser();
                return true;

            default:
                return false;
        }
    }
}
