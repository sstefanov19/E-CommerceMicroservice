package api.products.controller;

import api.products.dto.ProductDto;
import api.products.dto.ProductRequest;
import api.products.dto.ProductResponse;

import api.products.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    public void init() {
        productRequest = ProductRequest.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
        productResponse = ProductResponse.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
    }

    @Test
    public void ProductController_CreateProduct_ReturnCreated() throws Exception {
        given(productService.createProduct(ArgumentMatchers.any()))
                .willReturn(productResponse);

        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", CoreMatchers.is(productResponse.category())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productResponse.name())));

    }

    @Test
    public void ProductController_GetProducts_ReturnOkay() throws Exception {
        String category = "Electronics";
        when(productService.getProducts(category)).thenReturn(List.of(productResponse));

        ResultActions response = mockMvc.perform(get("/products")
                .param("category", category)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }

}
