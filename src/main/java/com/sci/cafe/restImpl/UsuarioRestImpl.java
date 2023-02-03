package com.sci.cafe.restImpl;

import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.rest.UsuarioRest;
import com.sci.cafe.service.UsuarioService;
import com.sci.cafe.utils.CafeUtils;
import com.sci.cafe.wrapper.UsuarioWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UsuarioRestImpl implements UsuarioRest {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public ResponseEntity<String> cadastrar(Map<String, String> requestMap) {
        try {

            return usuarioService.cadastrar(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return usuarioService.login(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<UsuarioWrapper>> obterTodosUsuarios() {
        try{
            return usuarioService.obterTodosUsuarios();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UsuarioWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editar(Map<String, String> requestMap) {
        try {
            return usuarioService.editar(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {

            return usuarioService.checkToken();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> mudarSenha(Map<String, String> requestMap) {
        try {

            return usuarioService.mudarSenha(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> esqueceuSenha(Map<String, String> requestMap) {
        try{
            return usuarioService.esqueceuSenha(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


