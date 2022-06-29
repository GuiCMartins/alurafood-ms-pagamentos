package br.com.alurafood.pagamentos.repositories;

import br.com.alurafood.pagamentos.models.JPAPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<JPAPagamento, Long> {
}
