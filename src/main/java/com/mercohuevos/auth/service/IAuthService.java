package com.mercohuevos.auth.service;
import com.mercohuevos.auth.dto.*;

public interface IAuthService {
    VerificarDniResponseDTO         verificarDni(String dni);
    void                            crearPassword(CrearPasswordRequestDTO request);
    TokenResponseDTO                login(LoginRequestDTO request);
    TokenResponseDTO                refrescar(RefreshRequestDTO request);
    void                            logout(String refreshToken);
}