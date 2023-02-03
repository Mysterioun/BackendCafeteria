package com.sci.cafe.wrapper;

import lombok.Data;

@Data
public class ProdutoWrapper {


    Integer id;

    String nome;

    String descricao;

    Integer preco;

    String status;

    Integer categoriaId;

    String categoriaNome;

    public ProdutoWrapper(){

    }


    public ProdutoWrapper(Integer id, String nome, String descricao, Integer preco, String status,
                          Integer categoriaId, String categoriaNome){

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.status = status;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;

    }

    public ProdutoWrapper(Integer id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public ProdutoWrapper(Integer id, String nome, String descricao, Integer preco){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }
}
