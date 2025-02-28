package com.workspacepi.apiquoteflow.adapters.jdbc.cotacoes.destinatarios;

import com.workspacepi.apiquoteflow.adapters.http.allErrors.ErrorHandler;
import com.workspacepi.apiquoteflow.domain.cotacoes.destinatarios.Destinatarios;
import com.workspacepi.apiquoteflow.domain.cotacoes.destinatarios.DestinatariosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.workspacepi.apiquoteflow.adapters.jdbc.cotacoes.destinatarios.DestinatariosSqlExpressions.*;
import static com.workspacepi.apiquoteflow.adapters.jdbc.cotacoes.destinatarios.DestinatariosSqlExpressions.sqlInserirDestinatario;

@Repository
public class DestinatariosJDBCRepository implements DestinatariosRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DestinatariosJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private RowMapper<Destinatarios> createDestinatariosRowMapper() {
        return(rs, rowNum) -> {
            UUID id =  UUID.fromString(rs.getString("id"));
            Timestamp data_envio = rs.getTimestamp("data_envio");
            UUID id_cotacao = UUID.fromString(rs.getString("id_cotacao"));
            UUID id_destinatario = UUID.fromString(rs.getString("id_destinatario"));

            return new Destinatarios(id, data_envio, id_cotacao, id_destinatario);
        };
    }

    private MapSqlParameterSource parameterSource(Destinatarios destinatarios) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", destinatarios.getId());
        params.addValue("data_envio", destinatarios.getData_envio());
        params.addValue("id_cotacao", destinatarios.getId_cotacao());
        params.addValue("id_destinatario", destinatarios.getId_destinatario());

        return params;
    }

    @Override
    public List<Destinatarios> findAllDestinatariosByCotacao(UUID id_cotacao) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id_cotacao", id_cotacao);
            return jdbcTemplate.query(sqlFindAllDestinatariosByCotacao(), params, createDestinatariosRowMapper());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Destinatarios findEmpresaDestinatariaByCotacaoAndId(UUID id_cotacao, UUID id_destinatario) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id_cotacao", id_cotacao);
            params.addValue("id_destinatario", id_destinatario);

            List<Destinatarios> destinatario = jdbcTemplate.query(sqlFindEmpresaDestinatariaByCotacaoAndId(), params, createDestinatariosRowMapper());
            return destinatario.isEmpty() ? null : destinatario.get(0);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Boolean inserirDestinatario(List<Destinatarios> destinatarios, UUID id_cotacao) {
        try {
            List<MapSqlParameterSource> batchParams = destinatarios.stream()
                .map(destinatario -> {
                    MapSqlParameterSource params = parameterSource(destinatario);
                    params.addValue("id_cotacao", id_cotacao);
                    return params;
                })
                .toList();

            int[] result = jdbcTemplate.batchUpdate(sqlInserirDestinatario(), batchParams.toArray(new MapSqlParameterSource[0]));
            return Arrays.stream(result).allMatch(rows -> rows > 0);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Boolean modificarDestinatario(Destinatarios destinatario, UUID id_cotacao, UUID id_destinatario) {
        try {
            MapSqlParameterSource params = parameterSource(destinatario);
            params.addValue("id_cotacao", id_cotacao);
            params.addValue("id_destinatario", id_destinatario);
            int numLinhasAfetas = jdbcTemplate.update(sqlModificarDestinatario(), params);
            return numLinhasAfetas > 0;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Boolean removerDestinatario(UUID id_cotacao, UUID id_destinatario) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id_cotacao", id_cotacao);
            params.addValue("id_destinatario", id_destinatario);
            int numLinhasAfetadas = jdbcTemplate.update(sqlRemoverDestinatario(), params);
            return numLinhasAfetadas > 0;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}
