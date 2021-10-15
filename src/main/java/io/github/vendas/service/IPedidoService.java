package io.github.vendas.service;

import java.util.Optional;

import io.github.vendas.dto.PedidoDTO;
import io.github.vendas.entity.Pedido;
import io.github.vendas.enums.StatusPedido;

public interface IPedidoService {

	Pedido salvar(PedidoDTO dto);
		
	Optional<Pedido> obterPedidoCompleto(Integer id);
	
	void atualizarStatus(Integer id, StatusPedido statusPedido);
}
