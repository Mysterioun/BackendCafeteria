package com.sci.cafe.dao;

import com.sci.cafe.model.Usuario;
import com.sci.cafe.wrapper.UsuarioWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

    Usuario findByEmailId(@Param("email")String email);

    List<UsuarioWrapper> obterTodosUsuarios();


    List<String> obterTodosAdmins();

    @Transactional
    @Modifying
    Integer editarStatus(@Param("status") String status, @Param("id") Integer id);

    Usuario findByEmail(String email);

}
