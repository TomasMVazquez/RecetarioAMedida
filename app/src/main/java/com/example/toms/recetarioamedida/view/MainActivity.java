package com.example.toms.recetarioamedida.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.controller.ControllerFireBaseDataBase;
import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;
import com.example.toms.recetarioamedida.utils.Util;
import com.example.toms.recetarioamedida.view.adaptador.ViewPagerAdapter;
import com.example.toms.recetarioamedida.view.fragment.LogInFragment;
import com.example.toms.recetarioamedida.view.fragment.MisRecetasFragment;
import com.example.toms.recetarioamedida.view.fragment.RecetaDetalleFragment;
import com.example.toms.recetarioamedida.view.fragment.RecetasFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecetasFragment.OnFragmentRecetasNotify, MisRecetasFragment.OnFragmentMisRecetasNotify{

    public static final int KEY_LOGIN=101;
    public static final int KEY_PUBLICAS = 201;
    public static final int KEY_MIAS = 202;
    public static final String KEY_WHERE = "where";

    private TextView frameText;
    private ImageView imageView;
    private CallbackManager callbackManager;

    private LogInFragment logInFragment = new LogInFragment();
    private RecetasFragment recetasFragment = new RecetasFragment();
    private MisRecetasFragment misRecetasFragment = new MisRecetasFragment();

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private static FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ViewPagerAdapter adapter;

    private static String idDataBase;


    @Override
    protected void onStart() {
        super.onStart();
        //TODO HACER PUBLICAS RECETAS (AGREGAR NOMBRE USUARIO) Y AGREGAR FAVORITOS DE LAS RECETAS PUBLICAS
        mAuth = FirebaseAuth.getInstance();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Handler handler = new Handler();
        if (accessToken != null){
            handleFacebookAccessToken(accessToken);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    currentUser = mAuth.getCurrentUser();
                    if (currentUser != null){
                        //si salio y volvio pero esta logeado que cargar
                        eliminarFragment(logInFragment);
                        cargarFragment(misRecetasFragment);
                    }else {
                        cargarFragment(recetasFragment);
                    }
                }
            }, 3000);
            updateUI(currentUser);
        }else{
            //cargarFragment(logInFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.printHash(this);

        //Gerente
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        frameText = findViewById(R.id.frameText);

        //Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //NavigationView
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);

        //Btn Hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()){
                    case R.id.login:
                        if (currentUser != null){
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            navigationView.getMenu().findItem(R.id.login).setTitle(R.string.login_iniciar);
                            frameText.setText("");
                            currentUser = null;
                        }else{
                            cargarFragment(logInFragment);
                        }
                        return true;

                    case R.id.recetas:
                        //cargar fragments
                        cargarFragment(recetasFragment);
                        return true;

                    case R.id.misRecetas:
                        if (currentUser!=null) {
                            cargarFragment(misRecetasFragment);
                        }else {
                            Toast.makeText(MainActivity.this, "Debes estar logeado para ingresar a tus recetas", Toast.LENGTH_SHORT).show();
                        }
                        return true;

//                    case R.id.favoritos:
//
//                        return true;

                    case R.id.aboutUs:
                        //cargar fragments
                        //TODO HAcer este fragment
                        Toast.makeText(MainActivity.this, "Seccion en construccion", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
            }
        });


    }


    //Metodos

    @Override
    public void onBackPressed() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawers();
        }else{
            if (mFragmentManager.getFragments().size() > 0){
                eliminarFragment(mFragmentManager.getFragments().get(mFragmentManager.getFragments().size()-1));
            }else {
                super.onBackPressed();
            }
        }
    }

    public void cargarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void eliminarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
//                            Toast.makeText(MainActivity.this, user.getDisplayName()+" OnResume", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser user){

        if (user != null) {
            cargarFragment(recetasFragment);
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String phone = user.getPhoneNumber();
//            Uri uri = user.getPhotoUrl();
//            String dataBaseName;
//
//            if (email != null) {
//                String mail = email.substring(0, email.indexOf("."));
//                dataBaseName = mail;
//            }else {
//                dataBaseName = phone;
//            }
            frameText.setText(user.getDisplayName());
            navigationView.getMenu().findItem(R.id.login).setTitle(R.string.login_salir);

        }else {
            //cargarFragment(logInFragment);
        }
    }

    //Confirmar si esta Logeado
    public static Boolean isLogon(Context context){
        if (currentUser!=null){
            return true;
        }else {
            return false;
        }
    }

    public static String dataBaseID(Context context) {
        if (isLogon(context)) {
            String email = currentUser.getEmail();
            String phone = currentUser.getPhoneNumber();
            String dataBaseName;

            if (email != null) {
                String mail = email.substring(0, email.indexOf("."));
                dataBaseName = mail;
            }else {
                dataBaseName = phone;
            }
            return dataBaseName;
        } else {
            return null;
        }
    }

    @Override
    public void irDetalleActividad(Receta receta, final Integer position) {
        final List<Receta> recetaList = new ArrayList<>();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        for (Fragment fragment:mFragmentManager.getFragments()) {
            eliminarFragment(fragment);
        }

        //Lista de fragments
        final List<Fragment> fragments = new ArrayList<>();
        //Adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),new ArrayList<Fragment>());
        //Controller
        ControllerFireBaseDataBase controllerFireBaseDataBase = new ControllerFireBaseDataBase();

        controllerFireBaseDataBase.entrgarTodasRecetas(new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                recetaList.addAll(results);
            }
        });
        //Progess dialog
        final ProgressDialog prog= new ProgressDialog(this);
        prog.setTitle("Por favor espere");
        prog.setMessage("Estamos cargando recetas");
        prog.setCancelable(false);
        prog.setIndeterminate(true);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (Integer i = 0; i<recetaList.size();i++){
                    fragments.add(RecetaDetalleFragment.giveReceta(MainActivity.this,position,recetaList.get(i),"Publicas"));
                }
                adapter.setFragmentList(fragments);
                //ViewPager
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);

                //Inicializado
                viewPager.setCurrentItem(position);
                prog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void irMiDetalleActividad(Receta receta, final Integer position) {
        final List<Receta> recetaList = new ArrayList<>();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        for (Fragment fragment:mFragmentManager.getFragments()) {
            eliminarFragment(fragment);
        }

        //Lista de fragments
        final List<Fragment> fragments = new ArrayList<>();
        //Adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),new ArrayList<Fragment>());
        //Controller
        ControllerFireBaseDataBase controllerFireBaseDataBase = new ControllerFireBaseDataBase();

        controllerFireBaseDataBase.entrgarMisRecetas(this,new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                recetaList.addAll(results);
            }
        });
        //Progess dialog
        final ProgressDialog prog= new ProgressDialog(this);
        prog.setTitle("Por favor espere");
        prog.setMessage("Estamos cargando sus recetas");
        prog.setCancelable(false);
        prog.setIndeterminate(true);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (Integer i = 0; i<recetaList.size();i++){
                    fragments.add(RecetaDetalleFragment.giveReceta(MainActivity.this,position,recetaList.get(i),"Mias"));
                }
                adapter.setFragmentList(fragments);
                //ViewPager
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);

                //Inicializado
                viewPager.setCurrentItem(position);
                prog.dismiss();
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case KEY_PUBLICAS:
                    cargarFragment(recetasFragment);
                    break;
                case KEY_MIAS:
                    cargarFragment(misRecetasFragment);
                    break;
            }

        }else {

        }
    }
}
