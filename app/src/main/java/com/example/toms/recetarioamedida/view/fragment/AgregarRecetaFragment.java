package com.example.toms.recetarioamedida.view.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.controller.ControllerFireBaseDataBase;
import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;
import com.example.toms.recetarioamedida.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

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

        linearLayout = view.findViewById(R.id.linearLayout);
        imagen = view.findViewById(R.id.imagenAgregada);
        titulo = view.findViewById(R.id.addTitulo);
        procedimiento = view.findViewById(R.id.addProcedimiento);
        addIngredientes = view.findViewById(R.id.addIngredientes);
        switchVegano = view.findViewById(R.id.switchVegano);
        switchTacc = view.findViewById(R.id.switchTacc);
        switchMani = view.findViewById(R.id.switchMani);
        switchVegetarian = view.findViewById(R.id.switchVegetarian);

        agregarImagen = view.findViewById(R.id.agregarImagen);
        agregarIngrediente = view.findViewById(R.id.agregarIngrediente);
        agregarReceta = view.findViewById(R.id.agregarReceta);

        agregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingred = addIngredientes.getText().toString();
                if (!addIngredientes.getText().toString().equals("")){
                    ingredientes.add(addIngredientes.getText().toString());

                    ingredientesInflados = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.ingrediente,null);
                    ingredientesInflados.setText(ingred);
                    linearLayout.addView(ingredientesInflados);

                    addIngredientes.setText("");

                }else {
                    Toast.makeText(view.getContext(), "No hay ingredientes para agregar", Toast.LENGTH_SHORT).show();
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
                EasyImage.openChooserWithGallery(AgregarRecetaFragment.this,"Elegir",101);
            }
        });

        agregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addIngredientes.getText().toString().equals("")){
                    ingredientes.add(addIngredientes.getText().toString());
                }

                Receta nuevaReceta = new Receta("0",rutaImagen,titulo.getText().toString(),ingredientes,procedimiento.getText().toString(),switchVegano.isChecked(),switchTacc.isChecked(),switchMani.isChecked(),switchVegetarian.isChecked());
                agregarRecetaDatabase(nuevaReceta);

                getActivity().getSupportFragmentManager().beginTransaction().remove(AgregarRecetaFragment.this).commit();

            }
        });


        return view;
    }

    public void agregarRecetaDatabase(final Receta receta){
        DatabaseReference id = mReference.child("recetaList").push();
        id.setValue(new Receta(id.getKey(),receta.getImagen(),receta.getTitulo(),receta.getIngredientes(),receta.getProcedimiento(),receta.getVegan(),receta.getTacc(),receta.getMani(),switchVegetarian.isChecked()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {

                //RAIZ REFERENCIA
                StorageReference raiz = mStorage.getReference();

                if (list.size()>0) {
                    switch (i) {
                        case 101:
                            File file = list.get(0);
                            Uri uri = Uri.fromFile(file);
                            Glide.with(getActivity()).load(uri).into(imagen);

                            final Uri uriTemp = Uri.fromFile(new File(uri.getPath()));

                            //String exten = uriTemp.getLastPathSegment().substring(uriTemp.getLastPathSegment().indexOf("."));
                            StorageReference nuevaFoto = raiz.child(getResources().getString(R.string.ruta_imagenes)).child(uriTemp.getLastPathSegment());

                            UploadTask uploadTask = nuevaFoto.putFile(uriTemp);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    rutaImagen = uriTemp.getLastPathSegment();
                                }
                            });

                            break;

                        default:
                            Glide.with(getActivity()).load(R.drawable.recetas_logo).into(imagen);
                            break;
                    }
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {

            }
        });
    }

}
