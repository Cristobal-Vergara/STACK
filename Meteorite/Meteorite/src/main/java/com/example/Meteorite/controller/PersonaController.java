// src/main/java/com/example/Meteorite/controller/PersonaController.java
package com.example.Meteorite.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.Meteorite.model.Persona;
import com.example.Meteorite.service.PersonaService;

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

@Tag(name = "Personas", description = "Operaciones sobre personas de Meteorite")
@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    @Operation(summary = "Obtener todas las personas",
               description = "Devuelve todas las personas con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado obtenido",
        content = @Content(mediaType = "application/hal+json"))
    })
    public CollectionModel<EntityModel<Persona>> getAll() {
        List<EntityModel<Persona>> lista = personaService
            .getAllPersonas().stream()
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(PersonaController.class).getById(p.getId())).withSelfRel()))
            .collect(Collectors.toList());

        return CollectionModel.of(lista,
            linkTo(methodOn(PersonaController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por ID",
               description = "Devuelve una persona concreta con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Encontrada",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Persona.class))),
      @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<EntityModel<Persona>> getById(
        @Parameter(description = "ID de la persona", required = true)
        @PathVariable Long id) {
        return personaService.getPersonaByid(id)
            .map(p -> {
                EntityModel<Persona> model = EntityModel.of(p,
                    linkTo(methodOn(PersonaController.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(PersonaController.class).getAll()).withRel("personas")
                );
                return ResponseEntity.ok(model);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva persona",
               description = "Registra una nueva persona y devuelve su recurso con enlaces")
    @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Creada",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Persona.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Persona>> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos de la persona a crear", required = true,
          content = @Content(schema = @Schema(implementation = Persona.class)))
        @Valid @RequestBody Persona persona) {

        Persona saved = personaService.createPersona(persona);
        EntityModel<Persona> model = EntityModel.of(saved,
            linkTo(methodOn(PersonaController.class).getById(saved.getId())).withSelfRel(),
            linkTo(methodOn(PersonaController.class).getAll()).withRel("personas")
        );

        return ResponseEntity
            .created(URI.create(model.getRequiredLink("self").getHref()))
            .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar persona existente",
               description = "Actualiza los datos de una persona existente")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Actualizada",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Persona.class))),
      @ApiResponse(responseCode = "404", description = "No encontrada"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Persona>> update(
        @Parameter(description = "ID de la persona", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos nuevos de la persona", required = true,
          content = @Content(schema = @Schema(implementation = Persona.class)))
        @Valid @RequestBody Persona persona) {

        Persona updated = personaService.updatePersona(id, persona);
        EntityModel<Persona> model = EntityModel.of(updated,
            linkTo(methodOn(PersonaController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(PersonaController.class).getAll()).withRel("personas")
        );
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar persona",
               description = "Elimina una persona por su ID")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminada"),
      @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID de la persona", required = true)
        @PathVariable Long id) {
        personaService.deletePersona(id);
        return ResponseEntity.noContent().build();
    }
}
