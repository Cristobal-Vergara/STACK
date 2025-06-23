// src/main/java/com/example/Meteorite/controller/ProveedorController.java
package com.example.Meteorite.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.Meteorite.model.Proveedor;
import com.example.Meteorite.service.ProveedorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@Tag(name = "Proveedores", description = "Operaciones sobre proveedores de Meteorite")
@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    @Operation(summary = "Listar proveedores",
               description = "Devuelve todos los proveedores con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado de proveedores",
        content = @Content(mediaType = "application/hal+json"))
    })
    public CollectionModel<EntityModel<Proveedor>> getAll() {
        List<EntityModel<Proveedor>> list = proveedorService.getAllProveedor().stream()
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(ProveedorController.class).getById(p.getId())).withSelfRel()))
            .collect(Collectors.toList());

        return CollectionModel.of(list,
            linkTo(methodOn(ProveedorController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por ID",
               description = "Devuelve un proveedor concreto con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Encontrado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Proveedor.class))),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<EntityModel<Proveedor>> getById(
        @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id) {

        return proveedorService.getProveedorById(id)
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(ProveedorController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(ProveedorController.class).getAll()).withRel("proveedores")))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear proveedor",
               description = "Registra un nuevo proveedor y devuelve su recurso con enlaces")
    @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Proveedor creado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Proveedor.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Proveedor>> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos del proveedor a crear", required = true,
          content = @Content(schema = @Schema(implementation = Proveedor.class)))
        @Valid @RequestBody Proveedor proveedor) {

        Proveedor saved = proveedorService.createProveedor(proveedor);
        EntityModel<Proveedor> model = EntityModel.of(saved,
            linkTo(methodOn(ProveedorController.class).getById(saved.getId())).withSelfRel(),
            linkTo(methodOn(ProveedorController.class).getAll()).withRel("proveedores")
        );

        return ResponseEntity
            .created(URI.create(model.getRequiredLink("self").getHref()))
            .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor",
               description = "Actualiza un proveedor existente y devuelve su representación")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Proveedor actualizado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Proveedor.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<EntityModel<Proveedor>> update(
        @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos nuevos del proveedor", required = true,
          content = @Content(schema = @Schema(implementation = Proveedor.class)))
        @Valid @RequestBody Proveedor proveedor) {

        return proveedorService.getProveedorById(id)
            .map(existing -> {
                Proveedor updated = proveedorService.updateProveedor(id, proveedor);
                EntityModel<Proveedor> model = EntityModel.of(updated,
                    linkTo(methodOn(ProveedorController.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(ProveedorController.class).getAll()).withRel("proveedores")
                );
                return ResponseEntity.ok(model);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor",
               description = "Elimina un proveedor por su ID")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Proveedor eliminado"),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id) {

        return proveedorService.getProveedorById(id)
            .map(p -> {
                proveedorService.deleteProveedor(id);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
