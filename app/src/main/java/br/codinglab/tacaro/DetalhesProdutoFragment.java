package br.codinglab.tacaro;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalhesProdutoFragment extends Fragment {


    public DetalhesProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detalhes_produto, container, false);

        TextView textViewNomeProduto = (TextView) rootView.findViewById(R.id.textViewNomeProduto);
        TextView textViewPrecoProduto = (TextView) rootView.findViewById(R.id.textViewPrecoProduto);
        ImageView imageViewProduto = (ImageView) rootView.findViewById(R.id.imageViewProduto);

        String nomeProduto = getArguments().getString("nomeProduto");
        String precoProduto = getArguments().getString("precoProduto");
        String imagemProduto = getArguments().getString("imagemProduto");
        String linkProduto = getArguments().getString("linkProduto");

        ImageLoader.getInstance().displayImage(imagemProduto, imageViewProduto);
        textViewNomeProduto.setText(nomeProduto);
        textViewPrecoProduto.setText(precoProduto);

        textViewNomeProduto.setTextColor(Color.parseColor("#FF434343"));
        textViewPrecoProduto.setTextColor(Color.parseColor("#FF212121"));

        Typeface ralewayExtraLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Light.ttf");

        textViewNomeProduto.setTypeface(ralewayExtraLight);
        textViewPrecoProduto.setTypeface(ralewayLight);

        return rootView;
    }


}