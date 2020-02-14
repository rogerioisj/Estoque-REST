package br.com.alura.estoque.retrofit;

import br.com.alura.estoque.services.ProdutoService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private final ProdutoService produtoService;

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public EstoqueRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        produtoService = retrofit.create(ProdutoService.class);
    }
}
