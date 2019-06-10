package br.com.viavarejo.viavarejo.model;

/**
 *
 * @author leonardo costa - 08/06/2019
 */
public class CondicaoPagamentoModel {

    private Double valorEntrada;
    private Integer qtdeParcelas;

    public Double getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(Double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public Integer getQtdeParcelas() {
        return qtdeParcelas;
    }

    public void setQtdeParcelas(Integer qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

    @Override
    public String toString() {
        return "CondicaoPagamento{" + "valorEntrada=" + valorEntrada + ", qtdeParcelas=" + qtdeParcelas + '}';
    }

}
