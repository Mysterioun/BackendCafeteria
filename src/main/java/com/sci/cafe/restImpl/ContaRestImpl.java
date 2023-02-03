package com.sci.cafe.restImpl;

import com.sci.cafe.model.Conta;
import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.rest.ContaRest;
import com.sci.cafe.service.ContaService;
import com.sci.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ContaRestImpl implements ContaRest {
    @Autowired
    ContaService contaService;

    @Override
    public ResponseEntity<String> gerarReport(Map<String, Object> requestMap) {
        try {
            return contaService.gerarReport(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Conta>> getContas() {
        try {
            return contaService.getContas();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return contaService.getPdf(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> excluirConta(Integer id) {
        try {
            return contaService.excluirConta(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
