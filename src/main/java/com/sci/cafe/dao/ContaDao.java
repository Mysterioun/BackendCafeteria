package com.sci.cafe.dao;

import com.sci.cafe.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContaDao extends JpaRepository<Conta, Integer> {

    List<Conta> getTodasContas();

    List<Conta> getContaPeloUsername(@Param("username") String username);
}
