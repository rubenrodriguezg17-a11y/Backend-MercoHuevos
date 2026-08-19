package com.mercohuevos.auth.service;

import java.util.List;
import com.mercohuevos.auth.dto.CrearUsuarioRequestDTO;
import com.mercohuevos.auth.dto.EditarUsuarioRequestDTO;
import com.mercohuevos.auth.dto.UsuarioDTO;

public interface IUsuarioService {
    UsuarioDTO          crear(CrearUsuarioRequestDTO request);
    List<UsuarioDTO>    listarTodos();
    UsuarioDTO          desbloquear(Long id);
    UsuarioDTO          obtenerPorDni(String dni);
    UsuarioDTO          editUsuario(Long id, EditarUsuarioRequestDTO request);
    UsuarioDTO          desactivarUsuario(Long id);
}