package br.codinglab.tacaro;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import Produtos.BuscaPeProduct;
import Produtos.Link;
import Produtos.Offer;
import Produtos.ProductRatings;
import Produtos.Seller;
import Produtos.SellerRatings;
import Produtos.Specification;
import Produtos.ThumbNail;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private Button btnPesquisar;
    private String urlPesquisa;
    private AutoCompleteTextView autoCompleteNome;
    private ImageView imageViewLogo;
    private ArrayList<String> listaNomes;
    private ArrayList<String> listaPrecos;
    private ArrayList<String> listaLinks;
    private ArrayList<String> listaProdutosSugeridos;
    private ArrayList<String> listaImagens;
    private ArrayList<String> listaLinksBuscapeProdutos;
    private ArrayList<String> listaDetalhesTecnicosProdutos;
    private String url = "http://sandbox.buscape.com.br/service/findProductList/4454485358486e4f31326f3d/BR/?format=json&keyword=";
    private NumberFormat formatacaoMoeda;

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        btnPesquisar = (Button) rootView.findViewById(R.id.btnPesquisar);
        autoCompleteNome = (AutoCompleteTextView) rootView.findViewById(R.id.editTextNomeProduto);
        imageViewLogo = (ImageView) rootView.findViewById(R.id.imageViewLogo);

        listaNomes = new ArrayList<>();
        listaPrecos = new ArrayList<>();
        listaLinks = new ArrayList<>();
        listaProdutosSugeridos = new ArrayList<>();
        listaImagens = new ArrayList<>();
        listaLinksBuscapeProdutos = new ArrayList<>();
        listaDetalhesTecnicosProdutos = new ArrayList<>();
        formatacaoMoeda = NumberFormat.getCurrencyInstance();

        //CONFIGURAÇÕES DO UNIVERSAL IMAGE LOADER (IMAGE CACHE E ASYNC REMOTE LOAD)
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);

        autoCompleteNome.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //LISTENER DE UM ITEM DA LISTA DE PESQUISAS SUGERIDAS
        autoCompleteNome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteNome.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Digite o nome do produto a ser pesquisado", Toast.LENGTH_SHORT).show();
                } else {
                    if (estaConectadoInternet()) {
                        if (listasEstaoVazias()) {
                            limpaListasProdutosAnteriores();
                        }
                        urlPesquisa = "http://sandbox.buscape.com.br/service/findProductList/4454485358486e4f31326f3d/BR/?format=json&keyword=";
                        String nomeProduto = autoCompleteNome.getText().toString().replace(" ", "+");
                        urlPesquisa += nomeProduto;
                        new AsyncTaskPesquisa().execute();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(autoCompleteNome.getWindowToken(), 0);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.NetworkSetting");
                        intent.setComponent(cName);
                        Toast.makeText(getActivity(), "Você não está conectado à Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //LISTENER DA TECLA 'PESQUISAR' DO TECLADO VIRTUAL
        autoCompleteNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    return false;
                }
                //VERIFICA SE O USUÁRIO ESTÁ DELETANDO O TEXTO DIGITADO
                if (event.getKeyCode() != KeyEvent.KEYCODE_DEL) {
                    if (autoCompleteNome.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Digite o nome do produto a ser pesquisado", Toast.LENGTH_SHORT).show();
                    } else {
                        if (estaConectadoInternet()) {
                            if (listasEstaoVazias()) {
                                limpaListasProdutosAnteriores();
                            }
                            urlPesquisa = "http://sandbox.buscape.com.br/service/findProductList/4454485358486e4f31326f3d/BR/?format=json&keyword=";
                            String nomeProduto = autoCompleteNome.getText().toString().replace(" ", "+");
                            urlPesquisa += nomeProduto;
                            new AsyncTaskPesquisa().execute();

                            //PARA ESCONDER O TECLADO VIRTUAL QUANDO APERTAR O BOTÃO PARA PESQUISAR
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(autoCompleteNome.getWindowToken(), 0);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                            Toast.makeText(getActivity(), "Você não está conectado à Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });

        //LISTENER QUE RECUPERA AS PESQUISAS SUGERIDAS DE ACORDO COM O QUE O USUÁRIO DIGITA
        autoCompleteNome.addTextChangedListener(new TextWatcher() {
            String urlOriginal = "http://www.buscape.com.br/ajax/autoComplete?q=";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!autoCompleteNome.getText().equals("")) {
                    if (s.toString().contains(" ")) {
                        s = s.toString().replace(" ", "+");
                    }
                    url = urlOriginal.concat(s.toString());
                    if (estaConectadoInternet()) {
                        new AsyncTaskPesquisaTemporaria().execute();
                    } else {
                        Toast.makeText(getActivity(), "Você não está conectado à Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //LISTENER DO BOTÃO PESQUISAR DA VIEW
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteNome.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Digite o nome do produto a ser pesquisado", Toast.LENGTH_SHORT).show();
                } else {
                    if (estaConectadoInternet()) {
                        if (listasEstaoVazias()) {
                            limpaListasProdutosAnteriores();
                        }
                        urlPesquisa = "http://sandbox.buscape.com.br/service/findProductList/4454485358486e4f31326f3d/BR/?format=json&keyword=";
                        String nomeProduto = autoCompleteNome.getText().toString().replace(" ", "+");
                        urlPesquisa += nomeProduto;
                        new AsyncTaskPesquisa().execute();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(autoCompleteNome.getWindowToken(), 0);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Você não está conectado à Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //FONTES RALEWAY
        Typeface ralewayExtraLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-ExtraLight.ttf");
        Typeface ralewayLight = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Light.ttf");
        btnPesquisar.setTypeface(ralewayLight);
        autoCompleteNome.setTypeface(ralewayExtraLight);

        return rootView;
    }

    //VERIFICA SE O USUÁRIO ESTÁ CONECTADO
    private boolean estaConectadoInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] informacaoRede = connectivity.getAllNetworkInfo();
            if (informacaoRede != null)
                for (int i = 0; i < informacaoRede.length; i++) {
                    if (informacaoRede[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    //FUNÇÃO QUE VERIFICA SE AS LISTAS ESTÃO VAZIAS (CASO O USUÁRIO JÁ TENHA PESQUISADO ALGUM PRODUTO
    private boolean listasEstaoVazias() {
        if (!listaNomes.isEmpty()) {
            return false;
        }
        return true;
    }

    //FUNÇÃO QUE LIMPA AS LISTAS SE ELAS JÁ POSSUIREM ALGO (CASO O USUÁRIO PESQUISE UM SEGUNDO PRODUTO)
    private void limpaListasProdutosAnteriores() {
        listaNomes.clear();
        listaLinks.clear();
        listaPrecos.clear();
        listaImagens.clear();
        listaLinksBuscapeProdutos.clear();
    }

    //ASYNCTASK PARA PESQUISA DE PRODUTOS DA API BUSCAPÉ
    class AsyncTaskPesquisa extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Busca");
            progressDialog.setMessage("Carregando...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            ServiceHandler serviceHandler = new ServiceHandler();
            String jsonStr = serviceHandler.makeServiceCall(urlPesquisa, ServiceHandler.GET);
            if (jsonStr != null) {

                /*SAMUEL*/

                try {
                    JSONObject generalJsonObject = new JSONObject(jsonStr);

                    JSONArray jsonArray = generalJsonObject.getJSONArray("product");

                    ArrayList<BuscaPeProduct> generalListOfProducts = new ArrayList<>();
                    for(int i = 0 ; i < jsonArray.length(); i++ ){

                        JSONObject jsonObj = jsonArray.getJSONObject(i).getJSONObject("product");


                        /*INFORMAÇÕES BÁSICOS SOBRE A PESQUISA DE UM PRODUTO*/
                        String fullProductName = jsonObj.optString("productname");
                        double maxPrice = Double.parseDouble(jsonObj.optString("pricemax"));
                        double minPrice = Double.parseDouble(jsonObj.optString("pricemin"));
                        int quantity = Integer.parseInt(jsonObj.optString("quantity"));
                        int amountOfOffers = Integer.parseInt(jsonObj.optString("numoffers"));
                        int amountOfSellers = Integer.parseInt(jsonObj.optString("totalsellers"));
                        int productId = Integer.parseInt(jsonObj.optString("id"));
                        int productCategoryId = Integer.parseInt(jsonObj.optString("categoryid"));

                        BuscaPeProduct tempProduct = new BuscaPeProduct(fullProductName,maxPrice,minPrice,quantity,amountOfOffers,amountOfSellers,productId,productCategoryId);


                        /*THUMB NAILS*/
                        JSONArray jsonThumbNailArray = jsonObj.getJSONObject("thumbnail").getJSONArray("formats");
                        for (int j = 0; j < jsonThumbNailArray.length(); j++) {
                            int width = Integer.parseInt(jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("width"));
                            int height = Integer.parseInt(jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("height"));
                            String url = jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("url");
                            tempProduct.setThumbNail(new ThumbNail(width, height, url));
                        }


                        /*SPEFICIATIONS*/
                        if(!jsonObj.getJSONObject("specification").isNull("item")){
                            JSONArray productSpecification = jsonObj.getJSONObject("specification").getJSONArray("item");
                            for (int j = 0; j < productSpecification.length(); j++) {
                                String label = productSpecification.getJSONObject(j).getJSONObject("item").getString("label");
                                JSONArray values = productSpecification.getJSONObject(j).getJSONObject("item").getJSONArray("label");
                                String sumValues = "";
                                for(int k = 0; k < values.length(); k++){
                                    sumValues += values.getString(k);
                                }
                                tempProduct.setSpecification(new Specification(label,sumValues));

                            }
                        }

                        /*PRODUCT RATINGS*/
                        double rating = Double.parseDouble(jsonObj.getJSONObject("rating").getJSONObject("useraveragerating").getString("rating"));
                        ProductRatings productRatings = new ProductRatings(rating);
                        tempProduct.setProductRatings(productRatings);

                        /*LINKS*/
                        JSONArray productLinks = jsonObj.getJSONArray("links");
                        for (int j = 0; j < productLinks.length(); j++) {
                            String jsonUrl = "";
                            String productUrl = "";
                            if(productLinks.getJSONObject(j).getJSONObject("link").getString("type") == "product"){
                                productUrl = productLinks.getJSONObject(j).getJSONObject("link").getString("url");
                            }else{
                                jsonUrl = productLinks.getJSONObject(j).getJSONObject("link").getString("url");
                            }
                            tempProduct.setProductLink(new Link(productUrl, jsonUrl));
                        }

                        /*OFFERS*/

                        String offersJson = serviceHandler.makeServiceCall(tempProduct.getProductLink().getProductJsonUrl(), ServiceHandler.GET);

                        JSONObject generalOffersJson = new JSONObject(offersJson);

                        JSONArray offerArray = generalOffersJson.getJSONArray("offer");

                        for(int o = 1; o < offerArray.length(); o++){

                            JSONObject seller = offerArray.getJSONObject(i).getJSONObject("seller");

                            String logoUrl = seller.getJSONObject("thumbnail").getString("url");
                            ThumbNail sellerThumbNail = new ThumbNail(0,0,logoUrl);

                            String sellerRatingType = seller.getJSONObject("rating").getJSONObject("ebitrating").getString("rating");
                            double sellerRating = Double.parseDouble(seller.getJSONObject("rating").getJSONObject("useraveragerating").getString("rating"));
                            SellerRatings sellerRatings = new SellerRatings(sellerRating,sellerRatingType);

                            String sellerName = seller.getString("sellername");
                            String sellerWebSiteUrl = seller.getJSONArray("links").getJSONObject(1).getJSONObject("link").getString("url");
                            int sellerId = Integer.parseInt(seller.getString("id"));

                            Seller productSeller = new Seller(sellerName,sellerId,sellerThumbNail,sellerRatings,sellerWebSiteUrl);


                            int offerProductId = Integer.parseInt(offerArray.getJSONObject(i).getString("productid"));
                            String offerName = offerArray.getJSONObject(i).getString("offername");
                            double fullPrice = Double.parseDouble(offerArray.getJSONObject(i).getJSONObject("price").getString("value"));
                            int amountOfParcels = Integer.parseInt(offerArray.getJSONObject(i).getJSONObject("price").getJSONObject("parcel").getString("number"));
                            double parcelValue = Double.parseDouble(offerArray.getJSONObject(i).getJSONObject("price").getJSONObject("parcel").getString("value"));

                            Offer tempOffer = new Offer(productSeller,productId,fullPrice,amountOfParcels,parcelValue,offerName);
                            tempProduct.setOffer(tempOffer);
                        }







                        generalListOfProducts.add(tempProduct);
                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*END - SAMUEL*/


                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj.optString("totalresultsreturned").equals("0")) {
                        return "Não encontrou resultado";
                    }
                    else {
                        JSONArray jsonArray = jsonObj.getJSONArray("product");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonProduto = jsonArray.getJSONObject(i).getJSONObject("product");
                            //SE O NOME CURTO DO PRODUTO ESTÁ PRESENTE NO JSON
                            if (jsonProduto.isNull("productshortname")) {
                                listaNomes.add(jsonProduto.optString("productname"));
                            }
                            else {
                                listaNomes.add(jsonProduto.optString("productshortname"));
                            }

                            //SE O PREÇO MÍNIMO DO PRODUTO ESTÁ PRESENTE NO JSON
                            if (jsonProduto.isNull("pricemin")) {
                                listaPrecos.add("Produto não disponível");
                            }
                            else {
                                String precoProduto = jsonProduto.optString("pricemin");
                                precoProduto = formatacaoMoeda.format(Double.parseDouble(precoProduto));
                                listaPrecos.add(precoProduto);
                            }

                            //SE O PRODUTO NÃO POSSUI FOTO
                            if (jsonProduto.isNull("thumbnail")) {
                                listaImagens.add("assets://SemImagem.png");
                            }
                            else {
                                //SE A FOTO NA RESOLUÇÃO 300X300 ESTÁ DISPONÍVEL
                                if (!jsonProduto.getJSONObject("thumbnail").getJSONArray("formats").isNull(1)) {
                                    listaImagens.add(jsonProduto.getJSONObject("thumbnail").getJSONArray("formats").getJSONObject(1).getJSONObject("formats").optString("url"));
                                }
                                else {
                                    listaImagens.add("assets://SemImagem.png");
                                }
                            }

                            //ADICIONA A URL PARA ACESSO DO PRODUTO NO SITE DO BUSCAPÉ
                            String linkOfertasProduto = jsonProduto.getJSONArray("links").getJSONObject(1).getJSONObject("link").optString("url");

                            //ADICIONA O FORMAT=JSON AO LINK DE OFERTA DO PRODUTO
                            linkOfertasProduto = linkOfertasProduto.substring(0, linkOfertasProduto.indexOf("?productId")) + "?format=json&" + linkOfertasProduto.substring(linkOfertasProduto.indexOf("?productId")+1, linkOfertasProduto.length());
                            listaLinks.add(linkOfertasProduto);

                            //ADICIONA OS LINKS DOS PRODUTOS NO SITE DO BUSCAPÉ
                            listaLinksBuscapeProdutos.add(jsonProduto.getJSONArray("links").getJSONObject(0).getJSONObject("link").optString("url"));

                            //for (int j = 0; j < jsonProduto.getJSONObject("specification").getJSONArray("item").length(); j++) {
                            //    listaDetalhesTecnicosProdutos.add(jsonProduto.getJSONObject("specification").getJSONArray("item").getJSONObject(j).getJSONObject("item").optString("label"));
                            //    Log.d("OK", listaDetalhesTecnicosProdutos.get(i) + " " + i);
                            //}
                        }
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("AsyncTaskPesquisa", "Não foi possível recuperar os dados do JSON");
                }
            } else {
                Log.e("ServiceHandler", "Não foi possível recuperar os dados da URL.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            //SE É DIFERENTE DE NULL ENTÃO NÃO ENCONTROU NENHUM RESULTADO PARA A PESQUISA
            if (s != null) {
                Toast.makeText(getActivity(), "Não encontramos seu produto :(", Toast.LENGTH_LONG).show();
            }
            else {
                //INSTÂNCIA DO FRAGMENT 'LISTA DE PRODUTOS'
                ProdutosFragment produtosFragment = new ProdutosFragment();

                //BUNDLE RESPONSÁVEL POR ENVIAR AS LISTAS RESULTANTES DA PESQUISA DO PRODUTO AO FRAGMENT LISTA
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("listaNomes", listaNomes);
                bundle.putStringArrayList("listaPrecos", listaPrecos);
                bundle.putStringArrayList("listaImagens", listaImagens);
                bundle.putStringArrayList("listaLinks", listaLinks);
                bundle.putStringArrayList("listaLinksBuscape", listaLinksBuscapeProdutos);
                bundle.putStringArrayList("listaDetalhesTecnicos", listaDetalhesTecnicosProdutos);

                produtosFragment.setArguments(bundle);

                //SUBSTITUI O FRAGMENT ATUAL DA ACTIVITY (INICIOFRAGMENT) PARA O FRAGMENT 'LISTA DE PRODUTOS'
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, produtosFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    //ASYNCTASK RESPONSÁVEL PELA PESQUISA DE ACORDO COM O QUE O USUÁRIO ESTÁ DIGITANDO (AUTO-COMPLETE)
    class AsyncTaskPesquisaTemporaria extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHandler serviceHandler = new ServiceHandler();
            String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    if (!listaProdutosSugeridos.isEmpty()) {
                        listaProdutosSugeridos.clear();
                    }
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    for (int i = 0; i < jsonObject.getJSONArray("palavras").length(); i++) {
                         String nomeProdutoSugerido = jsonObject.getJSONArray("palavras").getString(i);
                         listaProdutosSugeridos.add(nomeProdutoSugerido);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Não foi possível recuperar os dados da URL.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //CRIA UM ADAPTER SIMPLES PASSANDO O RESULTADO DO AUTO-COMPLETE DA API BUSCAPÉ
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, listaProdutosSugeridos);
            autoCompleteNome.setAdapter(adapter);
        }
    }
}