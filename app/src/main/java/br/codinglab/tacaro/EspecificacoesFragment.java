package br.codinglab.tacaro;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Produtos.Specification;


/**
 * A simple {@link Fragment} subclass.
 */
public class EspecificacoesFragment extends Fragment {

    private ArrayList<Specification> listaEspecificacoes;

    public EspecificacoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_especificacoes, container, false);
        listaEspecificacoes = (ArrayList<Specification>) getArguments().getSerializable("listaEspecificacoes");
        TextView textViewEspecificacoes = (TextView) rootView.findViewById(R.id.textViewEspecificacoes);
        TextView textViewTituloEspecificacoes = (TextView) rootView.findViewById(R.id.textViewTituloEspecificacoes);

        if (listaEspecificacoes.size() > 0) {
            textViewEspecificacoes.setText("");
            for (int i = 0; i < listaEspecificacoes.size(); i++) {
                String[] value = listaEspecificacoes.get(i).getValue().split(System.getProperty("line.separator"));
                textViewEspecificacoes.append(listaEspecificacoes.get(i).getLabel() + ": " + System.getProperty("line.separator"));
                for (int j = 0; j < value.length; j++) {
                    textViewEspecificacoes.append("\t" + value[j] + System.getProperty("line.separator"));
                }
                textViewEspecificacoes.append(System.getProperty("line.separator"));
            }
        }

        Typeface ralewayExtraLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Light.ttf");
        textViewEspecificacoes.setTypeface(ralewayLight);
        textViewTituloEspecificacoes.setTypeface(ralewayLight);

        return rootView;
    }


}
