package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioDao;
import com.series.tracker.models.Usuario;
import com.series.tracker.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>>  login(@RequestBody Usuario usuario){
        if(usuario.getCorreo() == null || usuario.getCorreo().isBlank() ||
           usuario.getPassword() == null || usuario.getPassword().isBlank()){
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Correo y contraseña son obligatorios");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Usuario usuarioLogueado = usuarioDao.obtenerUsuarioPorCredenciales(usuario);

        if(usuarioLogueado != null){
            String tokenJwt = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getCorreo());

            Map<String, String> response = new HashMap<>();
            response.put("token", tokenJwt);
            response.put("userId", String.valueOf(usuarioLogueado.getId()));

            return ResponseEntity.ok(response);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Credenciales inválidas");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
