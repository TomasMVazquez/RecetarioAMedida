package com.example.toms.recetarioamedida.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toms.recetarioamedida.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecetaDetalleFragment extends Fragment {


    public RecetaDetalleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receta_detalle, container, false);
    }

}
