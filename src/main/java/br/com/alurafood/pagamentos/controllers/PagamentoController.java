package br.com.alurafood.pagamentos.controllers;

import br.com.alurafood.pagamentos.Services.PagamentoService;
import br.com.alurafood.pagamentos.dtos.DTOPagamento;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public Page<DTOPagamento> findAll(@PageableDefault(size = 10) Pageable paginacao) {
        return service.findAll(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTOPagamento> findById(@PathVariable @NotNull Long id) {
        DTOPagamento dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DTOPagamento> create(@RequestBody @Valid DTOPagamento dto, UriComponentsBuilder uriBuilder) {
        DTOPagamento pagamento = service.create(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTOPagamento> update(@PathVariable @NotNull Long id, @RequestBody @Valid DTOPagamento dto) {
        DTOPagamento atualizado = service.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DTOPagamento> delete(@PathVariable @NotNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    @CircuitBreaker(name = "updatePagamento", fallbackMethod = "authorizedPayment")
    public void confirm(@PathVariable @NotNull Long id){
        service.confirm(id);
    }

    public void authorizedPayment(Long id, Exception e){
        service.alterStatus(id);
    }
}
