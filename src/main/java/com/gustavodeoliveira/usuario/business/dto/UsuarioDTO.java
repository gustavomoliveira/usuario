package com.gustavodeoliveira.usuario.business.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // conversão dos dados
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

}
