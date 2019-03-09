package com.example.toms.recetarioamedida.view.adaptador;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.utils.SwipeAndDragHelper;
import com.example.toms.recetarioamedida.model.Receta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

public class RecetarioAdaptador extends RecyclerView.Adapter implements
        SwipeAndDragHelper.ActionCompletionContract{

    //Atributos
    private List<Receta> recetasList;
    protected AdaptadorInterface escuchador;
    private ItemTouchHelper touchHelper;
    private FirebaseStorage mStorage;
    private Context context;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private Integer i;

    //Constructor
    public RecetarioAdaptador(List<Receta> recetasList, AdaptadorInterface escuchador) {
        this.recetasList = recetasList;
        this.escuchador = escuchador;
    }

    public void setRecetasList(List<Receta> recetasList) {
        this.recetasList = recetasList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        //Buscamos contexto
        context = parent.getContext();

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
        recetaViewHolder.cargar(context,receta);

    }

    @Override
    public int getItemCount() {
        return recetasList.size();
    }

    //Metodos sobreescritos de la interface implementada
    //Cambiar posicion de la celda cuando la muevo
    @Override
    public void onViewMoved(int oldPosition, final int newPosition) {
        Receta targetReceta = recetasList.get(oldPosition);
        String idMoved = recetasList.get(oldPosition).getId();
        String idToMoved = recetasList.get(newPosition).getId();
        Receta receta = new Receta(targetReceta);
        recetasList.remove(oldPosition);
        recetasList.add(newPosition,receta);
        notifyItemMoved(oldPosition,newPosition);
    }

    //Eliminar cuando hagamos swipe
    @Override
    public void onViewSwiped(int position) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference();
        String id = recetasList.get(position).getId();
        mReference.child("recetaList").child(id).removeValue();
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
        public void cargar(final Context context, Receta receta){
            //Gerente
            mStorage = FirebaseStorage.getInstance();

            //Raiz del Storage
            StorageReference raiz = mStorage.getReference();

            if (receta.getImagen()!=null) {
                StorageReference imagenReference = raiz.child(context.getResources().getString(R.string.ruta_imagenes)).child(receta.getImagen());
                imagenReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(imagen);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }else {
                Glide.with(context).load(R.drawable.recetas_logo).into(imagen);
            }
            //imagen.setImageResource(R.drawable.recetas_logo);
            titulo.setText(receta.getTitulo());
            procedimiento.setText(receta.getProcedimiento());
        }

    }

    //--------Metodo para eliminar recetas con el Swipe----------------------------------------//
    public void eliminarReceta (int posicion){
//        mDatabase = FirebaseDatabase.getInstance();
//        mReference = mDatabase.getReference();
//        final DatabaseReference deviceDb = mReference.child(MainActivity.showId()).child(context.getResources().getString(R.string.device_reference_child)).child(deviceList.get(posicion).getId());
//        deviceDb.removeValue();
        recetasList.remove(posicion);
        notifyItemRemoved(posicion);
    }

    //--------Metodo para volver a agregar la receta que se elimino------------------------------//
    public void noRemoverReceta (Receta receta, int posicion){
        recetasList.add(posicion,receta);
        notifyItemInserted(posicion);
    }

}
