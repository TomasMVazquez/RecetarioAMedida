package com.example.toms.recetarioamedida.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable  {

    //Atributos
    private String id;
    private String imagen;
    private String titulo;
    private List<String> ingredientes;
    private String procedimiento;
    private Integer position;
    private Boolean vegan;
    private Boolean vegetarian;
    private Boolean tacc;
    private Boolean mani;
    private Boolean publicRec;
    private Boolean favorite;

    //constructor
    public Receta() {
    }

    public Receta(String id, String imagen, String titulo, List<String> ingredientes, String procedimiento, Boolean vegan, Boolean tacc, Boolean mani, Boolean vegetarian, Boolean publicRec, Boolean favorite) {
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.procedimiento = procedimiento;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.tacc = tacc;
        this.mani = mani;
        this.publicRec = publicRec;
        this.favorite = favorite;
    }

    public Receta(Receta receta) {
        this.id = receta.id;
        this.imagen = receta.imagen;
        this.titulo = receta.titulo;
        this.ingredientes = receta.ingredientes;
        this.procedimiento = receta.procedimiento;
        this.vegan = receta.vegan;
        this.tacc = receta.tacc;
        this.mani = receta.mani;
        this.vegetarian = receta.vegetarian;
        this.publicRec = receta.publicRec;
        this.favorite = receta.favorite;
    }

    //toString

    @Override
    public String toString() {
        return "Receta{" +
                "id='" + id + '\'' +
                ", imagen='" + imagen + '\'' +
                ", titulo='" + titulo + '\'' +
                ", ingredientes=" + ingredientes +
                ", procedimiento='" + procedimiento + '\'' +
                '}';
    }


    //getter
    public Integer getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public String getImagen() {
        return imagen;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public Boolean getTacc() {
        return tacc;
    }

    public Boolean getMani() {
        return mani;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public Boolean getPublicRec() {
        return publicRec;
    }

    public Boolean getFavorite() {
        return favorite;
    }
}
