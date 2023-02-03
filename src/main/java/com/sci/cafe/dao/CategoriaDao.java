package com.sci.cafe.dao;

import com.sci.cafe.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaDao extends JpaRepository<Categoria, Integer> {

    List<Categoria> getTodasCategorias();

}
