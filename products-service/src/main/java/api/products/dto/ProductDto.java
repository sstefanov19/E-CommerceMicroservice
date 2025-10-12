package api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class ProductDto {
    Long id;
    @NotBlank(message = "Category cant be blank!")
    String category;
    @NotBlank(message = "Name cant be blank!")
    String name;

    @Positive BigDecimal price;

    @Positive Integer quantity;
}
