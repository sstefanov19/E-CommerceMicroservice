package api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder

public record ProductDto(Long id, @NotBlank String category, @NotBlank String name , @Positive BigDecimal price , @Positive Integer quantity){}
