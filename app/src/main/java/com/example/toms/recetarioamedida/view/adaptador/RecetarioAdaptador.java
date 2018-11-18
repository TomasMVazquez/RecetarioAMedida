package com.example.toms.recetarioamedida.view.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.controller.SwipeAndDragHelper;
import com.example.toms.recetarioamedida.model.Receta;


import java.util.ArrayList;
import java.util.List;

public class RecetarioAdaptador extends RecyclerView.Adapter implements
        SwipeAndDragHelper.ActionCompletionContract{

    //Atributos
    private List<Receta> recetasList;
    protected AdaptadorInterface escuchador;
    private ItemTouchHelper touchHelper;


    //Constructor

    public RecetarioAdaptador(List<Receta> recetasList, AdaptadorInterface escuchador) {
        this.recetasList = recetasList;
        this.escuchador = escuchador;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        //Buscamos contexto
        Context context = parent.getContext();

        //pasamos contexto al inflador
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflamos view
        View view = inflater.inflate(R.layout.tarjeta_receta,parent,false);

        //pasamos hoder
        RecetaViewHolder recetaViewHolder = new RecetaViewHolder(view);

        return recetaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        //Buscamos datos
        Receta receta = recetasList.get(position);

        //Casteamos
        final RecetaViewHolder recetaViewHolder = (RecetaViewHolder) viewHolder;

        //Cargamos dato
        recetaViewHolder.cargar(receta);

    }

    @Override
    public int getItemCount() {
        return recetasList.size();
    }

    //Metodos sobreescritos de la interface implementada
    //Cambiar posicion de la celda cuando la muevo
    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Receta targetReceta = recetasList.get(oldPosition);
        Receta receta = new Receta(targetReceta);
        recetasList.remove(oldPosition);
        recetasList.add(newPosition,receta);
        notifyItemMoved(oldPosition,newPosition);
    }

    //Eliminar cuando hagamos swipe
    @Override
    public void onViewSwiped(int position) {
        recetasList.remove(position);
        notifyItemRemoved(position);
    }

    //Creamos metodo para asignar el Helper
    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }

    //Interface pra que nos escuche
    public interface AdaptadorInterface{
        void irDetalle(Receta receta, Integer position);
    }

    //Creamos View Holder
    public class RecetaViewHolder extends RecyclerView.ViewHolder{

        //Atributos
        private TextView titulo;
        private ImageView imagen;
        private TextView procedimiento;

        //constructor
        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.recetaImagen);
            titulo = itemView.findViewById(R.id.recetaTitulo);
            procedimiento = itemView.findViewById(R.id.recetaProcedimiento);

            //poner listener al item para ir al detalle
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Receta receta = recetasList.get(getAdapterPosition());
                    Integer position = recetasList.indexOf(receta);
                    escuchador.irDetalle(receta, position);
                }
            });

        }

        //metodo del view holder para cargar datos
        public void cargar(Receta receta){
            imagen.setImageResource(R.drawable.recetas_logo);
            titulo.setText(receta.getTitulo());
            procedimiento.setText(receta.getProcedimiento());
        }

    }

}
