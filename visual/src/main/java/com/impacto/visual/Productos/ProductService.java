package com.impacto.visual.Productos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductoRepository productoRepository;

    public Producto saveProduct(ProductoDto productRequest) throws IOException {
        Producto product = new Producto();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());

        MultipartFile image = productRequest.getImage();
        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());
        }

        return productoRepository.save(product);
    }
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    public Producto updateProducto(Long id, ProductoDto productoDetails) throws IOException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + id));

        producto.setName(productoDetails.getName());
        producto.setDescription(productoDetails.getDescription());
        producto.setPrice(productoDetails.getPrice());

        MultipartFile image = productoDetails.getImage();
        if (image != null && !image.isEmpty()) {
            producto.setImage(image.getBytes());
        }

        return productoRepository.save(producto);
    }
}
