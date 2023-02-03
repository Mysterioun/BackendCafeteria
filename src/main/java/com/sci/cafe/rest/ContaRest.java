package com.sci.cafe.rest;

import com.sci.cafe.model.Conta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/conta")
public interface ContaRest {

    @PostMapping(path = "/gerarReport")
    ResponseEntity<String> gerarReport (@RequestBody Map<String, Object> requestMap);

    @GetMapping(path = "/getContas")
    ResponseEntity<List<Conta>> getContas();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);

    @PostMapping(path = "/excluir/{id}")
    ResponseEntity<String> excluirConta(@PathVariable Integer id);
}
