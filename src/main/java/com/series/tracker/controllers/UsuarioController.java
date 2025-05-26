package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioDao;
import com.series.tracker.models.Usuario;
import com.series.tracker.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios(@RequestHeader("Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Usuario> usuarios = usuarioDao.obtenerUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    private boolean validarToken(String token) {
        String usuarioId = jwtUtil.getKey(token);

        return usuarioId != null;
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);
        usuarioDao.crearUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable long id,
                                                    @RequestBody Usuario usuarioActualizado,
                                                    @RequestHeader("Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuarioExistente = usuarioDao.obtenerUsuarioPorId(id);

        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            String hash = argon2.hash(1, 1024, 1, usuarioActualizado.getPassword());
            usuarioExistente.setPassword(hash);
        }

        usuarioDao.modificarUsuario(usuarioExistente);

        return ResponseEntity.ok(usuarioExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long id,
                                                @RequestHeader("Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        usuarioDao.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
