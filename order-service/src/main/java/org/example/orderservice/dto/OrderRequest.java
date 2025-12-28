package org.example.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Category of product cannot be blank!")
    private String category;

    @NotBlank(message = "Category of product cannot be blank!")
    private String item_name;

    @NotBlank(message = "Category of product cannot be blank!")
    private Integer quantity;


}
