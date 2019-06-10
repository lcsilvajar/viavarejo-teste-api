package br.com.viavarejo.viavarejo.model;

/**
 *
 * @author leonardo costa - 08/06/2019
 */
public class ListaParcelasModel {

    private Integer numeroParcela;
    private Double valor;
    private Double taxaJurosAoMes;

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getTaxaJurosAoMes() {
        return taxaJurosAoMes;
    }

    public void setTaxaJurosAoMes(Double taxaJurosAoMes) {
        this.taxaJurosAoMes = taxaJurosAoMes;
    }

    @Override
    public String toString() {
        return "ListaParcelas{" + "numeroParcela=" + numeroParcela + ", valor=" + valor + ", taxaJurosAoMes=" + taxaJurosAoMes + '}';
    }

}
