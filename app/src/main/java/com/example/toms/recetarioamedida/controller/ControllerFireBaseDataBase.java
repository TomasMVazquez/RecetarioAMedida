package com.example.toms.recetarioamedida.controller;

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

    public void entregarListaRecetas(final ResultListener<List<Receta>> listResultListener){

        DaoFireBaseDataBase daoFireBaseDataBase = new DaoFireBaseDataBase();

        daoFireBaseDataBase.dameRecetas(new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                listResultListener.finish(results);
            }
        });

    }

}
