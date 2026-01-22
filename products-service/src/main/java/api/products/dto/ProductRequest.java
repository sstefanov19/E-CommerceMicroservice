package api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest (
    @NotBlank(message = "Category of product cannot be blank!")
     String category,
    @NotBlank(message = "Name of product cannot be blank!")
     String name,
    @NotNull(message = "Price of product cannot be blank!")
    @Positive
     BigDecimal price,
    @NotNull(message = "Quantity of product cannot be blank!")
     Integer quantity
){}
