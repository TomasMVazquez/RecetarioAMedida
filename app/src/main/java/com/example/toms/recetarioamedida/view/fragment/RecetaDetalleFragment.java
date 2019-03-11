package com.example.toms.recetarioamedida.view.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecetaDetalleFragment extends Fragment {

    public static final String KEY_RECETA = "receta";
    public static final String KEY_POSITION = "position";

    private static Context context;
    private StorageReference raiz;
    private FirebaseStorage mStorage;
    private DatabaseReference mReference;
    private TextView nombreReceta;
    private TextView procedimientoReceta;
    private ImageView imagenReceta;
    private LinearLayout linearLayoutReceta;
    private static String deDondeVengo;

    public RecetaDetalleFragment() {
        // Required empty public constructor
    }

    //Constructor
    public static RecetaDetalleFragment giveReceta(Context contextView,int fragmentNumber, Receta receta, String fromWhere){
        deDondeVengo = fromWhere;
        context = contextView;
        RecetaDetalleFragment recetaDetalle = new RecetaDetalleFragment();
        Bundle args = new Bundle();
        args.putString(KEY_RECETA,receta.getId());
        recetaDetalle.setArguments(args);
        return recetaDetalle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receta_detalle, container, false);

        Bundle bundle = getArguments();
        final String idRec = bundle.getString(KEY_RECETA);

        final List<Receta> recetaList = new ArrayList<>();
        final List<Receta> miRecetaList = new ArrayList<>();

        //firebase
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        //Gerente
        mStorage = FirebaseStorage.getInstance();
        //Raiz del Storage
        raiz = mStorage.getReference();

        nombreReceta = view.findViewById(R.id.nombreReceta);
        procedimientoReceta = view.findViewById(R.id.procedimientoReceta);
        imagenReceta = view.findViewById(R.id.imagenReceta);
        linearLayoutReceta = view.findViewById(R.id.linearLayoutReceta);

        ControllerFireBaseDataBase controllerFireBaseDataBase = new ControllerFireBaseDataBase();

        if (deDondeVengo.equals("Mias")) {
            controllerFireBaseDataBase.entrgarMisRecetas(getContext(), new ResultListener<List<Receta>>() {
                @Override
                public void finish(List<Receta> results) {
                    miRecetaList.addAll(results);
                    mostrar(miRecetaList, idRec);
                }
            });
        }else {
            controllerFireBaseDataBase.entrgarTodasRecetas(new ResultListener<List<Receta>>() {
                @Override
                public void finish(List<Receta> results) {
                    recetaList.addAll(results);
                    mostrar(recetaList,idRec);
                }
            });
        }

        return view;
    }

    public void mostrar(List<Receta> recetaList, String idRec){
        for (Receta nvaReceta: recetaList) {
            if (nvaReceta.getId().equals(idRec)){
                if(nvaReceta.getImagen()!=null) {
                    StorageReference imagenReference = raiz.child(context.getResources().getString(R.string.ruta_imagenes)).child(nvaReceta.getImagen());
                    imagenReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(imagenReceta);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "ERROR IMAGEN", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 1000);

                nombreReceta.setText(nvaReceta.getTitulo());
                procedimientoReceta.setText(nvaReceta.getProcedimiento());

                for (int i = 0; i < nvaReceta.getIngredientes().size() ; i++) {
                    TextView ingredientesInflados = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.ingrediente,null);
                    ingredientesInflados.setText(nvaReceta.getIngredientes().get(i));
                    linearLayoutReceta.addView(ingredientesInflados);
                }
            }else {
                Toast.makeText(context, "Error al querer buscar una receta", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
