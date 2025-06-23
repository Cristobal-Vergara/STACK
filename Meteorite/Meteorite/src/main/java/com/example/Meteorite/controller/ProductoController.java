// src/main/java/com/example/Meteorite/controller/ProductoController.java
package com.example.Meteorite.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Meteorite.model.Producto;
import com.example.Meteorite.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@Tag(name = "Productos", description = "Operaciones sobre productos de Meteorite")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos",
               description = "Devuelve todos los productos con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listado de productos",
        content = @Content(mediaType = "application/hal+json"))
    })
    public CollectionModel<EntityModel<Producto>> getAll() {
        List<EntityModel<Producto>> list = productoService.getAllProductos().stream()
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(ProductoController.class).getById(p.getId())).withSelfRel()))
            .collect(Collectors.toList());

        return CollectionModel.of(list,
            linkTo(methodOn(ProductoController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID",
               description = "Devuelve un producto concreto con enlaces HATEOAS")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Encontrado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Producto.class))),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<EntityModel<Producto>> getById(
        @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {

        return productoService.getProductoById(id)
            .map(prod -> {
                EntityModel<Producto> model = EntityModel.of(prod,
                    linkTo(methodOn(ProductoController.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(ProductoController.class).getAll()).withRel("productos")
                );
                return ResponseEntity.ok(model);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear producto",
               description = "Registra un nuevo producto y devuelve su recurso con enlaces")
    @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Producto creado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Producto.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Producto>> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos del producto a crear", required = true,
          content = @Content(schema = @Schema(implementation = Producto.class)))
        @Valid @RequestBody Producto producto) {

        Producto saved = productoService.createProducto(producto);
        EntityModel<Producto> model = EntityModel.of(saved,
            linkTo(methodOn(ProductoController.class).getById(saved.getId())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).getAll()).withRel("productos")
        );
        return ResponseEntity
            .created(URI.create(model.getRequiredLink("self").getHref()))
            .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto",
               description = "Actualiza un producto existente y devuelve su representación")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Producto actualizado",
        content = @Content(mediaType = "application/hal+json",
          schema = @Schema(implementation = Producto.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<EntityModel<Producto>> update(
        @Parameter(description = "ID del producto", required = true) @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos nuevos del producto", required = true,
          content = @Content(schema = @Schema(implementation = Producto.class)))
        @Valid @RequestBody Producto producto) {

        return productoService.getProductoById(id)
            .map(existing -> {
                Producto updated = productoService.updateProducto(id, producto);
                EntityModel<Producto> model = EntityModel.of(updated,
                    linkTo(methodOn(ProductoController.class).getById(id)).withSelfRel(),
                    linkTo(methodOn(ProductoController.class).getAll()).withRel("productos")
                );
                return ResponseEntity.ok(model);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto",
               description = "Elimina un producto por su ID")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Producto eliminado"),
      @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {

        return productoService.getProductoById(id)
            .map(p -> {
                productoService.deleteProducto(id);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
