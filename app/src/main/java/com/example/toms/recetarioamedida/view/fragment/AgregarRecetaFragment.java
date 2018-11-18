package com.example.toms.recetarioamedida.view.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarRecetaFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;

    private EditText titulo;
    private List<String> ingredientes = new ArrayList<>();
    private TextView ingredientesAgregados;
    private EditText addIngredientes;
    private EditText procedimiento;
    private ImageView imagen;
    private FloatingActionButton agregarImagen;
    private FloatingActionButton agregarIngrediente;
    private FloatingActionButton agregarReceta;

    public AgregarRecetaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_agregar_receta, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();
        //Gerente
        mStorage = FirebaseStorage.getInstance();

        //Raiz del Storage
        StorageReference raiz = mStorage.getReference();


        imagen = view.findViewById(R.id.imagenAgregada);
        titulo = view.findViewById(R.id.addTitulo);
        ingredientesAgregados = view.findViewById(R.id.ingredientesAgregados);
        procedimiento = view.findViewById(R.id.addProcedimiento);
        addIngredientes = view.findViewById(R.id.addIngredientes);

        agregarImagen = view.findViewById(R.id.agregarImagen);
        agregarIngrediente = view.findViewById(R.id.agregarIngrediente);
        agregarReceta = view.findViewById(R.id.agregarReceta);

        agregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingred = addIngredientes.getText().toString();
                if (!addIngredientes.getText().toString().equals("")){
                    ingredientes.add(addIngredientes.getText().toString());
                    String ingrediente = ingredientesAgregados.getText().toString() + addIngredientes.getText().toString() + "; ";
                    ingredientesAgregados.setText(ingrediente);
                    addIngredientes.setText("");
                }else {
                    Toast.makeText(view.getContext(), "No hay ingredientes para agregar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        agregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(getActivity(),"Elegir",101);
            }
        });



        return view;
    }

    public void agregarRecetaDatabase(Receta receta){
        DatabaseReference id = mReference.child("recetaList").push();
        id.setValue(new Receta(id.getKey(),receta.getImagen(),receta.getTitulo(),receta.getIngredientes(),receta.getProcedimiento()));
    }


}
