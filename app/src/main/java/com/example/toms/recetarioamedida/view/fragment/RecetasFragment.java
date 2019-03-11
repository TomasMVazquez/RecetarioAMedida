package com.example.toms.recetarioamedida.view.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toms.recetarioamedida.R;
import com.example.toms.recetarioamedida.controller.ControllerFireBaseDataBase;
import com.example.toms.recetarioamedida.utils.SwipeAndDragHelper;
import com.example.toms.recetarioamedida.model.Receta;
import com.example.toms.recetarioamedida.utils.ResultListener;
import com.example.toms.recetarioamedida.view.MainActivity;
import com.example.toms.recetarioamedida.view.NuevaRecetaActivity;
import com.example.toms.recetarioamedida.view.adaptador.RecetarioAdaptador;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecetasFragment extends Fragment implements RecetarioAdaptador.AdaptadorInterface {

    private List<Receta> recetaList = new ArrayList<>();
    private RecetarioAdaptador recetarioAdaptador;

    public RecetasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);

        recetarioAdaptador = new RecetarioAdaptador(new ArrayList<Receta>(),this);

        //Datos
        ControllerFireBaseDataBase controllerFireBaseDataBase = new ControllerFireBaseDataBase();

        controllerFireBaseDataBase.entrgarTodasRecetas(new ResultListener<List<Receta>>() {
            @Override
            public void finish(List<Receta> results) {
                if (results.size()>0) {
                    recetarioAdaptador.setRecetasList(results);
                    recetaList.addAll(results);
                }
            }
        });

        //Recycler View
        RecyclerView recetasRecycler = view.findViewById(R.id.recyclerRecetas);
        recetasRecycler.setHasFixedSize(true);

        //LinearLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recetasRecycler.setLayoutManager(llm);

        //Adaptador
        recetasRecycler.setAdapter(recetarioAdaptador);

        //Swipe and Drag
        SwipeAndDragHelper swipeAndDragHelper =new SwipeAndDragHelper(new SwipeAndDragHelper.ActionCompletionContract() {
            @Override
            public void onViewMoved(int oldPosition, int newPosition) {

            }

            @Override
            public void onViewSwiped(final int position) {
                final Receta receta = recetaList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmación");
                builder.setMessage("Por favor confirmar que usted quiere quitar esta receta de su lista");
                builder.setCancelable(false);
                builder.setIcon(getActivity().getDrawable(R.drawable.recetas_logo));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recetarioAdaptador.eliminarReceta(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recetarioAdaptador.noRemoverReceta(receta,position);
                    }
                });

                builder.show();
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        recetarioAdaptador.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(recetasRecycler);

        //FabAgregarReceta
        //TODO Cambiar la forma que voy de fragment en fragment directo. Pasar por El Main con escuchadores
        FloatingActionButton fabAddReceta = view.findViewById(R.id.fabAddReceta);
        fabAddReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(RecetasFragment.this).commit();
                Intent intent = new Intent(getActivity(),NuevaRecetaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.KEY_WHERE,MainActivity.KEY_PUBLICAS);
                intent.putExtras(bundle);
                startActivityForResult(intent,MainActivity.KEY_PUBLICAS);
            }
        });


        return view;
    }

    @Override
    public void irDetalle(Receta receta, Integer position) {
        OnFragmentRecetasNotify onFragmentRecetasNotify = (OnFragmentRecetasNotify) getContext();
        onFragmentRecetasNotify.irDetalleActividad(receta, position);
    }

    public interface OnFragmentRecetasNotify{
        public void irDetalleActividad(Receta receta, Integer position);
    }

}
