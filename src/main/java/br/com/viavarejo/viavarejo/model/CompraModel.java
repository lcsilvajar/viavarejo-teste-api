package br.com.viavarejo.viavarejo.model;

import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author leonardo costa - 08/06/2019
 */
@Validated
public class CompraModel {

    @NotNull
    private ProdutoModel produto;
    @NotNull
    private CondicaoPagamentoModel condicaoPagamento;

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

    public CondicaoPagamentoModel getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamentoModel condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    @Override
    public String toString() {
        return "Compra{" + "produto=" + produto + ", condicaoPagamento=" + condicaoPagamento + '}';
    }

}
