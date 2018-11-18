package com.example.toms.recetarioamedida.controller;

import com.example.toms.recetarioamedida.dao.DaoFireBaseDataBase;
import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class ControllerFireBaseDataBase {

    private List<Receta> recetaList = new ArrayList<>();

    public void entregarListaRecetas(final ResultListener<List<Receta>> listResultListener){

        DaoFireBaseDataBase daoFireBaseDataBase = new DaoFireBaseDataBase();
        daoFireBaseDataBase.dameRecetas(new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                if (results!=null) {
                    listResultListener.finish(results);
                }else {
                    listResultListener.finish(recetaList);
                }
            }
        });

    }

}
