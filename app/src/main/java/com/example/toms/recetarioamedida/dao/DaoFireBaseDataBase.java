package com.example.toms.recetarioamedida.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.model.RecetaContenedor;
import com.example.toms.recetarioamedida.utils.ResultListener;
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

    public void dameRecetas(final ResultListener<List<Receta>> listenerDelController){
        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();

        mReference.child("recetaList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Receta receta = dataSnapshot.getValue(Receta.class);
                recetaList.add(receta);
                listenerDelController.finish(recetaList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Receta receta = dataSnapshot.getValue(Receta.class);
                recetaList.remove(receta);
                listenerDelController.finish(recetaList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
