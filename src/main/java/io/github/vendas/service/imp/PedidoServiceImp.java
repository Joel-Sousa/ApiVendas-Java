package io.github.vendas.service.imp;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.vendas.dto.ItemPedidoDTO;
import io.github.vendas.dto.PedidoDTO;
import io.github.vendas.entity.Cliente;
import io.github.vendas.entity.ItemPedido;
import io.github.vendas.entity.Pedido;
import io.github.vendas.entity.Produto;
import io.github.vendas.enums.StatusPedido;
import io.github.vendas.exception.PedidoNaoEncontradoException;
import io.github.vendas.exception.RegraNegocioException;
import io.github.vendas.repository.IClientes;
import io.github.vendas.repository.IItemsPedido;
import io.github.vendas.repository.IPedidos;
import io.github.vendas.repository.IProdutos;
import io.github.vendas.service.IPedidoService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class PedidoServiceImp implements IPedidoService{

	//Injecao de dependencia feito com o 	@RequiredArgsConstructor da lib do Lombok
	private final IPedidos pedidosRepository;
	private final IClientes clientesRepository;
	private final IProdutos produtosRepository;
	private final IItemsPedido itemsPedidoRepository;

	@Transactional
	@Override
	public Pedido salvar(PedidoDTO dto) {
		Integer idCliente = dto.getCliente();
		Cliente cliente = clientesRepository.findById(idCliente)
				.orElseThrow(() -> new RegraNegocioException("Codigo cliente invalido"));
		
		Pedido pedido = new Pedido();
		pedido.setTotal(dto.getTotal());
		pedido.setDataPedido(LocalDate.now());
		pedido.setCliente(cliente);
		pedido.setStatus(StatusPedido.REALIZADO);
		
		List<ItemPedido> itemsList = converterItems(pedido, dto.getItems());
		pedidosRepository.save(pedido);
		itemsPedidoRepository.saveAll(itemsList);
		pedido.setItens(itemsList);
		return pedido;
	} 
	
	 
	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		return pedidosRepository.findByIdFetchItens(id);
	}
	
	private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
		if(items.isEmpty()) 
			throw new RegraNegocioException("Nao e posssivel realizar um pedido sem items.");
		
		return items.stream().map(e -> {
			Integer idProduto = e.getProduto();
			Produto produto = produtosRepository.findById(idProduto)
			.orElseThrow(() -> new RegraNegocioException("Codigo de produto invalido: " + idProduto));
			
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setQuantidade(e.getQuantidade());
			itemPedido.setPedido(pedido);
			itemPedido.setProduto(produto);
			return itemPedido;
		}).collect(Collectors.toList());
	}

	@Transactional 
	@Override
	public void atualizarStatus(Integer id, StatusPedido statusPedido) {
		pedidosRepository.findById(id).map( e -> {
			e.setStatus(statusPedido);
			return pedidosRepository.save(e);
		}).orElseThrow(() -> new PedidoNaoEncontradoException());
	}
}
