package com.example.toms.recetarioamedida.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Receta implements Serializable  {

    //Atributos
    private String id;
    private Integer imagen;
    private String titulo;
    private ArrayList<String> ingredientes;
    private String procedimiento;

    //constructor
    public Receta() {
    }

    public Receta(String id, Integer imagen, String titulo, ArrayList<String> ingredientes, String procedimiento) {
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.procedimiento = procedimiento;
    }

    public Receta(Receta receta) {
        this.id = receta.id;
        this.imagen = receta.imagen;
        this.titulo = receta.titulo;
        this.ingredientes = receta.ingredientes;
        this.procedimiento = receta.procedimiento;
    }

    //toString


    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", imagen=" + imagen +
                ", titulo='" + titulo + '\'' +
                ", ingredientes=" + ingredientes +
                ", procedimiento='" + procedimiento + '\'' +
                '}';
    }

    //getter

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public Integer getImagen() {
        return imagen;
    }
}
