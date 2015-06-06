package br.codinglab.tacaro;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.ArrayList;

import Produtos.Offer;
import Produtos.Specification;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalhesProdutoFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RatingBar avaliacaoProduto;
    private TextView textViewNumAvaliacoes;
    private NumberFormat formatacaoMoeda;
    private ArrayList<Specification> listaEspecificacoes;
    private ArrayList<Offer> listaOfertas;

    public DetalhesProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detalhes_produto, container, false);

        //RECYCLERVIEW PARA A LISTA DE PRODUTOS
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        TextView textViewNomeProduto = (TextView) rootView.findViewById(R.id.textViewNomeProduto);
        TextView textViewPrecoProduto = (TextView) rootView.findViewById(R.id.textViewPrecoProduto);
        ImageView imageViewProduto = (ImageView) rootView.findViewById(R.id.imageViewProduto);
        textViewNumAvaliacoes = (TextView) rootView.findViewById(R.id.textViewNumAvaliacoes);
        TextView textViewLabelPreco = (TextView) rootView.findViewById(R.id.textViewLabelPreco);
        Button btnIrParaDetalhes = (Button) rootView.findViewById(R.id.btnIrParaDetalhes);
        avaliacaoProduto = (RatingBar) rootView.findViewById(R.id.avaliacaoProduto);
        formatacaoMoeda = NumberFormat.getCurrencyInstance();

        String nomeProduto = getArguments().getString("nomeProduto");
        String precoProduto = getArguments().getString("precoProduto");
        String linkProduto = getArguments().getString("linkProduto");
        String imagemProduto = getArguments().getString("imagemProduto");
        int qtdeAvaliacoes = getArguments().getInt("qtdeAvaliacoes");
        double notaProduto = getArguments().getDouble("notaProduto");

        listaEspecificacoes = (ArrayList<Specification>) getArguments().getSerializable("listaEspecificacoes");
        listaOfertas = (ArrayList<Offer>) getArguments().getSerializable("listaOfertas");

        ImageLoader.getInstance().displayImage(imagemProduto, imageViewProduto);
        textViewNomeProduto.setText(nomeProduto);
        textViewPrecoProduto.setText("R$" + precoProduto);

        textViewNomeProduto.setTextColor(Color.parseColor("#FF434343"));
        textViewPrecoProduto.setTextColor(Color.parseColor("#FF212121"));

        Typeface ralewayExtraLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Light.ttf");

        textViewNomeProduto.setTypeface(ralewayLight);
        textViewPrecoProduto.setTypeface(ralewayLight);
        textViewNumAvaliacoes.setTypeface(ralewayLight);
        textViewLabelPreco.setTypeface(ralewayExtraLight);
        btnIrParaDetalhes.setTypeface(ralewayLight);

        textViewNumAvaliacoes.setText(qtdeAvaliacoes + " avaliaram");
        avaliacaoProduto.setRating(Math.round(Float.parseFloat(String.valueOf(notaProduto)) / 2));

        btnIrParaDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EspecificacoesFragment especificacoesFragment = new EspecificacoesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("listaEspecificacoes", listaEspecificacoes);
                especificacoesFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, especificacoesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        adapter = new RecyclerViewAdapterOfertas(getActivity(), listaOfertas);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}