package io.github.vendas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.vendas.ApiErrors;
import io.github.vendas.exception.PedidoNaoEncontradoException;
import io.github.vendas.exception.RegraNegocioException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
 
	@ExceptionHandler(RegraNegocioException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleRegraNegocioException(RegraNegocioException e) {
		String mensagemError = e.getMessage();
		return new ApiErrors(mensagemError);
	}

	@ExceptionHandler(PedidoNaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrors handlePedidoNotFoundException(PedidoNaoEncontradoException e) {
		return new ApiErrors(e.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getAllErrors()
				.stream().map(e -> e.getDefaultMessage())
				.collect(Collectors.toList());
		return new ApiErrors(errors);
	}
}
