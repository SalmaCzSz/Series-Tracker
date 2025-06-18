package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioDao;
import com.series.tracker.models.Usuario;
import com.series.tracker.utils.JWTUtil;
import com.series.tracker.utils.SecurityUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    private String getAuthenticatedUsername() {
        return SecurityUtil.getAuthenticatedUsername();
    }

    private ResponseEntity<Map<String, String>> unauthorizedResponse() {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Usuario no autorizado. Por favor inicia sesión.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @GetMapping
    public ResponseEntity<?> obtenerUsuarios() {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        List<Usuario> usuarios = usuarioDao.obtenerUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        ResponseEntity<Map<String, String>> error = validarUsuario(usuario);

        if (error != null) {
            return error;
        }

        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);
        usuarioDao.crearUsuario(usuario);
        usuario.setPassword(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable long id,
                                              @RequestBody Usuario usuarioActualizado) {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        Usuario usuarioExistente = usuarioDao.obtenerUsuarioPorId(id);

        if (usuarioExistente == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario no encontrado");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        if (usuarioActualizado.getNombre() == null || usuarioActualizado.getNombre().isBlank() ||
            usuarioActualizado.getApellido() == null || usuarioActualizado.getApellido().isBlank() ||
            usuarioActualizado.getCorreo() == null || usuarioActualizado.getCorreo().isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Por favor, complete todos los datos obligatorios.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            String hash = argon2.hash(1, 1024, 1, usuarioActualizado.getPassword());
            usuarioExistente.setPassword(hash);
        }

        usuarioDao.modificarUsuario(usuarioExistente);
        usuarioExistente.setPassword(null);

        return ResponseEntity.ok(usuarioExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable long id) {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        Usuario usuarioExistente = usuarioDao.obtenerUsuarioPorId(id);

        if (usuarioExistente == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario no encontrado");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        usuarioDao.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable long id) {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        Usuario usuario = usuarioDao.obtenerUsuarioPorId(id);

        if (usuario == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario no encontrado");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        usuario.setPassword(null);

        return ResponseEntity.ok(usuario);
    }

    private ResponseEntity<Map<String, String>> validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank() ||
            usuario.getApellido() == null || usuario.getApellido().isBlank() ||
            usuario.getCorreo() == null || usuario.getCorreo().isBlank() ||
            (usuario.getPassword() == null || usuario.getPassword().isBlank())) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Por favor, complete todos los datos obligatorios.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return null;
    }
}
