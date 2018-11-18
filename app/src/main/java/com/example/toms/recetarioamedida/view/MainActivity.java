package com.example.toms.recetarioamedida.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.view.fragment.AgregarRecetaFragment;
import com.example.toms.recetarioamedida.view.fragment.LogInFragment;
import com.example.toms.recetarioamedida.view.fragment.RecetasFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {

    private TextView frameText;
    private ImageView imageView;
    private CallbackManager callbackManager;

    private LogInFragment logInFragment = new LogInFragment();
    private RecetasFragment recetasFragment = new RecetasFragment();

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onResume() {
        super.onResume();

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

                    }

                }

            }, 3000);

            updateUI(currentUser);

        }else {
            cargarFragment(logInFragment);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gerente
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        frameText = findViewById(R.id.frameText);

        //NavigationView
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){

                    case R.id.login:
                        if (currentUser != null){
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            navigationView.getMenu().findItem(R.id.login).setTitle(R.string.login_iniciar);
                            currentUser = null;
                        }else{
                            cargarFragment(logInFragment);
                        }
                        return true;

                    case R.id.recetas:
                        //cargar fragments
                        cargarFragment(recetasFragment);
                        return true;

                    case R.id.aboutUs:
                        //cargar fragments
                        return true;
                }

                return false;
            }
        });


    }


    //Metodos

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
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
                            Toast.makeText(MainActivity.this, user.getDisplayName()+" OnResume", Toast.LENGTH_SHORT).show();
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
            //String name = user.getDisplayName() + " " + R.string.login_salir;
            Uri uri = user.getPhotoUrl();
            //Glide.with(this).load(uri).into(imageView);
            frameText.setText(user.getDisplayName());
            navigationView.getMenu().findItem(R.id.login).setTitle(R.string.login_salir);

        }else {
            //cargarFragment(logInFragment);
        }
    }



}
