package com.example.toms.recetarioamedida.model;

import java.util.List;

public class RecetaContenedor {

    //Atributos
    private List<Receta> recetaList;

    //Constructor
    public RecetaContenedor(List<Receta> recetaList) {
        this.recetaList = recetaList;
    }

    //Getter

    public List<Receta> getRecetaList() {
        return recetaList;
    }

    //ToString

    @Override
    public String toString() {
        return "RecetaContenedor{" +
                "recetaList=" + recetaList +
                '}';
    }
}
