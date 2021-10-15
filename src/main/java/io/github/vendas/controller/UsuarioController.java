package io.github.vendas.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.vendas.dto.CredenciaisDTO;
import io.github.vendas.dto.TokenDTO;
import io.github.vendas.entity.Usuario;
import io.github.vendas.exception.SenhaInvalidaException;
import io.github.vendas.jwt.JwtService;
import io.github.vendas.service.imp.UsuarioServiceImp;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioServiceImp usuarioService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario salvar(@Valid @RequestBody Usuario usuario) {
		String senhaCriptografada = passwordEncoder.encode(usuario.getPass());
				usuario.setPass(senhaCriptografada);
		return usuarioService.salvar(usuario);
	}

	@PostMapping("/auth")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Usuario usuario = Usuario.builder()
										.login(credenciais.getLogin())
										.pass(credenciais.getPass()).build();
			UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
			String token = jwtService.gerarToken(usuario);
			
			return new TokenDTO(usuario.getLogin(), token);
		}catch(UsernameNotFoundException | SenhaInvalidaException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
}
