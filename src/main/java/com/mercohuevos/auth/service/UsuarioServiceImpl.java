package com.mercohuevos.auth.service;

import java.util.List;

import com.mercohuevos.auth.dto.EditarUsuarioRequestDTO;
import org.springframework.stereotype.Service;

import com.mercohuevos.auth.dto.CrearUsuarioRequestDTO;
import com.mercohuevos.auth.dto.UsuarioDTO;
import com.mercohuevos.auth.model.Usuario;
import com.mercohuevos.auth.repository.IUsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository repository;

    @Override
    public UsuarioDTO crear(CrearUsuarioRequestDTO request) {
        if (repository.existsByDni(request.dni())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese DNI");
        }
        Usuario usuario = new Usuario();
        usuario.setDni(request.dni());
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setRol(request.rol());
        usuario.setArea(request.area());
        usuario.setPasswordConfigurada(false);
        return toDTO(repository.save(usuario));
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UsuarioDTO desbloquear(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);
        return toDTO(repository.save(usuario));
    }

    @Override
    public UsuarioDTO obtenerPorDni(String dni) {
        Usuario usuario = repository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + dni));
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO editUsuario(Long id, EditarUsuarioRequestDTO request) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado" + id));

        user.setNombreCompleto(request.nombreCompleto());
        user.setRol(request.rol());
        user.setArea(request.area());
        return toDTO(repository.save(user));
    }

    @Override
    public UsuarioDTO desactivarUsuario(Long id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado" + id));

        user.setActivo(false);
        return toDTO(repository.save(user));
    }

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(
                u.getIdUsuario(),
                u.getDni(),
                u.getNombreCompleto(),
                u.getRol().name(),
                u.getArea() !=null? u.getArea().name():null,
                u.isActivo(),
                u.isBloqueado());
    }
}