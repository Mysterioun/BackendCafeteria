package com.sci.cafe.service;

import com.sci.cafe.wrapper.UsuarioWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

    ResponseEntity<String> cadastrar(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UsuarioWrapper>> obterTodosUsuarios();

    ResponseEntity<String> editar(Map<String, String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> mudarSenha(Map<String, String> requestMap);

    ResponseEntity<String> esqueceuSenha(Map<String, String> requestMap);


}
