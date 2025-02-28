package com.workspacepi.apiquoteflow.domain.respostas;

import com.workspacepi.apiquoteflow.application.respostas.CotacaoComProdutosDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RespostaCotacaoRepository {

    List<CotacaoComProdutosDTO> buscarCotacoesComProdutos(UUID id_empresa_resposta);

    List<RespostaCotacao> respostasCotacao(UUID id_empresa_resposta);

    RespostaCotacao respostaCotacao(UUID id_empresa_resposta, UUID id_cotacao);

    Boolean registrarResposta(RespostaCotacao resposta, UUID id_empresa_resposta, UUID id_cotacao);

    Boolean modificarResposta(RespostaCotacao resposta, UUID id_empresa_resposta, UUID id_cotacao);

    Boolean deletarResposta(UUID id_empresa_resposta, UUID id_cotacao);

}
