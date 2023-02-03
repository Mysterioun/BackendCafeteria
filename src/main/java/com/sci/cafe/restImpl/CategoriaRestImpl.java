package com.sci.cafe.restImpl;

import com.sci.cafe.model.Categoria;
import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.rest.CategoriaRest;
import com.sci.cafe.service.CategoriaService;
import com.sci.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoriaRestImpl implements CategoriaRest {


    @Autowired
    CategoriaService categoriaService;


    @Override
    public ResponseEntity<String> addNovaCategoria(Map<String, String> requestMap) {
        try {

            return categoriaService.addNovaCategoria(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Categoria>> getTodasCategorias(String filterValue) {
        try {

            return categoriaService.getTodasCategorias(filterValue);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarCategoria(Map<String, String> requestMap) {
        try{
            return categoriaService.editarCategoria(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
