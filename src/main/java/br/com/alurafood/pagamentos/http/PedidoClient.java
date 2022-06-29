package br.com.alurafood.pagamentos.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("pedidos-ms")
public interface PedidoClient {
    @PutMapping(value = "/pedidos/{id}/pago")
    void updatePagamento(@PathVariable Long id);
}
