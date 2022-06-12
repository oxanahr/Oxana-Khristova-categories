package com.novarto.category.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class SingleProductDTO extends ProductDTO {
    private Long categoryId;
}
