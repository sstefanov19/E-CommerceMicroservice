package api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Category of product cannot be blank!")
    private String category;
    @NotBlank(message = "Name of product cannot be blank!")
    private String name;
    @NotBlank(message = "Price of product cannot be blank!")
    private BigDecimal price;

    @NotNull(message = "Quantity of product cannot be blank!")
    private Integer quantity;

}
