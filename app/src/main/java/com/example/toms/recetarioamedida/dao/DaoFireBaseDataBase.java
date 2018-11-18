package com.example.toms.recetarioamedida.dao;

import android.support.annotation.NonNull;

import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DaoFireBaseDataBase {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Receta> recetaList;

    public void dameRecetas(ResultListener<List<Receta>> listenerDelController){
        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    Receta receta = childSnapShot.getValue(Receta.class);
                    recetaList.add(receta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listenerDelController.finish(recetaList);

    }

}
