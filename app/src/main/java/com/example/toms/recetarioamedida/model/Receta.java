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

    //constructor
    public Receta() {
    }

    public Receta(String id, String imagen, String titulo, List<String> ingredientes, String procedimiento) {
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
}
