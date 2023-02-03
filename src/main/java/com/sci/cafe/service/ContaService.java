package com.sci.cafe.service;

import com.sci.cafe.model.Conta;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ContaService {
    ResponseEntity<String> gerarReport(Map<String, Object> requestMap);

    ResponseEntity<List<Conta>> getContas();

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    ResponseEntity<String> excluirConta(Integer id);
}
