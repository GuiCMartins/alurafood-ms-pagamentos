package br.com.alurafood.pagamentos.Services;

import br.com.alurafood.pagamentos.dtos.DTOPagamento;
import br.com.alurafood.pagamentos.enums.Status;
import br.com.alurafood.pagamentos.http.PedidoClient;
import br.com.alurafood.pagamentos.models.JPAPagamento;
import br.com.alurafood.pagamentos.repositories.PagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient pedido;

    public Page<DTOPagamento> findAll(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, DTOPagamento.class));
    }

    public DTOPagamento findById(Long id) {
        JPAPagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(pagamento, DTOPagamento.class);
    }

    public DTOPagamento create(DTOPagamento dto) {
        JPAPagamento pagamento = modelMapper.map(dto, JPAPagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);

        return modelMapper.map(pagamento, DTOPagamento.class);
    }

    public DTOPagamento update(Long id, DTOPagamento dto) {
        JPAPagamento pagamento = modelMapper.map(dto, JPAPagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, DTOPagamento.class);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void confirm(Long id){
        Optional<JPAPagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedido.updatePagamento(pagamento.get().getPedidoId());
    }

    public void alterStatus(Long id) {
        Optional<JPAPagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());
    }
}
