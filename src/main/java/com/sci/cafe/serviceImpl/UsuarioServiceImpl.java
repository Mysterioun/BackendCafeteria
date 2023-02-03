package com.sci.cafe.serviceImpl;

import com.google.common.base.Strings;
import com.sci.cafe.JWT.CustomerUserDatailsService;
import com.sci.cafe.JWT.JwtFilter;
import com.sci.cafe.JWT.JwtUtil;
import com.sci.cafe.model.Usuario;
import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.dao.UsuarioDao;
import com.sci.cafe.service.UsuarioService;
import com.sci.cafe.utils.CafeUtils;
import com.sci.cafe.utils.EmailUtils;
import com.sci.cafe.wrapper.UsuarioWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioDao usuarioDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDatailsService customerUserDatailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> cadastrar(Map<String, String> requestMap) {
        log.info("Dentro cadastro {}", requestMap);

        try {
            if (validadeSignUpMap(requestMap)) {

                Usuario usuario = usuarioDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(usuario)) {

                    usuarioDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Cadastrado com Sucesso.", HttpStatus.OK);

                } else {
                    return CafeUtils.getResponseEntity("Email ja existe.", HttpStatus.BAD_REQUEST);
                }

            } else {
                return CafeUtils.getResponseEntity(CafeConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validadeSignUpMap(Map<String,String> requestMap){
        if (requestMap.containsKey("nome") && requestMap.containsKey("numeroContato")
                && requestMap.containsKey("email") && requestMap.containsKey("senha")){
            return true;
        }
        return false;
    }


    private Usuario getUserFromMap(Map<String, String> requestMap){

        Usuario usuario = new Usuario();
        usuario.setNome(requestMap.get("nome"));
        usuario.setNumeroContato(requestMap.get("numeroContato"));
        usuario.setEmail(requestMap.get("email"));
        usuario.setSenha(requestMap.get("senha"));
        usuario.setStatus("false");
        usuario.setRole("usuario");
        return usuario;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {

        log.info("Inside Login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("senha"))
            );
            if(auth.isAuthenticated()){
                if(customerUserDatailsService.getUserDatail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUserDatailsService.getUserDatail().getEmail(),
                                    customerUserDatailsService.getUserDatail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Espere pelo Admin aprovar."+"\"}",
                            HttpStatus.BAD_REQUEST );
                }
            }

        }catch (Exception ex){
            log.info("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Dados Errados."+"\"}",
                HttpStatus.BAD_REQUEST );
    }

    @Override
    public ResponseEntity<List<UsuarioWrapper>> obterTodosUsuarios() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(usuarioDao.obterTodosUsuarios(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editar(Map<String, String> requestMap) {
        try{

            if(jwtFilter.isAdmin()){
               Optional<Usuario> optional = usuarioDao.findById(Integer.parseInt(requestMap.get("id")));
               if(!optional.isEmpty()){
                    usuarioDao.editarStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), usuarioDao.obterTodosAdmins());
                    return CafeUtils.getResponseEntity("Status do usuario editado com sucesso.", HttpStatus.OK);

               }else{
                   return CafeUtils.getResponseEntity("Id do usuario não existe", HttpStatus.OK);
               }
            }else{
                return CafeUtils.getResponseEntity(CafeConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String usuario, List<String> todosAdmins) {
        todosAdmins.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Conta Aprovada", "USUARIO:- "+usuario+ "\n foi aprovada pelo \n ADMIN:-"+ jwtFilter.getCurrentUser(), todosAdmins);
        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Conta Desativada", "USUARIO:- "+usuario+ "\n foi desativado pelo \n ADMIN:-"+ jwtFilter.getCurrentUser(), todosAdmins);
        }

    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> mudarSenha(Map<String, String> requestMap) {
        try {
            Usuario usuarioObj = usuarioDao.findByEmail(jwtFilter.getCurrentUser());
            if(!usuarioObj.equals(null)) {
                if (usuarioObj.getSenha().equals(requestMap.get("senhaAntiga"))) {
                    usuarioObj.setSenha(requestMap.get("senhaNova"));
                    usuarioDao.save(usuarioObj);
                    return CafeUtils.getResponseEntity("Senha editada com Sucesso", HttpStatus.OK);

                }
                return CafeUtils.getResponseEntity("Senha antiga Incorreta", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> esqueceuSenha(Map<String, String> requestMap) {
        try{
            Usuario usuario = usuarioDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(usuario) && !Strings.isNullOrEmpty(usuario.getEmail()))

                emailUtils.esqueceuEmail(usuario.getEmail(), "Dados da Cafeteria", usuario.getSenha());
                return CafeUtils.getResponseEntity("Verifique seu email para as Credenciais", HttpStatus.OK);



        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
