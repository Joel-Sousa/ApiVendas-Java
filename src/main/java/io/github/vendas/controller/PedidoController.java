package io.github.vendas.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.vendas.dto.AtualizacaoStatusPedidoDTO;
import io.github.vendas.dto.InformacaoItemPedidoDTO;
import io.github.vendas.dto.InformacaoPedidoDTO;
import io.github.vendas.dto.PedidoDTO;
import io.github.vendas.entity.ItemPedido;
import io.github.vendas.entity.Pedido;
import io.github.vendas.enums.StatusPedido;
import io.github.vendas.service.IPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private IPedidoService service;
	
	public PedidoController(IPedidoService service){
		this.service = service;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer save(@RequestBody @Valid PedidoDTO dto) {
		Pedido pedido = service.salvar(dto);
		return pedido.getId();
	}
	
	@GetMapping("{id}")
	public InformacaoPedidoDTO getById(@PathVariable Integer id) {
		return service.obterPedidoCompleto(id).map(e -> converter(e))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado"));
	}
	
	private InformacaoPedidoDTO converter(Pedido pedido) {
		return InformacaoPedidoDTO.builder()
		.codigo(pedido.getId())
		.dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
		.cpf(pedido.getCliente().getCpf())
		.nomeCliente(pedido.getCliente().getNome())
		.total(pedido.getTotal())
		.status(pedido.getStatus().name())
		.items(converter(pedido.getItens()))
		.build();
	}
	
	private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){
		if(CollectionUtils.isEmpty(itens))
			return Collections.emptyList();
		
		return itens.stream().map(e -> InformacaoItemPedidoDTO
				.builder().descricaoProduto(e.getProduto().getDescricao())
				.precoUnitario(e.getProduto().getPreco())
				.quantidade(e.getQuantidade())
				.build()
			).collect(Collectors.toList());
		
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping("{id}")
	public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDTO dto) {
		
		String novoStatus = dto.getNovoStatus();
		service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
	}
	
}
