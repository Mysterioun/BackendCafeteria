package com.sci.cafe.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Conta.getTodasContas", query = "select c from Conta c order by c.id desc")
@NamedQuery(name = "Conta.getContaPeloUsername", query = "select c from Conta c where c.criadoPor=:username order by c.id desc")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "conta")
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "numeroContato")
    private String numeroContato;

    @Column(name = "metodoPagamento")
    private String metodoPagamento;

    @Column(name = "total")
    private Integer total;

    @Column(name = "produtoDetalhes", columnDefinition = "json")
    private String produtoDetalhes;

    @Column(name = "criadoPor")
    private String criadoPor;

}
