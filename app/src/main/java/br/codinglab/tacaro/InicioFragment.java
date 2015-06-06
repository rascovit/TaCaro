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

import Produtos.BuscapeProduct;
import Produtos.Link;
import Produtos.Offer;
import Produtos.ProductRatings;
import Produtos.Seller;
import Produtos.SellerRatings;
import Produtos.Specification;
import Produtos.Thumbnail;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private Button btnPesquisar;
    private String urlPesquisa;
    private AutoCompleteTextView autoCompleteNome;
    private ImageView imageViewLogo;
    private ArrayList<BuscapeProduct> generalListOfProducts;
    private ArrayList<String> listaProdutosSugeridos;
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
        listaProdutosSugeridos = new ArrayList<>();
        generalListOfProducts = new ArrayList<>();
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
                if (!generalListOfProducts.isEmpty()) {
                    generalListOfProducts.clear();
                }
                try {
                    JSONObject generalJsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = generalJsonObject.getJSONArray("product");
                    for(int i = 0 ; i < jsonArray.length(); i++ ){
                        JSONObject jsonObj = jsonArray.getJSONObject(i).getJSONObject("product");
                        /*INFORMAÇÕES BÁSICOS SOBRE A PESQUISA DE UM PRODUTO*/
                        String fullProductName = jsonObj.getString("productname");
                        double maxPrice;
                        double minPrice = Double.parseDouble(jsonObj.getString("pricemin"));
                        if(!jsonObj.isNull("pricemax")){
                            maxPrice = Double.parseDouble(jsonObj.getString("pricemax"));
                        }else{
                            maxPrice = minPrice;
                        }
                        int quantity = Integer.parseInt(jsonObj.getString("quantity"));
                        int amountOfOffers = Integer.parseInt(jsonObj.getString("numoffers"));
                        int amountOfSellers = Integer.parseInt(jsonObj.getString("totalsellers"));
                        int productId = Integer.parseInt(jsonObj.getString("id"));
                        int productCategoryId = Integer.parseInt(jsonObj.getString("categoryid"));
                        int ratingAmount = Integer.parseInt(jsonObj.getJSONObject("rating").getJSONObject("useraveragerating").optString("numcomments"));

                        BuscapeProduct tempProduct = new BuscapeProduct(fullProductName,maxPrice,minPrice,quantity,amountOfOffers,amountOfSellers,productId,productCategoryId,ratingAmount);

                        /*THUMBNAILS*/
                        JSONArray jsonThumbNailArray = jsonObj.getJSONObject("thumbnail").getJSONArray("formats");
                        for (int j = 0; j < jsonThumbNailArray.length(); j++) {
                            int width = 0;
                            int height = 0;
                            if (jsonThumbNailArray.getJSONObject(j).isNull("formats")) {
                                width = Integer.parseInt(jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("width"));
                                height = Integer.parseInt(jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("height"));
                            }
                            String url = jsonThumbNailArray.getJSONObject(j).getJSONObject("formats").optString("url");
                            tempProduct.setThumbNail(new Thumbnail(width, height, url));
                        }


                        /*SPEFICIATIONS*/
                        if(!jsonObj.getJSONObject("specification").isNull("item")){
                            JSONArray productSpecification = jsonObj.getJSONObject("specification").getJSONArray("item");
                            for (int j = 0; j < productSpecification.length(); j++) {
                                String label = productSpecification.getJSONObject(j).getJSONObject("item").getString("label");
                                JSONArray values = productSpecification.getJSONObject(j).getJSONObject("item").getJSONArray("value");
                                String sumValues = "";
                                for(int k = 0; k < values.length(); k++){
                                    sumValues += values.getString(k) + System.getProperty("line.separator");
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
                        for(int o = 0; o < offerArray.length(); o++){
                            JSONObject seller = offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("seller");
                            String logoUrl = "";
                            if (!seller.isNull("thumbnail")) {
                                logoUrl = seller.getJSONObject("thumbnail").getString("url");
                            }
                            Thumbnail sellerThumbNail = new Thumbnail(0,0,logoUrl);

                            String sellerRatingType = "";
                            if (!seller.getJSONObject("rating").isNull("ebitrating")) {
                                sellerRatingType = seller.getJSONObject("rating").getJSONObject("ebitrating").getString("rating");
                            }

                            double sellerRating = 0.0;
                            if (!seller.getJSONObject("rating").isNull("useraveragerating")) {
                                sellerRating = Double.parseDouble(seller.getJSONObject("rating").getJSONObject("useraveragerating").getString("rating"));
                            }
                            SellerRatings sellerRatings = new SellerRatings(sellerRating,sellerRatingType);

                            String sellerName = seller.getString("sellername");

                            String sellerWebSiteUrl = "";
                            if (!seller.getJSONArray("links").getJSONObject(0).isNull("link")) {
                                sellerWebSiteUrl = seller.getJSONArray("links").getJSONObject(0).getJSONObject("link").getString("url");
                            }
                            int sellerId = Integer.parseInt(seller.getString("id"));
                            Seller productSeller = new Seller(sellerName,sellerId,sellerThumbNail,sellerRatings,sellerWebSiteUrl);


                            int offerProductId;
                            String offerName = "";
                            if (!offerArray.getJSONObject(o).isNull("offer")) {
                                offerProductId = Integer.parseInt(offerArray.getJSONObject(o).getJSONObject("offer").getString("productid"));
                                offerName = offerArray.getJSONObject(o).getJSONObject("offer").getString("offername");
                            }


                            double fullPrice = 0.0;
                            if(!offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("price").isNull("value")){
                                fullPrice = Double.parseDouble(offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("price").getString("value"));
                            }

                            int amountOfParcels = 1;
                            double parcelValue = fullPrice;
                            if(!offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("price").isNull("parcel")){
                                 amountOfParcels = Integer.parseInt(offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("price").getJSONObject("parcel").getString("number"));
                                 parcelValue = Double.parseDouble(offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("price").getJSONObject("parcel").getString("value"));
                            }

                            String url = offerArray.getJSONObject(o).getJSONObject("offer").getJSONObject("thumbnail").optString("url");
                            Thumbnail thumbNail = new Thumbnail(0,0,url);
                            Offer tempOffer = new Offer(productSeller,productId,fullPrice,amountOfParcels,parcelValue,offerName,thumbNail);
                            tempProduct.setOffer(tempOffer);
                        }
                        generalListOfProducts.add(tempProduct);
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
                bundle.putSerializable("listaProdutos",generalListOfProducts);
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