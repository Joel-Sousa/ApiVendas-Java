package io.github.vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.vendas.entity.ItemPedido;

public interface IItemsPedido extends JpaRepository<ItemPedido, Integer>{

	
}
