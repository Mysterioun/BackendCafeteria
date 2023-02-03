package com.sci.cafe.serviceImpl;

import com.google.common.base.Strings;
import com.sci.cafe.JWT.JwtFilter;
import com.sci.cafe.model.Categoria;
import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.dao.CategoriaDao;
import com.sci.cafe.service.CategoriaService;
import com.sci.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    CategoriaDao categoriaDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNovaCategoria(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validarCategoriaMap(requestMap, false)){
                    categoriaDao.save(getCategoriaFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Categoria Adicionada com Sucesso", HttpStatus.OK);
                }

            }else{
                return CafeUtils.getResponseEntity(CafeConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validarCategoriaMap(Map<String, String> requestMap, boolean validarId) {
        if(requestMap.containsKey("nome")){
            if(requestMap.containsKey("id") && validarId){
                return true;
            } else if (!validarId) {
                return true;
            }
        }
        return false;
    }

    private Categoria getCategoriaFromMap(Map<String, String> requestMap, Boolean isAdd){
        Categoria categoria = new Categoria();

        if(isAdd){
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNome(requestMap.get("nome"));
        return categoria;
    }

    @Override
    public ResponseEntity<List<Categoria>> getTodasCategorias(String filterValue) {
        try {
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Dentro de: ");
                return new ResponseEntity<List<Categoria>>(categoriaDao.getTodasCategorias(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoriaDao.findAll(), HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Categoria>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarCategoria(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validarCategoriaMap(requestMap, true)){
                    Optional optional = categoriaDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        categoriaDao.save(getCategoriaFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Categoria editada com sucesso", HttpStatus.OK);

                    }else{
                        return CafeUtils.getResponseEntity("Categoria não existe", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);

            }else {
                return CafeUtils.getResponseEntity(CafeConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
