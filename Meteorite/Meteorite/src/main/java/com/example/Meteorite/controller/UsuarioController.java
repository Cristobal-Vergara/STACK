package com.example.Meteorite.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Meteorite.model.Usuario;
import com.example.Meteorite.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Usuarios de Meteorite")
@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    public CollectionModel<EntityModel<Usuario>> getAll() {
        List<EntityModel<Usuario>> usuarios = usuarioService.getAllUsuarios().stream()
            .map(usuario -> EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).getById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).updateUsuario(usuario.getId(), usuario)).withRel("update")
            ))
            .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su identificador")
    public ResponseEntity<EntityModel<Usuario>> getById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
            .map(usuario -> EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).getAll()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).updateUsuario(id, usuario)).withRel("update")
            ))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario y lo agrega a la lista de usuarios")
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        EntityModel<Usuario> model = EntityModel.of(nuevoUsuario,
            linkTo(methodOn(UsuarioController.class).getById(nuevoUsuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).getAll()).withRel("usuarios")
        );
        return ResponseEntity.created(linkTo(methodOn(UsuarioController.class).getById(nuevoUsuario.getId())).toUri())
                             .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente por su identificador")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.getUsuarioById(id)
            .map(u -> {
                Usuario updated = usuarioService.updateUsuario(id, usuario);
                EntityModel<Usuario> model = EntityModel.of(updated,
                    linkTo(methodOn(UsuarioController.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(UsuarioController.class).getAll()).withRel("usuarios")
                );
                return ResponseEntity.ok(model);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su identificador")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
            .map(u -> {
                usuarioService.deleteUsuario(id);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
