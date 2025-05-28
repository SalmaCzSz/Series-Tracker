package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioDao;
import com.series.tracker.models.Usuario;
import com.series.tracker.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>>  login(@RequestBody Usuario usuario){
        Usuario usuarioLogueado = usuarioDao.obtenerUsuarioPorCredenciales(usuario);

        if(usuarioLogueado != null){
            String tokenJwt = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getCorreo());

            Map<String, String> response = new HashMap<>();
            response.put("token", tokenJwt);

            return ResponseEntity.ok(response);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Credenciales inválidas");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
