package io.github.vendas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.vendas.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	Optional<Usuario> findByLogin(String login);
}
