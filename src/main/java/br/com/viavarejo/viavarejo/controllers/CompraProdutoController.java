package br.com.viavarejo.viavarejo.controllers;

import br.com.viavarejo.viavarejo.model.CompraModel;
import br.com.viavarejo.viavarejo.model.ListaParcelasModel;
import br.com.viavarejo.viavarejo.model.SelicModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author leonardo
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Validated
public class CompraProdutoController {

    @Value("${selic_url}")
    String selicUrl;
    @Autowired
    RestTemplate restTemplate;

    /**
     * Retorna Parcelas a partir de uma compra.
     *
     * @param compra
     * @return ResponseEntity<Response<ListaParcelasModel>>
     */
    @PostMapping(value = "/compra", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> compra(@Valid @RequestBody CompraModel compra) {
        ListaParcelasModel parcelas = new ListaParcelasModel();
        if (compra.getCondicaoPagamento().getQtdeParcelas() <= 6) {
            parcelas.setNumeroParcela(compra.getCondicaoPagamento().getQtdeParcelas());
            parcelas.setTaxaJurosAoMes(0d);
            parcelas.setValor(calcValor(compra.getProduto().getValor(), 0d, compra.getCondicaoPagamento().getQtdeParcelas(), compra.getCondicaoPagamento().getValorEntrada()));
        } else {
            Double tx = taxaJuros();
            parcelas.setNumeroParcela(compra.getCondicaoPagamento().getQtdeParcelas());
            parcelas.setTaxaJurosAoMes(BigDecimal.valueOf(tx * 100)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue());
            parcelas.setValor(calcValor(compra.getProduto().getValor(), tx, compra.getCondicaoPagamento().getQtdeParcelas(), compra.getCondicaoPagamento().getValorEntrada()));
        }
        return new ResponseEntity<>(parcelas, HttpStatus.OK);
    }

    public Double taxaJuros() {
        Date dataFin = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFinal = dateFormat.format(dataFin);
        Long mesTimeStamp = dataFin.getTime() - 2629743000l;
        Date dataIni = new Date(mesTimeStamp);
        String dataInicial = dateFormat.format(dataIni);
        selicUrl = selicUrl + "&dataInicial=" + dataInicial + "&dataFinal=" + dataFinal;
        ResponseEntity<List<SelicModel>> selicResponse
                = restTemplate.exchange(selicUrl,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<SelicModel>>() {
                });
        Double tx = 0d;
        Integer days = 0;
        if (selicResponse != null && selicResponse.getBody() != null) {
            for (SelicModel selicModel : selicResponse.getBody()) {
                if (isNumeric(selicModel.getValor())) {
                    tx += Double.parseDouble(selicModel.getValor());
                    days++;
                }
            }
        }
        return tx / days;
    }

    public Double calcValor(Double valor, Double taxa, Integer parcela, Double valorEntrada) {
        valor = valor - valorEntrada;
        Double juros = valor * (taxa);
        Double total = (valor / parcela) + juros;
        return BigDecimal.valueOf(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
