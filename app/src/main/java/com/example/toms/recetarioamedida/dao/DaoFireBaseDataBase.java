package com.example.toms.recetarioamedida.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.model.RecetaContenedor;
import com.example.toms.recetarioamedida.utils.ResultListener;
import com.example.toms.recetarioamedida.view.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaoFireBaseDataBase {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Receta> recetaList = new ArrayList<>();
    private RecetaContenedor recetaContenedor;

    public void dameTodasRecetas(final ResultListener<List<Receta>> listResultListener){
        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();

        mReference.child("recetaList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    Receta addReceta = childSnapShot.getValue(Receta.class);
                    recetaList.add(addReceta);
                }
                listResultListener.finish(recetaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void dameMisRecetas(Context context,final ResultListener<List<Receta>> listResultListener){
        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();
        String miDataBase = MainActivity.dataBaseID(context);

        mReference.child(miDataBase).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    Receta addReceta = childSnapShot.getValue(Receta.class);
                    recetaList.add(addReceta);
                }
                listResultListener.finish(recetaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
