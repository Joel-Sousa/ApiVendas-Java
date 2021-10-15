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

import io.github.vendas.entity.Produto;
import io.github.vendas.repository.IProdutos;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private IProdutos produtosRepository;
	
	public ProdutoController(IProdutos produtosRepository){
		this.produtosRepository = produtosRepository;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Produto save(@RequestBody @Valid Produto produto) {
		return produtosRepository.save(produto);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("{id}")
	public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
		produtosRepository.findById(id).map(e -> {
			produto.setId(e.getId());
			produtosRepository.save(produto);
			return e;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("{id}")
	public void delete (@PathVariable Integer id) {
		produtosRepository.findById(id)
		.map(e ->{
			produtosRepository.delete(e);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
	}
	
	@GetMapping("{id}")
	public Produto getById(@PathVariable Integer id) {
		return produtosRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
	}
	
	
	@GetMapping
	public List<Produto> find(Produto filtro){
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		
		Example example = Example.of(filtro, matcher);
		return produtosRepository.findAll(example);
	}
	
}










