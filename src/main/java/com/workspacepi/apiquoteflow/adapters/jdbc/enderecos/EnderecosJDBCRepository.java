// Nosso repositório de acesso a dados

package com.workspacepi.apiquoteflow.adapters.jdbc.enderecos;

import com.workspacepi.apiquoteflow.adapters.http.allErrors.ErrorHandler;
import com.workspacepi.apiquoteflow.domain.enderecos.Enderecos;
import com.workspacepi.apiquoteflow.domain.enderecos.EnderecosRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

import static com.workspacepi.apiquoteflow.adapters.jdbc.enderecos.EnderecosSqlExpressions.*;


// Nosso repositório que define os nossos métodos de query e de crud usando o JDBC

@Repository
public class EnderecosJDBCRepository implements EnderecosRepository {

    //  Um atributo para criar o nosso template do JDBC assim como o seu construtor

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EnderecosJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//     Logger cuida do envio das nossas exceptions específicas ao invés das exceptions padrões

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);


//  Função da RowMapper para aproveitamento de código
//  Essa função é usada para mapear o resultado de uma consulta SQL

    private RowMapper<Enderecos> createEnderecoRowMapper() {
            return (rs, rowNum) -> {
                String bairro_endereco = rs.getString("bairro");
                String cep_endereco = rs.getString("cep");
                String complemento_endereco = rs.getString("complemento");
                String localidade_endereco = rs.getString("localidade");
                String logradouro_endereco = rs.getString("logradouro");
                int numero_endereco = rs.getRow();
                String uf_endereco = rs.getString("uf");
                UUID id_empresa_endereco = UUID.fromString(rs.getString("id_empresa"));
                UUID id_endereco = UUID.fromString(rs.getString("id_endereco"));

                return new Enderecos(bairro_endereco, cep_endereco, complemento_endereco, localidade_endereco, logradouro_endereco, numero_endereco, uf_endereco, id_empresa_endereco, id_endereco);
        };
    }

// Função para mapeamento dos parâmetros para as consultas sql

    private MapSqlParameterSource parameterSource(Enderecos enderecos){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bairro", enderecos.getBairro());
        params.addValue("cep", enderecos.getCep());
        params.addValue("complemento", enderecos.getComplemento());
        params.addValue("localidade", enderecos.getLocalidade());
        params.addValue("logradouro", enderecos.getLogradouro());
        params.addValue("numero", enderecos.getNumero());
        params.addValue("uf", enderecos.getUf());
        params.addValue("id_empresa", enderecos.getId_empresa());
        params.addValue("id_endereco", enderecos.getId_endereco());
        return params;
    }

    @Override
    public List<Enderecos> findAll() {
        List<Enderecos> enderecos = List.of();
        try {
            enderecos = jdbcTemplate.query(sqlFindAllEnderecos(), createEnderecoRowMapper());
            return enderecos;

        } catch (Exception e) {
            LOGGER.error("Houve um erro ao consultar os endereços: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Enderecos findByEmpresa(UUID id_empresa) {
        List<Enderecos> enderecos;
        try {
            MapSqlParameterSource params = new MapSqlParameterSource("id_empresa", id_empresa);
            enderecos = jdbcTemplate.query(sqlFindAllEnderecos(), params, createEnderecoRowMapper());
            return enderecos.isEmpty() ? null : enderecos.get(0);

        } catch (Exception e) {
            LOGGER.error("Houve um erro ao consultar os endereços: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Enderecos findById(UUID id_endereco) {
        List<Enderecos> enderecos;
        try {
            MapSqlParameterSource params = new MapSqlParameterSource("id_endereco", id_endereco);
            enderecos = jdbcTemplate.query(sqlFindEnderecosById(), params, createEnderecoRowMapper());
            return enderecos.isEmpty() ? null : enderecos.get(0);
        } catch (Exception e) {
            LOGGER.error("Houve um erro ao consultar o endereco : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Boolean cadastrarEndereco(Enderecos enderecos) {
        try {
            MapSqlParameterSource params = parameterSource(enderecos);
            int numLinhasAfetadas = jdbcTemplate.update(sqlCadastrarEndereco(), params);
            return numLinhasAfetadas > 0;
        } catch (Exception e) {
            LOGGER.error("Houve um erro ao cadastrar o endereco: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean modificarEndereco(Enderecos enderecos) {
        try {
            MapSqlParameterSource params = parameterSource(enderecos);
            int numLinhasAfetadas = jdbcTemplate.update(sqlModificarEndereco(), params);
            return numLinhasAfetadas > 0;
        } catch (Exception e) {
            LOGGER.error("Houve um erro ao editar o endereço: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean deleteEnderecobyId(UUID id_endereco) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource("id_endereco", id_endereco);
            int numLinhasAfetadas = jdbcTemplate.update(sqlDeleteEnderecoById(), params);
            return numLinhasAfetadas == 1;
        } catch (Exception e) {
            LOGGER.error("Houve um erro ao deletar o endereco: " + id_endereco + ". \n" + e.getMessage());
            throw e;
        }
    }
}
