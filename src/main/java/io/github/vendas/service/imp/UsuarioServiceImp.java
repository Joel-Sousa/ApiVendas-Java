package io.github.vendas.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.vendas.entity.Usuario;
import io.github.vendas.exception.SenhaInvalidaException;
import io.github.vendas.repository.UsuarioRepository;

@Service
public class UsuarioServiceImp implements UserDetailsService{
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getLogin());
		boolean senhasIguais = encoder.matches(usuario.getPass(), user.getPassword());
		if(senhasIguais)
			return user;
		
		throw new SenhaInvalidaException();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario =  usuarioRepository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario nao Encontrado na base de dados."));
		
		String[] roles = usuario.isAdmin() ? 
				new String[] {"ADMIN", "USER"} : new String[] {"USER"};
		
		return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getPass())
                .roles(roles)
                .build();
	}
}
