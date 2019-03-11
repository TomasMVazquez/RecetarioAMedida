package com.example.toms.recetarioamedida.controller;

import android.content.Context;

import com.example.toms.recetarioamedida.dao.DaoFireBaseDataBase;
import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class ControllerFireBaseDataBase {

    private List<Receta> recetaList = new ArrayList<>();

    public void entrgarTodasRecetas(final ResultListener<List<Receta>> listResultListener){
        DaoFireBaseDataBase daoFireBaseDataBase = new DaoFireBaseDataBase();
        daoFireBaseDataBase.dameTodasRecetas(new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                listResultListener.finish(results);
            }
        });
    }

    public void entrgarMisRecetas(Context context, final ResultListener<List<Receta>> listResultListener){
        DaoFireBaseDataBase daoFireBaseDataBase = new DaoFireBaseDataBase();
        daoFireBaseDataBase.dameMisRecetas(context,new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                listResultListener.finish(results);
            }
        });
    }

}
