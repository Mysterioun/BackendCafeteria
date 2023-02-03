package com.sci.cafe.rest;


import com.sci.cafe.wrapper.UsuarioWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/usuario")
public interface UsuarioRest {

    @PostMapping(path = "/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody(required = true)Map<String, String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true)Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<UsuarioWrapper>> obterTodosUsuarios();


    @PostMapping(path = "/editar")
    public ResponseEntity<String> editar(@RequestBody(required = true)Map<String, String> requestMap);

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path = "/mudarSenha")
    ResponseEntity<String> mudarSenha(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/esqueceuSenha")
    ResponseEntity<String> esqueceuSenha(@RequestBody Map<String, String> requestMap);

}
