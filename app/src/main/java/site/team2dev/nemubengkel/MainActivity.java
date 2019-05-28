package site.team2dev.nemubengkel;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener{

    UserSessionManager session;
    String menuAktif;

    final RxPermissions rxPermissions = new RxPermissions(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if(!granted){
                        finish();
                    }else{
                        setContentView(R.layout.activity_main);



                        BottomNavigationView navigation = findViewById(R.id.navigation);
                        navigation.setOnNavigationItemSelectedListener(this);

                        session = new UserSessionManager(getApplicationContext());


//        ambil data dari aktivity sebelumnya
                        if (savedInstanceState == null) {
                            Bundle extras = getIntent().getExtras();
                            if (extras == null) {
                                menuAktif = null;
                            } else {
                                menuAktif = extras.getString("keys");
                            }
                        } else {
                            menuAktif = (String) savedInstanceState.getSerializable("keys");
                        }

//        menu aktif
                        if (menuAktif != null) {
                            navigation.getMenu().findItem(R.id.navigation_profil).setChecked(true);
                            loadFragment(new ProfilFragment());
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("params", "");
                            Fragment fragment=new HomeFragment();
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        }

                    }

                });

    }


    private boolean loadFragment(Fragment fragment ){
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

        Bundle bundle = new Bundle();

        Fragment fragment=null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                bundle.putString("params", "");
                fragment=new HomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.navigation_mobil:
                bundle.putString("params", "&kategori=2");
                fragment=new HomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.navigation_motor:
                bundle.putString("params", "&kategori=1");
                fragment=new HomeFragment();
                fragment.setArguments(bundle);
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
                Intent intent=new Intent(MainActivity.this, EditProfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.addbengkel:
                Intent i=new Intent(MainActivity.this, AddBengkel.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;

            case R.id.logout:
                logout();
                return true;

            default:
                return false;
        }
    }

    public void logout(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        session.logoutUser();
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


}
