package com.gestaocrise.bo;

import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.dao.PerfilUsuarioDAO;
import com.gestaocrise.dto.UsuarioCreateDTO;
import com.gestaocrise.dto.UsuarioResponseDTO;
import com.gestaocrise.dto.PerfilUsuarioDTO;
import com.gestaocrise.entity.Usuario;
import com.gestaocrise.entity.PerfilUsuario;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    PerfilUsuarioDAO perfilDAO;

    @Transactional
    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {
        validarCamposObrigatorios(dto);
        validarEmailUnico(dto.email);

        PerfilUsuario perfil = perfilDAO.buscarPorId(dto.perfilId);
        if (perfil == null) {
            throw new NotFoundException("Perfil de usuário não encontrado");
        }

        String senhaHash = criptografarSenha(dto.senha);

        Usuario usuario = new Usuario(dto.nome, dto.email, senhaHash, perfil);
        usuarioDAO.salvar(usuario);

        return converterParaDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioCreateDTO dto) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado");
        }

        if (!usuario.getEmail().equals(dto.email)) {
            validarEmailUnico(dto.email);
        }

        PerfilUsuario perfil = perfilDAO.buscarPorId(dto.perfilId);
        if (perfil == null) {
            throw new NotFoundException("Perfil de usuário não encontrado");
        }

        usuario.setNome(dto.nome);
        usuario.setEmail(dto.email);
        usuario.setPerfil(perfil);
        usuarioDAO.atualizar(usuario);

        return converterParaDTO(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado");
        }
        usuario.setAtivo(false);
        usuarioDAO.atualizar(usuario);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado");
        }
        return converterParaDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado");
        }
        return converterParaDTO(usuario);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioDAO.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Usuario buscarPorEmailEntity(String email) {
        return usuarioDAO.buscarPorEmail(email);
    }

    // Métodos auxiliares
    private void validarCamposObrigatorios(UsuarioCreateDTO dto) {
        if (dto.nome == null || dto.nome.trim().isEmpty()) {
            throw new BadRequestException("Nome é obrigatório");
        }
        if (dto.email == null || dto.email.trim().isEmpty()) {
            throw new BadRequestException("Email é obrigatório");
        }
        if (dto.senha == null || dto.senha.trim().isEmpty()) {
            throw new BadRequestException("Senha é obrigatória");
        }
        if (dto.perfilId == null) {
            throw new BadRequestException("Perfil ID é obrigatório");
        }
    }

    private void validarEmailUnico(String email) {
        Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new BadRequestException("Email já está registrado");
        }
    }

    private String criptografarSenha(String senha) {
        return BCrypt.withDefaults().hashToString(12, senha.toCharArray());
    }

    public boolean verificarSenha(String senhaPlana, String senhaHash) {
        return BCrypt.verifyer().verify(senhaPlana.toCharArray(), senhaHash).verified;
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        PerfilUsuarioDTO perfilDTO = new PerfilUsuarioDTO(
                usuario.getPerfil().getId(),
                usuario.getPerfil().getNome(),
                usuario.getPerfil().getDescricao()
        );
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                perfilDTO
        );
    }
}
