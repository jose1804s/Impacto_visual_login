package com.impacto.visual.Productos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
}
