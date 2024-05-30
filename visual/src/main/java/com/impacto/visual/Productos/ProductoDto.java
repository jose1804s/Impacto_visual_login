package com.impacto.visual.Productos;



import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductoDto {
    private String name;
    private String description;
    private double price;
    private MultipartFile image;
}
