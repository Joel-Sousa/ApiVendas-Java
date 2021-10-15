package io.github.vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.vendas.entity.Produto;

public interface IProdutos extends JpaRepository<Produto, Integer> {

	
}
