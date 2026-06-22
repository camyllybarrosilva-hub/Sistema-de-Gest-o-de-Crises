package com.gestaocrise.bo;

import com.gestaocrise.dao.CriseDAO;
import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.dto.CriseCreateDTO;
import com.gestaocrise.dto.CriseResponseDTO;
import com.gestaocrise.dto.CriseUpdateDTO;
import com.gestaocrise.entity.Crise;
import com.gestaocrise.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CriseBO {

    @Inject
    CriseDAO criseDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Transactional
    public CriseResponseDTO criar(CriseCreateDTO dto, Long usuarioIdLogado) {
        validarCamposObrigatorios(dto);

        Usuario criadoPor = usuarioDAO.buscarPorId(usuarioIdLogado);
        if (criadoPor == null) {
            throw new NotFoundException("Usuário não encontrado");
        }

        Usuario responsavel = null;
        if (dto.responsavelId != null) {
            responsavel = usuarioDAO.buscarPorId(dto.responsavelId);
            if (responsavel == null) {
                throw new NotFoundException("Responsável não encontrado");
            }
        }

        Crise crise = new Crise(dto.titulo, dto.descricao, dto.nivel, criadoPor);
        crise.setResponsavel(responsavel);
        criseDAO.salvar(crise);

        return converterParaDTO(crise);
    }

    @Transactional
    public CriseResponseDTO atualizar(Long id, CriseUpdateDTO dto) {
        Crise crise = criseDAO.buscarPorId(id);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }

        validarCamposObrigatorios(new CriseCreateDTO() {{
            titulo = dto.titulo;
            descricao = dto.descricao;
            nivel = dto.nivel;
            responsavelId = dto.responsavelId;
        }});

        Usuario responsavel = null;
        if (dto.responsavelId != null) {
            responsavel = usuarioDAO.buscarPorId(dto.responsavelId);
            if (responsavel == null) {
                throw new NotFoundException("Responsável não encontrado");
            }
        }

        crise.setTitulo(dto.titulo);
        crise.setDescricao(dto.descricao);
        crise.setNivel(dto.nivel);
        crise.setResponsavel(responsavel);
        crise.setDataAtualizacao(LocalDateTime.now());
        criseDAO.atualizar(crise);

        return converterParaDTO(crise);
    }

    @Transactional
    public CriseResponseDTO alterarStatus(Long id, String novoStatus) {
        Crise crise = criseDAO.buscarPorId(id);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }

        validarTransicaoDeStatus(crise.getStatus(), novoStatus);

        crise.setStatus(novoStatus);
        crise.setDataAtualizacao(LocalDateTime.now());
        criseDAO.atualizar(crise);

        return converterParaDTO(crise);
    }

    @Transactional
    public void deletar(Long id) {
        Crise crise = criseDAO.buscarPorId(id);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }
        criseDAO.deletar(id);
    }

    public CriseResponseDTO buscarPorId(Long id) {
        Crise crise = criseDAO.buscarPorId(id);
        if (crise == null) {
            throw new NotFoundException("Crise não encontrada");
        }
        return converterParaDTO(crise);
    }

    public List<CriseResponseDTO> listarTodos() {
        return criseDAO.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<CriseResponseDTO> buscarPorStatus(String status) {
        return criseDAO.buscarPorStatus(status).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<CriseResponseDTO> buscarPorNivel(String nivel) {
        return criseDAO.buscarPorNivel(nivel).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Crise buscarPorIdEntity(Long id) {
        return criseDAO.buscarPorId(id);
    }

    // Métodos auxiliares
    private void validarCamposObrigatorios(CriseCreateDTO dto) {
        if (dto.titulo == null || dto.titulo.trim().isEmpty()) {
            throw new BadRequestException("Título é obrigatório");
        }
        if (dto.descricao == null || dto.descricao.trim().isEmpty()) {
            throw new BadRequestException("Descrição é obrigatória");
        }
        if (dto.nivel == null || dto.nivel.trim().isEmpty()) {
            throw new BadRequestException("Nível é obrigatório");
        }
        if (!validarNivel(dto.nivel)) {
            throw new BadRequestException("Nível inválido. Valores: BAIXO, MEDIO, ALTO, CRITICO");
        }
    }

    private boolean validarNivel(String nivel) {
        return nivel.equals("BAIXO") || nivel.equals("MEDIO") || 
               nivel.equals("ALTO") || nivel.equals("CRITICO");
    }

    private void validarTransicaoDeStatus(String statusAtual, String novoStatus) {
        // ENCERRADA não pode voltar para nenhum outro status
        if ("ENCERRADA".equals(statusAtual)) {
            throw new BadRequestException("Crise encerrada não pode ser alterada");
        }

        // Validar status válidos
        if (!validarStatus(novoStatus)) {
            throw new BadRequestException("Status inválido. Valores: ABERTA, EM_ANDAMENTO, RESOLVIDA, ENCERRADA");
        }
    }

    private boolean validarStatus(String status) {
        return status.equals("ABERTA") || status.equals("EM_ANDAMENTO") || 
               status.equals("RESOLVIDA") || status.equals("ENCERRADA");
    }

    private CriseResponseDTO converterParaDTO(Crise crise) {
        CriseResponseDTO dto = new CriseResponseDTO();
        dto.id = crise.getId();
        dto.titulo = crise.getTitulo();
        dto.descricao = crise.getDescricao();
        dto.nivel = crise.getNivel();
        dto.status = crise.getStatus();
        dto.dataCriacao = crise.getDataCriacao();
        dto.dataAtualizacao = crise.getDataAtualizacao();
        dto.criadoPorNome = crise.getCriadoPor().getNome();
        dto.criadoPorId = crise.getCriadoPor().getId();
        if (crise.getResponsavel() != null) {
            dto.responsavelNome = crise.getResponsavel().getNome();
            dto.responsavelId = crise.getResponsavel().getId();
        }
        return dto;
    }
}
