package com.sci.cafe.JWT;

import com.sci.cafe.dao.UsuarioDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUserDatailsService implements UserDetailsService {

    @Autowired
    UsuarioDao usuarioDao;

    private com.sci.cafe.model.Usuario userDatail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Inside loadUserByUsername{}", username);

        userDatail = usuarioDao.findByEmailId(username);
        if (!Objects.isNull(userDatail)){
            return new User(userDatail.getEmail(), userDatail.getSenha(), new ArrayList<>());
        }

        else {
            throw new UsernameNotFoundException("Usuario não encontrado.");
        }
    }

    public com.sci.cafe.model.Usuario getUserDatail(){
        return userDatail;
    }
}
