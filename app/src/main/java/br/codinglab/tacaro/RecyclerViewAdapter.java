package br.codinglab.tacaro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Produtos.BuscapeProduct;

/* CÓDIGO FONTE FORNECIDO PELA GOOGLE PARA IMPLEMENTAÇÃO DO RECYCLERVIEW ADAPTER */
/* CONTÉM ALTERAÇÕES NO CONSTRUTOR (POSSUI O PARAMETRO CONTEXT A MAIS PARA CARREGAR AS FONTES DOS ASSETS */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<BuscapeProduct> listaProdutos;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(Context context, ArrayList<BuscapeProduct> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView textViewNome = (TextView) holder.view.findViewById(R.id.nome);
        TextView textViewPreco = (TextView) holder.view.findViewById(R.id.preco);
        ImageView imageView = (ImageView) holder.view.findViewById(R.id.imagemProduto);

        //CARREGA A IMAGEM DA POSIÇÃO 'POSITION' ATRAVÉS DA URL DA LISTA
        ImageLoader.getInstance().displayImage(listaProdutos.get(position).getThumbNails().get(1).getUrl(), imageView);

        //SETA O TEXTO PARA O NOME DO PRODUTO E PREÇO
        textViewNome.setText(listaProdutos.get(position).getFullProductName());
        textViewPreco.setText("R$" + String.valueOf(listaProdutos.get(position).getMinProductPrice()));
        textViewNome.setTextColor(Color.parseColor("#FF434343"));
        textViewPreco.setTextColor(Color.parseColor("#FF212121"));

        Typeface ralewayExtraLight = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf");

        textViewPreco.setTypeface(ralewayLight);
        textViewNome.setTypeface(ralewayExtraLight);

        //LISTENER QUE DETECTA UM TOUCH NA VIEW (NO CASO UM ITEM DA LISTA DE PRODUTOS)
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSTÂNCIA DO FRAGMENT 'DETALHES DO PRODUTO'
                DetalhesProdutoFragment detalhesProdutoFragment = new DetalhesProdutoFragment();

                //BUNDLE RESPONSÁVEL POR ENVIAR AS LISTAS RESULTANTES DA PESQUISA DO PRODUTO AO FRAGMENT LISTA
                Bundle bundle = new Bundle();
                bundle.putString("nomeProduto", listaProdutos.get(position).getFullProductName());
                bundle.putString("precoProduto", String.valueOf(listaProdutos.get(position).getMinProductPrice()));
                bundle.putString("imagemProduto", listaProdutos.get(position).getThumbNails().get(1).getUrl());
                bundle.putString("linkProduto", listaProdutos.get(position).getProductLink().getProductUrl());
                bundle.putSerializable("listaOfertas", listaProdutos.get(position).getProductOffers());
                bundle.putInt("qtdeAvaliacoes", listaProdutos.get(position).getRatingAmount());
                bundle.putDouble("notaProduto", listaProdutos.get(position).getProductRatings().getRating());
                bundle.putSerializable("listaEspecificacoes", listaProdutos.get(position).getProductSpecification());
                detalhesProdutoFragment.setArguments(bundle);

                //TROCA O FRAGMENT DA ACTIVITY PARA O FRAGMENT 'DETALHES DO PRODUTO'
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, detalhesProdutoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }
}