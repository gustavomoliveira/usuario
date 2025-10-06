package com.gustavodeoliveira.usuario.business;

import com.gustavodeoliveira.usuario.business.converter.UsuarioConverter;
import com.gustavodeoliveira.usuario.business.dto.UsuarioDTO;
import com.gustavodeoliveira.usuario.infrastructure.entity.Usuario;
import com.gustavodeoliveira.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }
}
