package br.codinglab.tacaro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Produtos.BuscapeProduct;

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

        ArrayList<BuscapeProduct> listaProdutos;

        //RECYCLERVIEW PARA A LISTA DE PRODUTOS
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //RECEBE O QUE FOI PASSADO VIA BUNDLE DO FRAGMENT ANTERIOR (InicioFragment)
        listaProdutos = (ArrayList<BuscapeProduct>) getArguments().getSerializable("listaProdutos");

        //CRIA NOVO ADAPTER DA RECYCLERVIEW PASSANDO AS LISTAS
        adapter = new RecyclerViewAdapter(getActivity(), listaProdutos);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}