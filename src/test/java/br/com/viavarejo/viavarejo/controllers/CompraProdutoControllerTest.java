package br.com.viavarejo.viavarejo.controllers;

import br.com.viavarejo.viavarejo.model.CompraModel;
import br.com.viavarejo.viavarejo.model.CondicaoPagamentoModel;
import br.com.viavarejo.viavarejo.model.ProdutoModel;
import br.com.viavarejo.viavarejo.model.SelicModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author leonardo
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CompraProdutoControllerTest {

    @Autowired
    private MockMvc mvc;
    @Value("${selic_url}")
    String selicUrl;
    @Autowired
    RestTemplate restTemplate;

    public static final String EFETUA_COMPRA_URL = "/api/compra";
    private static final Long CODIGO_PRODUTO = 123l;
    private static final String NOME_PRODUTO = "TELEVISÃO SAMSUNG";
    private static final Double VALOR_PRODUTO = 9999.99d;
    private static final Double VALOR_ENTRADA = 999.99d;
    private static final Integer QUANTIDADE_PARCELAS = 9;
    private static final Double VALOR_PARCELAS = 1103.5d;

    @Test
    public void testEfetuarCompra() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(EFETUA_COMPRA_URL)
                .content(this.obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroParcela").value(QUANTIDADE_PARCELAS))
                .andExpect(jsonPath("$.valor").value(calcValor(VALOR_PRODUTO, taxaJuros(), QUANTIDADE_PARCELAS, VALOR_ENTRADA)))
                .andExpect(jsonPath("$.taxaJurosAoMes").value(BigDecimal.valueOf(taxaJuros() * 100)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue()));
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        CompraModel compra = new CompraModel();
        ProdutoModel produto = new ProdutoModel();
        produto.setCodigo(CODIGO_PRODUTO);
        produto.setNome(NOME_PRODUTO);
        produto.setValor(VALOR_PRODUTO);
        CondicaoPagamentoModel condPagModel = new CondicaoPagamentoModel();
        condPagModel.setQtdeParcelas(QUANTIDADE_PARCELAS);
        condPagModel.setValorEntrada(VALOR_ENTRADA);
        compra.setProduto(produto);
        compra.setCondicaoPagamento(condPagModel);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(compra);
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

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public Double calcValor(Double valor, Double taxa, Integer parcela, Double valorEntrada) {
        valor = valor - valorEntrada;
        Double juros = valor * (taxa);
        Double total = (valor / parcela) + juros;
        return BigDecimal.valueOf(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
