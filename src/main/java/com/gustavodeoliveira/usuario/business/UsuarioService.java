package com.gustavodeoliveira.usuario.business;

import com.gustavodeoliveira.usuario.business.converter.UsuarioConverter;
import com.gustavodeoliveira.usuario.business.dto.UsuarioDTO;
import com.gustavodeoliveira.usuario.infrastructure.entity.Usuario;
import com.gustavodeoliveira.usuario.infrastructure.exceptions.ConflictException;
import com.gustavodeoliveira.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.gustavodeoliveira.usuario.infrastructure.repository.UsuarioRepository;
import com.gustavodeoliveira.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        validarEmailUnico(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void validarEmailUnico(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);

        if (existe) throw new ConflictException("Email já cadastrado: " + email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto) {
        // busca email do usuario através do token - tira a obrigatoriedade de passar o email
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não localizado."));

        // mescla os dados recebidos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // salva os dados do usuário convertido e depois pega o retorno e converte para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }
}
