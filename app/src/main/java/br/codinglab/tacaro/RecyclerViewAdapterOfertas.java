package br.codinglab.tacaro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Produtos.Offer;

public class RecyclerViewAdapterOfertas extends RecyclerView.Adapter<RecyclerViewAdapterOfertas.ViewHolder> {

    private ArrayList<Offer> listaOfertas;
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
    public RecyclerViewAdapterOfertas(Context context, ArrayList<Offer> listaOfertas) {
        this.context = context;
        this.listaOfertas = listaOfertas;
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
        ImageView imageViewLoja = (ImageView) holder.view.findViewById(R.id.imagemLoja);
        ImageView imageViewProduto = (ImageView) holder.view.findViewById(R.id.imagemProduto);

        //VERIFICA SE NÃO TEM IMAGEM. CASO NÃO TENHA, ENTÃO MOSTRE O TEXTVIEW COM O NOME DA LOJA
        if (listaOfertas.get(position).getSeller().getSellerThumbNail().equals("")) {
            textViewNome.setText(listaOfertas.get(position).getSeller().getSellerName());
            textViewNome.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage("", imageViewLoja);
        }
        else {
            ImageLoader.getInstance().displayImage(listaOfertas.get(position).getSeller().getSellerThumbNail().getUrl(), imageViewLoja);
        }

        ImageLoader.getInstance().displayImage(listaOfertas.get(position).getProductThumbnail().getUrl(), imageViewProduto);

        //SETA O TEXTO PARA O NOME DO PRODUTO E PREÇO
        textViewNome.setText(listaOfertas.get(position).getOfferName());
        textViewPreco.setText("R$" + String.valueOf(listaOfertas.get(position).getFullPrice()));
        textViewPreco.setTextColor(Color.parseColor("#FF212121"));

        //AO CLICAR EM UMA OFERTA, O APP ABRE O LINK CAPTURADO DO JSON (LINK DO PRODUTO NO BUSCAPÉ)
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listaOfertas.get(position).getSeller().getSellerWebSiteUrl())));
            }
        });

        Typeface ralewayExtraLight = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf");

        textViewPreco.setTypeface(ralewayExtraLight);
        textViewNome.setTypeface(ralewayExtraLight);
    }

    @Override
    public int getItemCount() {
        return listaOfertas.size();
    }
}