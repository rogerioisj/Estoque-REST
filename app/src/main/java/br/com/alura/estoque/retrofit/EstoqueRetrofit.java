package br.com.alura.estoque.retrofit;

import br.com.alura.estoque.services.ProdutoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    public static final String URL_CLIENT = "http://192.168.15.228:8080/";
    private final ProdutoService produtoService;

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public EstoqueRetrofit() {
        OkHttpClient client = configuraClient();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_CLIENT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        produtoService = retrofit.create(ProdutoService.class);
    }

    private OkHttpClient configuraClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
