package com.gustavodeoliveira.usuario.business;

import com.gustavodeoliveira.usuario.business.converter.UsuarioConverter;
import com.gustavodeoliveira.usuario.business.dto.UsuarioDTO;
import com.gustavodeoliveira.usuario.infrastructure.entity.Usuario;
import com.gustavodeoliveira.usuario.infrastructure.exceptions.ConflictException;
import com.gustavodeoliveira.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.gustavodeoliveira.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

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
}
