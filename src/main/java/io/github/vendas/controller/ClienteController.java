package io.github.vendas.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.vendas.entity.Cliente;
import io.github.vendas.repository.IClientes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/clientes")
@Api("Api Clientes")
public class ClienteController {
	
	private IClientes clienteRepository;
	
	public ClienteController(IClientes clienteRepository){
		this.clienteRepository = clienteRepository;
	}
	
	@GetMapping("{id}")
	@ApiOperation("Obter detalhes de um cliente")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cliente encontrado"),
		@ApiResponse(code = 404, message = "Cliente nao encontrado do id informado")
	})
	public Cliente getById(@PathVariable @ApiParam("Id do cliente") Integer id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
		
	}
	
	@ApiOperation("Obter detalhes de um cliente")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
		@ApiResponse(code = 400, message = "Erro de validacao")
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Cliente save(@RequestBody @Valid Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("{id}")
	public void delete(@PathVariable Integer id) {
		clienteRepository.findById(id)
		.map(cliente -> {
			clienteRepository.delete(cliente);
			return cliente;
		})
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("{id}")
	public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
		clienteRepository.findById(id).map(e -> {
			cliente.setId(e.getId());
			clienteRepository.save(cliente);
			return e;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encotrado")); 	
	}
	
	@GetMapping
	public List<Cliente> find(Cliente filtro) {
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		
		Example example = Example.of(filtro, matcher);
		return clienteRepository.findAll(example);
	}
}





