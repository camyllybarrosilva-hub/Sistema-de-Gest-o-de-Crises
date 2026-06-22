package com.gestaocrise.bo;

import com.gestaocrise.dao.AcaoCriseDAO;
import com.gestaocrise.dao.CriseDAO;
import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.dto.AcaoCriseCreateDTO;
import com.gestaocrise.dto.AcaoCriseResponseDTO;
import com.gestaocrise.entity.AcaoCrise;
import com.gestaocrise.entity.Crise;
import com.gestaocrise.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AcaoCriseBO {

    @Inject
    AcaoCriseDAO acaoDAO;

    @Inject
    CriseDAO criseDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Transactional
    public AcaoCriseResponseDTO criar(AcaoCriseCreateDTO dto, Long usuarioIdLogado) {
        validarCamposObrigatorios(dto);

        Crise crise = criseDAO.buscarPorId(dto.criseId);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }

        validarCriseAceita(crise.getStatus());

        Usuario executadoPor = usuarioDAO.buscarPorId(usuarioIdLogado);
        if (executadoPor == null) {
            throw new NotFoundException("Usuário não encontrado");
        }

        validarTipo(dto.tipo);

        AcaoCrise acao = new AcaoCrise(dto.descricao, dto.tipo, executadoPor, crise);
        acaoDAO.salvar(acao);

        return converterParaDTO(acao);
    }

    public AcaoCriseResponseDTO buscarPorId(Long id) {
        AcaoCrise acao = acaoDAO.buscarPorId(id);
        if (acao == null) {
            throw new NotFoundException("Ação não encontrada");
        }
        return converterParaDTO(acao);
    }

    public List<AcaoCriseResponseDTO> listarPorCrise(Long criseId) {
        Crise crise = criseDAO.buscarPorId(criseId);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }
        return acaoDAO.buscarPorCrise(criseId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<AcaoCriseResponseDTO> listarTodos() {
        return acaoDAO.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private void validarCamposObrigatorios(AcaoCriseCreateDTO dto) {
        if (dto.descricao == null || dto.descricao.trim().isEmpty()) {
            throw new BadRequestException("Descrição é obrigatória");
        }
        if (dto.tipo == null || dto.tipo.trim().isEmpty()) {
            throw new BadRequestException("Tipo é obrigatório");
        }
        if (dto.criseId == null) {
            throw new BadRequestException("ID da crise é obrigatório");
        }
    }

    private void validarCriseAceita(String status) {
        if (!status.equals("ABERTA") && !status.equals("EM_ANDAMENTO")) {
            throw new BadRequestException("Ações só podem ser registradas em crises ABERTA ou EM_ANDAMENTO");
        }
    }

    private void validarTipo(String tipo) {
        if (!tipo.equals("CONTENCAO") && !tipo.equals("COMUNICACAO") && 
            !tipo.equals("RESOLUCAO") && !tipo.equals("MONITORAMENTO")) {
            throw new BadRequestException("Tipo inválido. Valores: CONTENCAO, COMUNICACAO, RESOLUCAO, MONITORAMENTO");
        }
    }

    private AcaoCriseResponseDTO converterParaDTO(AcaoCrise acao) {
        AcaoCriseResponseDTO dto = new AcaoCriseResponseDTO();
        dto.id = acao.getId();
        dto.descricao = acao.getDescricao();
        dto.tipo = acao.getTipo();
        dto.dataRegistro = acao.getDataRegistro();
        dto.executadoPorNome = acao.getExecutadoPor().getNome();
        dto.executadoPorId = acao.getExecutadoPor().getId();
        dto.criseId = acao.getCrise().getId();
        return dto;
    }
}
