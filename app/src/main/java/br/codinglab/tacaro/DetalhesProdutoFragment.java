package br.codinglab.tacaro;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import Produtos.BuscapeProduct;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalhesProdutoFragment extends Fragment {

    private ArrayList<String> linksImagensLoja;
    private ArrayList<String> nomesLojas;
    private ArrayList<String> precosLojas;
    private ArrayList<String> linksImagensProdutos;
    private ArrayList<String> linksProdutosBuscape;
    private ArrayList<String> listaDetalhesTecnicos;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RatingBar avaliacaoProduto;
    private TextView textViewNumAvaliacoes;
    private NumberFormat formatacaoMoeda;

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
        avaliacaoProduto = (RatingBar) rootView.findViewById(R.id.avaliacaoProduto);

        linksImagensLoja = new ArrayList<>();
        nomesLojas = new ArrayList<>();
        precosLojas = new ArrayList<>();
        linksImagensProdutos = new ArrayList<>();
        formatacaoMoeda = NumberFormat.getCurrencyInstance();


        ArrayList<BuscapeProduct> lista = (ArrayList<BuscapeProduct>) getArguments().getSerializable("listinhaDoAmorDeProdutinhos");
        Log.d("LISTATAMANHO", String.valueOf(lista.size()));

        String nomeProduto = getArguments().getString("nomeProduto");
        String precoProduto = getArguments().getString("precoProduto");
        String linkProduto = getArguments().getString("linkProduto");
        String imagemProduto = getArguments().getString("imagemProduto");
        String linkProdutoBuscape = getArguments().getString("linkProdutoBuscape");
        linksProdutosBuscape = getArguments().getStringArrayList("linksProdutosBuscape");
        listaDetalhesTecnicos = getArguments().getStringArrayList("listaDetalhesTecnicos");

        ImageLoader.getInstance().displayImage(imagemProduto, imageViewProduto);
        textViewNomeProduto.setText(nomeProduto);

        //VERIFICA SE O PRODUTO POSSUI UM PREÇO OU NÃO PARA EXIBIR
        if (precoProduto.contains("Produto não disponível")) {
            textViewPrecoProduto.setText(precoProduto);
        }
        else {
            textViewPrecoProduto.setText(precoProduto);
        }

        textViewNomeProduto.setTextColor(Color.parseColor("#FF434343"));
        textViewPrecoProduto.setTextColor(Color.parseColor("#FF212121"));

        Typeface ralewayExtraLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Light.ttf");

        textViewNomeProduto.setTypeface(ralewayLight);
        textViewPrecoProduto.setTypeface(ralewayLight);
        textViewNumAvaliacoes.setTypeface(ralewayLight);
        textViewLabelPreco.setTypeface(ralewayExtraLight);

        new AsyncTaskPesquisaOfertas().execute(linkProduto);

        return rootView;
    }

    class AsyncTaskPesquisaOfertas extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Ofertas");
            progressDialog.setMessage("Buscando ofertas...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urlProduto) {
            ServiceHandler serviceHandler = new ServiceHandler();
            String jsonStr = serviceHandler.makeServiceCall(urlProduto[0], ServiceHandler.GET);
            if (jsonStr != null) {
                if (!nomesLojas.isEmpty()) {
                    nomesLojas.clear();
                    precosLojas.clear();
                    linksImagensLoja.clear();
                }
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonProduto = jsonObj.getJSONArray("product");

                    final String numeroAvaliacoes = jsonProduto.getJSONObject(0).getJSONObject("product").getJSONObject("rating").getJSONObject("useraveragerating").optString("numcomments");
                    //PARA SETAR VALOR DE TEXTVIEW É NECESSÁRIO FAZER NA THREAD PRINCIPAL (UI THREAD)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewNumAvaliacoes.setText(numeroAvaliacoes + " avaliaram");
                        }
                    });

                    //PARSE DA NOTA DO PRODUTO (REFATORAR DEPOIS)
                    String notaProduto = jsonProduto.getJSONObject(0).getJSONObject("product").getJSONObject("rating").getJSONObject("useraveragerating").optString("rating");
                    avaliacaoProduto.setRating(Math.round(Float.parseFloat(notaProduto) / 2));

                    JSONArray jsonArray = jsonObj.getJSONArray("offer");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonOferta = jsonArray.getJSONObject(i).getJSONObject("offer");
                        JSONObject jsonLoja = jsonOferta.getJSONObject("seller");

                        if (!jsonOferta.isNull("thumbnail")) {
                            linksImagensProdutos.add(jsonOferta.getJSONObject("thumbnail").optString("url"));
                        }
                        else {
                            linksImagensProdutos.add("assets://SemImagem.png");
                        }

                        if (!jsonLoja.isNull("thumbnail")) {
                            linksImagensLoja.add(jsonLoja.getJSONObject("thumbnail").optString("url"));
                            nomesLojas.add("");
                        }
                        else {
                            linksImagensLoja.add("assets://SemImagem.png");
                            nomesLojas.add(jsonLoja.optString("sellername"));
                        }
                        String precoOferta = jsonOferta.getJSONObject("price").optString("value");
                        precoOferta = formatacaoMoeda.format(Double.parseDouble(precoOferta));
                        precosLojas.add(precoOferta);
                    }
                } catch (JSONException e) {
                    Log.e("AsyncTaskPesquisa", "Não foi possível recuperar os dados do JSON: " + e.toString());
                }

            } else {
                Log.e("ServiceHandler", "Não foi possível recuperar os dados da URL.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            adapter = new RecyclerViewAdapterOfertas(getActivity(), precosLojas, linksImagensLoja, nomesLojas, linksImagensProdutos, linksProdutosBuscape);
            recyclerView.setAdapter(adapter);
        }
    }
}