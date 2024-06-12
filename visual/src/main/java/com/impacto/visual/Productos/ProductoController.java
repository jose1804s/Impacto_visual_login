package com.impacto.visual.Productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductoController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductoController.class);
    @Autowired
    private ProductService productoService;

    @PostMapping("/upload")
    public ResponseEntity<?> createProducto(@ModelAttribute ProductoDto productRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ADMIN")); 

        if (!isAdmin) {
            logger.warn("El usuario {} intentó subir un producto sin privilegios de administrador", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Se requieren privilegios de administrador.");
        }

        try {
            Producto producto = productService.saveProduct(productRequest);
            logger.info("Producto {} subido exitosamente por el usuario {}", producto.getId(), userDetails.getUsername());
            return ResponseEntity.ok(producto);
        } catch (IOException e) {
            logger.error("Ocurrió una IOException al subir el producto: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrió un error al procesar la carga del producto.");
        } catch (Exception e) {
            logger.error("Ocurrió un error inesperado al subir el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        try {
            Producto producto = productService.getProductoById(id);
            if (producto != null) {
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Ocurrió un error al obtener el producto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        try {
            List<Producto> productos = productService.getAllProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            logger.error("Ocurrió un error inesperado al obtener los productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Producto> updateProducto(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        ProductoDto productoDto = new ProductoDto();
        productoDto.setName(name);
        productoDto.setDescription(description);
        productoDto.setPrice(price);
        productoDto.setImage(image);

        Producto updatedProducto = productoService.updateProducto(id, productoDto);
        return ResponseEntity.ok(updatedProducto);
    }
}