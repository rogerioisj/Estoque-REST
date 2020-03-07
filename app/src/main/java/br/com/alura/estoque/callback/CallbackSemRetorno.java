package br.com.alura.estoque.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static br.com.alura.estoque.callback.MensangensCallback.FALHA_COMUNICAÇÃO;
import static br.com.alura.estoque.callback.MensangensCallback.RESPOSTA_NÃO_SUCEDIDA;


public class CallbackSemRetorno implements Callback<Void> {

    private final RespostaCallback callback;

    public CallbackSemRetorno(RespostaCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {

        if (response.isSuccessful()) {
            callback.quandoSucesso();
        } else {
            callback.quandoFalha(RESPOSTA_NÃO_SUCEDIDA);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.quandoFalha(FALHA_COMUNICAÇÃO + t.getMessage());
    }

    public interface RespostaCallback {
        void quandoSucesso();

        void quandoFalha(String erro);
    }
}
