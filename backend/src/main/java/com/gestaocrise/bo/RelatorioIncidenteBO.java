package com.gestaocrise.bo;

import com.gestaocrise.dao.RelatorioIncidenteDAO;
import com.gestaocrise.dao.CriseDAO;
import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.dto.RelatorioIncidenteCreateDTO;
import com.gestaocrise.dto.RelatorioIncidenteResponseDTO;
import com.gestaocrise.entity.RelatorioIncidente;
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
public class RelatorioIncidenteBO {

    @Inject
    RelatorioIncidenteDAO relatorioDAO;

    @Inject
    CriseDAO criseDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Transactional
    public RelatorioIncidenteResponseDTO criar(RelatorioIncidenteCreateDTO dto, Long usuarioIdLogado) {
        validarCamposObrigatorios(dto);

        Crise crise = criseDAO.buscarPorId(dto.criseId);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }

        validarCriseAceita(crise.getStatus());

        Usuario geradoPor = usuarioDAO.buscarPorId(usuarioIdLogado);
        if (geradoPor == null) {
            throw new NotFoundException("Usuário não encontrado");
        }

        RelatorioIncidente relatorio = new RelatorioIncidente(dto.titulo, dto.conteudo, crise, geradoPor);
        relatorioDAO.salvar(relatorio);

        return converterParaDTO(relatorio);
    }

    public RelatorioIncidenteResponseDTO buscarPorId(Long id) {
        RelatorioIncidente relatorio = relatorioDAO.buscarPorId(id);
        if (relatorio == null) {
            throw new NotFoundException("Relatório não encontrado");
        }
        return converterParaDTO(relatorio);
    }

    public List<RelatorioIncidenteResponseDTO> listarPorCrise(Long criseId) {
        Crise crise = criseDAO.buscarPorId(criseId);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }
        return relatorioDAO.buscarPorCrise(criseId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<RelatorioIncidenteResponseDTO> listarTodos() {
        return relatorioDAO.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private void validarCamposObrigatorios(RelatorioIncidenteCreateDTO dto) {
        if (dto.titulo == null || dto.titulo.trim().isEmpty()) {
            throw new BadRequestException("Título é obrigatório");
        }
        if (dto.conteudo == null || dto.conteudo.trim().isEmpty()) {
            throw new BadRequestException("Conteúdo é obrigatório");
        }
        if (dto.criseId == null) {
            throw new BadRequestException("ID da crise é obrigatório");
        }
    }

    private void validarCriseAceita(String status) {
        if (!status.equals("RESOLVIDA") && !status.equals("ENCERRADA")) {
            throw new BadRequestException("Relatórios só podem ser gerados para crises RESOLVIDA ou ENCERRADA");
        }
    }

    private RelatorioIncidenteResponseDTO converterParaDTO(RelatorioIncidente relatorio) {
        RelatorioIncidenteResponseDTO dto = new RelatorioIncidenteResponseDTO();
        dto.id = relatorio.getId();
        dto.titulo = relatorio.getTitulo();
        dto.conteudo = relatorio.getConteudo();
        dto.dataGeracao = relatorio.getDataGeracao();
        dto.criseId = relatorio.getCrise().getId();
        dto.criseTitulo = relatorio.getCrise().getTitulo();
        dto.geradoPorNome = relatorio.getGeradoPor().getNome();
        dto.geradoPorId = relatorio.getGeradoPor().getId();
        return dto;
    }
}
