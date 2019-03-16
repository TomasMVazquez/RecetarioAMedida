package com.example.toms.recetarioamedida.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.model.Receta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;

public class NuevaRecetaActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;

    private LinearLayout linearLayout;
    private EditText titulo;
    private List<String> ingredientes = new ArrayList<>();
    private TextView ingredientesInflados;
    private EditText addIngredientes;
    private EditText procedimiento;
    private ImageView imagen;
    private FloatingActionButton agregarImagen;
    private FloatingActionButton agregarIngrediente;
    private FloatingActionButton agregarReceta;
    private String rutaImagen;
    private Integer position;
    private Switch switchVegano;
    private Switch switchTacc;
    private Switch switchMani;
    private Switch switchVegetarian;
    private int where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_receta);

        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();
        //Gerente
        mStorage = FirebaseStorage.getInstance();

        //Raiz del Storage
        StorageReference raiz = mStorage.getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        where = bundle.getInt(MainActivity.KEY_WHERE);

        linearLayout = findViewById(R.id.linearLayout);
        imagen = findViewById(R.id.imagenAgregada);
        titulo = findViewById(R.id.addTitulo);
        procedimiento = findViewById(R.id.addProcedimiento);
        addIngredientes = findViewById(R.id.addIngredientes);
        switchVegano = findViewById(R.id.switchVegano);
        switchTacc = findViewById(R.id.switchTacc);
        switchMani = findViewById(R.id.switchMani);
        switchVegetarian = findViewById(R.id.switchVegetarian);

        agregarImagen = findViewById(R.id.agregarImagen);
        agregarIngrediente = findViewById(R.id.agregarIngrediente);
        agregarReceta = findViewById(R.id.agregarReceta);

        agregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingred = addIngredientes.getText().toString();
                if (!addIngredientes.getText().toString().equals("")){
                    ingredientes.add(addIngredientes.getText().toString());

                    ingredientesInflados = (TextView) LayoutInflater.from(NuevaRecetaActivity.this).inflate(R.layout.ingrediente,null);
                    ingredientesInflados.setText(ingred);
                    linearLayout.addView(ingredientesInflados);

                    addIngredientes.setText("");

                }else {
                    Toast.makeText(NuevaRecetaActivity.this, "No hay ingredientes para agregar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchVegano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchVegetarian.setChecked(true);
                }
            }
        });

        agregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(NuevaRecetaActivity.this,"Elegir",101);
            }
        });

        agregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addIngredientes.getText().toString().equals("")){
                    ingredientes.add(addIngredientes.getText().toString());
                }

                if (titulo.equals("")){
                    Toast.makeText(NuevaRecetaActivity.this, "Debes ponerle un titulo a la receta", Toast.LENGTH_SHORT).show();
                }else {
                    Receta nuevaReceta = new Receta("0", rutaImagen, titulo.getText().toString(), ingredientes, procedimiento.getText().toString(), switchVegano.isChecked(), switchTacc.isChecked(), switchMani.isChecked(), switchVegetarian.isChecked(), false, false);
                    agregarRecetaDatabase(nuevaReceta);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    public void agregarRecetaDatabase(final Receta receta){
        DatabaseReference id;
        Boolean esPublica;
        if (where == MainActivity.KEY_MIAS){
            String miDataBase = MainActivity.dataBaseID(this);
            id = mReference.child(miDataBase).push();
            esPublica = false;
        }else {
            id = mReference.child("recetaList").push();
            esPublica = true;
        }

        id.setValue(new Receta(id.getKey(),receta.getImagen(),receta.getTitulo(),receta.getIngredientes(),receta.getProcedimiento(),receta.getVegan(),receta.getTacc(),receta.getMani(),switchVegetarian.isChecked(),esPublica,false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {

                //RAIZ REFERENCIA
                StorageReference raiz = mStorage.getReference();

                if (list.size() > 0) {
                    switch (i) {
                        case 101:
                            File file = list.get(0);
                            Uri uri = Uri.fromFile(file);
                            Glide.with(NuevaRecetaActivity.this).load(uri).into(imagen);

                            final Uri uriTemp = Uri.fromFile(new File(uri.getPath()));

                            //String exten = uriTemp.getLastPathSegment().substring(uriTemp.getLastPathSegment().indexOf("."));

                            StorageReference nuevaFoto = raiz.child(getResources().getString(R.string.ruta_imagenes)).child(uriTemp.getLastPathSegment());

                            UploadTask uploadTask = nuevaFoto.putFile(uriTemp);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NuevaRecetaActivity.this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    rutaImagen = uriTemp.getLastPathSegment();
                                }
                            });

                            break;

                        default:
                            Glide.with(NuevaRecetaActivity.this).load(R.drawable.recetas_logo).into(imagen);
                            break;
                    }
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {

            }
        });}
}
