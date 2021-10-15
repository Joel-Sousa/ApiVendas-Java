package io.github.vendas.exception;

public class PedidoNaoEncontradoException extends RuntimeException{

	public PedidoNaoEncontradoException() {
		super("Pedido nao encontrado!");
	}
}
