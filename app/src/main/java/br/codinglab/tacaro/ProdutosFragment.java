package br.codinglab.tacaro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ProdutosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_produtos, container, false);

        ArrayList<String> listaNomes;
        ArrayList<String> listaPrecos;
        ArrayList<String> listaImagens;
        ArrayList<String> listaLinks;

        //RECYCLERVIEW PARA A LISTA DE PRODUTOS
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //RECEBE O QUE FOI PASSADO VIA BUNDLE DO FRAGMENT ANTERIOR
        listaNomes = getArguments().getStringArrayList("listaNomes");
        listaPrecos = getArguments().getStringArrayList("listaPrecos");
        listaImagens = getArguments().getStringArrayList("listaImagens");
        listaLinks = getArguments().getStringArrayList("listaLinks");

        //CRIA NOVO ADAPTER DA RECYCLERVIEW PASSANDO AS LISTAS
        adapter = new RecyclerViewAdapter(getActivity(), listaNomes, listaPrecos, listaImagens, listaLinks);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}