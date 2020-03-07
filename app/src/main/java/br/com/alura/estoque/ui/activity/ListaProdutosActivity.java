package br.com.alura.estoque.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.com.alura.estoque.R;
import br.com.alura.estoque.database.EstoqueDatabase;
import br.com.alura.estoque.database.dao.ProdutoDAO;
import br.com.alura.estoque.model.Produto;
import br.com.alura.estoque.repository.ProdutoRepository;
import br.com.alura.estoque.ui.dialog.EditaProdutoDialog;
import br.com.alura.estoque.ui.dialog.SalvaProdutoDialog;
import br.com.alura.estoque.ui.recyclerview.adapter.ListaProdutosAdapter;

import static br.com.alura.estoque.ui.activity.ListaProdutosConstantes.ERRO_CARREGA_PRODUTOS;
import static br.com.alura.estoque.ui.activity.ListaProdutosConstantes.ERRO_EDITA_PRODUTO;
import static br.com.alura.estoque.ui.activity.ListaProdutosConstantes.ERRO_REMOVE_PRODUTO;
import static br.com.alura.estoque.ui.activity.ListaProdutosConstantes.ERRO_SALVA_PRODUTO;
import static br.com.alura.estoque.ui.activity.ListaProdutosConstantes.TITULO_APPBAR;

public class ListaProdutosActivity extends AppCompatActivity {


    private ListaProdutosAdapter adapter;
    private ProdutoDAO dao;
    private ProdutoRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        setTitle(TITULO_APPBAR);

        configuraListaProdutos();
        configuraFabSalvaProduto();

        EstoqueDatabase db = EstoqueDatabase.getInstance(this);
        dao = db.getProdutoDAO();

        repository = new ProdutoRepository(dao);
        repository.buscaProdutos(new ProdutoRepository.DadosCarregadosCallback<List<Produto>>() {
            @Override
            public void quandoSucesso(List<Produto> produtosNovos) {
                adapter.atualiza(produtosNovos);
            }

            @Override
            public void quandoFalha(String erro) {
                Toast.makeText(ListaProdutosActivity.this, ERRO_CARREGA_PRODUTOS,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configuraListaProdutos() {
        RecyclerView listaProdutos = findViewById(R.id.activity_lista_produtos_lista);
        adapter = new ListaProdutosAdapter(this, this::abreFormularioEditaProduto);
        listaProdutos.setAdapter(adapter);
        adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void remove(int posicao,
                        Produto produtoRemovido) {
        repository.remove(produtoRemovido, new ProdutoRepository.DadosCarregadosCallback<Void>() {
            @Override
            public void quandoSucesso(Void resultado) {
                adapter.remove(posicao);
            }

            @Override
            public void quandoFalha(String erro) {
                Toast.makeText(ListaProdutosActivity.this, ERRO_REMOVE_PRODUTO,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configuraFabSalvaProduto() {
        FloatingActionButton fabAdicionaProduto = findViewById(R.id.activity_lista_produtos_fab_adiciona_produto);
        fabAdicionaProduto.setOnClickListener(v -> abreFormularioSalvaProduto());
    }

    private void abreFormularioSalvaProduto() {
        new SalvaProdutoDialog(this, produtoCriado ->
                repository.salva(produtoCriado, new ProdutoRepository.DadosCarregadosCallback<Produto>() {
                    @Override
                    public void quandoSucesso(Produto produtoSalvo) {
                        adapter.adiciona(produtoSalvo);
                    }

                    @Override
                    public void quandoFalha(String erro) {
                        Toast.makeText(ListaProdutosActivity.this, ERRO_SALVA_PRODUTO,
                                Toast.LENGTH_SHORT).show();
                    }
                }))
                .mostra();
    }

    private void abreFormularioEditaProduto(int posicao, Produto produto) {
        new EditaProdutoDialog(this, produto,
                produtoCriado -> repository.edita(produtoCriado,
                        new ProdutoRepository.DadosCarregadosCallback<Produto>() {
                            @Override
                            public void quandoSucesso(Produto produtoEditado) {
                                adapter.edita(posicao, produtoEditado);
                            }

                            @Override
                            public void quandoFalha(String erro) {
                                Toast.makeText(ListaProdutosActivity.this
                                        , ERRO_EDITA_PRODUTO
                                        , Toast.LENGTH_SHORT).show();

                            }
                        }))
                .mostra();
    }


}