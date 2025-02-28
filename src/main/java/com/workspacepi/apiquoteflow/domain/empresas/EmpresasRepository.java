package com.workspacepi.apiquoteflow.domain.empresas;


import java.util.List;
import java.util.UUID;

public interface EmpresasRepository {

//  Buscar todas as empresas
    List<Empresas> findAll();

//  Buscar empresa por ID
    Empresas findById(UUID id_empresa);

//  Buscar empresa pelo CNPJ
    Empresas findByCNPJ(String cnpj);

//  Buscar empresa pelo Email
    Empresas findByEmail(String email);

//  Inserção
    Boolean cadastrarEmpresa(Empresas empresas);

//  Modificação
    Boolean modificarEmpresa(Empresas empresas);

//  Delete
    Boolean deleteEmpresaById(UUID id_empresa);

}
