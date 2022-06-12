package com.novarto.category.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private Long productId;
    private String name;
    private String description;
    private Double price;
}
