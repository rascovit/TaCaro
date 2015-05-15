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
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private Button btnPesquisar;
    private String urlPesquisa;
    private TextView tituloApp;
    private AutoCompleteTextView autoCompleteNome;
    private ArrayList<String> listaNomes;
    private ArrayList<String> listaPrecos;
    private ArrayList<String> listaLinks;
    private ArrayList<String> listaProdutosSugeridos;
    private ArrayList<String> listaImagens;
    private String url = "http://sandbox.buscape.com.br/service/findProductList/4454485358486e4f31326f3d/BR/?format=json&keyword=";

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        btnPesquisar = (Button) rootView.findViewById(R.id.btnPesquisar);
        autoCompleteNome = (AutoCompleteTextView) rootView.findViewById(R.id.editTextNomeProduto);
        tituloApp = (TextView) rootView.findViewById(R.id.textViewTitulo);

        listaNomes = new ArrayList<>();
        listaPrecos = new ArrayList<>();
        listaLinks = new ArrayList<>();
        listaProdutosSugeridos = new ArrayList<>();
        listaImagens = new ArrayList<>();

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
                        if (!listaNomes.isEmpty()) {
                            listaNomes.clear();
                            listaLinks.clear();
                            listaPrecos.clear();
                            listaImagens.clear();
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
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (autoCompleteNome.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Digite o nome do produto a ser pesquisado", Toast.LENGTH_SHORT).show();
                    } else {
                        if (estaConectadoInternet()) {
                            if (!listaNomes.isEmpty()) {
                                listaNomes.clear();
                                listaLinks.clear();
                                listaPrecos.clear();
                                listaImagens.clear();
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
                    return true;
                }
                return false;
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
                        if (!listaNomes.isEmpty()) {
                            listaNomes.clear();
                            listaLinks.clear();
                            listaPrecos.clear();
                            listaImagens.clear();
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
        tituloApp.setTypeface(ralewayLight);

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
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObj.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject produtoAtual = jsonArray.getJSONObject(i).getJSONObject("product");
                        //SE O NOME CURTO DO PRODUTO NÃO ESTÁ PRESENTE NO JSON
                        if (produtoAtual.isNull("productshortname")) {
                            listaNomes.add(produtoAtual.optString("productname"));
                        }
                        else {
                            listaNomes.add(produtoAtual.optString("productshortname"));
                        }

                        String precoProduto = produtoAtual.optString("pricemin");

                        //SE O PREÇO MÍNIMO DO PRODUTO ESTÁ PRESENTE NO JSON
                        if (produtoAtual.isNull("pricemin")) {
                            listaPrecos.add("Produto não disponível");
                        }
                        else {
                            listaPrecos.add("R$" + precoProduto);
                        }

                        //SE O PRODUTO NÃO POSSUI FOTO
                        if (produtoAtual.isNull("thumbnail")) {
                            listaImagens.add("assets://SemImagem.png");
                        }
                        else {
                            //VERIFICA SE O PRODUTO POSSUI IMAGEM PARA SER EXIBIDA
                            if (!produtoAtual.getJSONObject("thumbnail").getJSONArray("formats").isNull(1)) {
                                //FAZ O PARSE DA URL DA IMAGEM DE 300X300 FORNECIDA PELO BUSCAPE (POSIÇÃO '1' DO ARRAY 'FORMATS'
                                listaImagens.add(produtoAtual.getJSONObject("thumbnail").getJSONArray("formats").getJSONObject(1).getJSONObject("formats").optString("url"));
                            }
                            else {
                                listaImagens.add("assets://SemImagem.png");
                            }
                        }
                        //ADICIONA A URL PARA ACESSO DO PRODUTO NO SITE DO BUSCAPÉ (POSIÇÃO '0' DO ARRAY LINKS
                        listaLinks.add(produtoAtual.getJSONArray("links").getJSONObject(0).getJSONObject("link").optString("url"));
                    }
                } catch (JSONException e) {
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

            //INSTÂNCIA DO FRAGMENT 'LISTA DE PRODUTOS'
            ProdutosFragment produtosFragment = new ProdutosFragment();

            //BUNDLE RESPONSÁVEL POR ENVIAR AS LISTAS RESULTANTES DA PESQUISA DO PRODUTO AO FRAGMENT LISTA
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("listaNomes", listaNomes);
            bundle.putStringArrayList("listaPrecos", listaPrecos);
            bundle.putStringArrayList("listaImagens", listaImagens);
            bundle.putStringArrayList("listaLinks", listaLinks);

            produtosFragment.setArguments(bundle);

            //TROCA O FRAGMENT DA ACTIVITY PARA O FRAGMENT 'LISTA DE PRODUTOS'
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, produtosFragment);
            transaction.addToBackStack(null);
            transaction.commit();
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