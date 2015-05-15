package br.codinglab.tacaro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/* CÓDIGO FONTE FORNECIDO PELA GOOGLE PARA IMPLEMENTAÇÃO DO RECYCLERVIEW ADAPTER */
/* CONTÉM ALTERAÇÕES NO CONSTRUTOR (POSSUI O PARAMETRO CONTEXT A MAIS PARA CARREGAR AS FONTES DOS ASSETS */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> nomes;
    private ArrayList<String> precos;
    private ArrayList<String> linksImgs;
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
    public RecyclerViewAdapter(Context context, ArrayList<String> nomes, ArrayList<String> precos, ArrayList<String> linksImgs) {
        this.context = context;
        this.nomes = nomes;
        this.precos = precos;
        this.linksImgs = linksImgs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView textViewNome = (TextView) holder.view.findViewById(R.id.nome);
        TextView textViewPreco = (TextView) holder.view.findViewById(R.id.preco);
        ImageView imageView = (ImageView) holder.view.findViewById(R.id.imagemProduto);

        Typeface ralewayExtraLight = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf");

        //SETA O TEXTO PARA O NOME DO PRODUTO E PREÇO
        textViewNome.setText(nomes.get(position));
        textViewPreco.setText(precos.get(position));
        textViewNome.setTextColor(Color.parseColor("#FF434343"));
        textViewPreco.setTextColor(Color.parseColor("#FF212121"));

        //CARREGA A IMAGEM DA POSIÇÃO 'POSITION' ATRAVÉS DA URL DA LISTA
        ImageLoader.getInstance().displayImage(linksImgs.get(position), imageView);

        textViewPreco.setTypeface(ralewayLight);
        textViewNome.setTypeface(ralewayExtraLight);
    }

    @Override
    public int getItemCount() {
        return nomes.size();
    }
}