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

public class RecyclerViewAdapterOfertas extends RecyclerView.Adapter<RecyclerViewAdapterOfertas.ViewHolder> {

    private ArrayList<String> precos;
    private ArrayList<String> linksImgs;
    private ArrayList<String> nomesLojas;
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
    public RecyclerViewAdapterOfertas(Context context, ArrayList<String> precos, ArrayList<String> linksImgs, ArrayList<String> nomesLojas) {
        this.context = context;
        this.precos = precos;
        this.linksImgs = linksImgs;
        this.nomesLojas = nomesLojas;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterOfertas.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.oferta_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView textViewPreco = (TextView) holder.view.findViewById(R.id.precoLoja);
        TextView textViewNome = (TextView) holder.view.findViewById(R.id.nomeLoja);
        ImageView imageView = (ImageView) holder.view.findViewById(R.id.imagemLoja);

        if (nomesLojas.get(position).equals("assets://SemImagem.png")) {
            textViewNome.setText(nomesLojas.get(position));
            textViewNome.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(linksImgs.get(position), imageView);
        }
        else {
            ImageLoader.getInstance().displayImage(linksImgs.get(position), imageView);
        }

        //SETA O TEXTO PARA O NOME DO PRODUTO E PREÇO
        textViewNome.setText(nomesLojas.get(position));
        textViewPreco.setText("R$" + precos.get(position));
        textViewPreco.setTextColor(Color.parseColor("#FF212121"));

        Typeface ralewayExtraLight = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf");

        textViewPreco.setTypeface(ralewayLight);
        textViewNome.setTypeface(ralewayExtraLight);
    }

    @Override
    public int getItemCount() {
        return precos.size();
    }
}