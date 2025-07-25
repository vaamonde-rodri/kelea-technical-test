package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_shouldReturnCorrectPriceFor1000OnDay14() throws Exception {
        mockMvc.perform(
                        get("/prices/query")
                                .param("applicationDate", "2020-06-14T10:00:00")
                                .param("productId", "35455")
                                .param("brandId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void test2_shouldReturnCorrectPriceFor1600OnDay14() throws Exception {
        mockMvc.perform(
                        get("/prices/query")
                                .param("applicationDate", "2020-06-14T16:00:00")
                                .param("productId", "35455")
                                .param("brandId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    void test3_shouldReturnCorrectPriceFor2100OnDay14() throws Exception {
        mockMvc.perform(
                        get("/prices/query")
                                .param("applicationDate", "2020-06-14T21:00:00")
                                .param("productId", "35455")
                                .param("brandId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void test4_shouldReturnCorrectPriceFor1000OnDay15() throws Exception {
        mockMvc.perform(
                        get("/prices/query")
                                .param("applicationDate", "2020-06-15T10:00:00")
                                .param("productId", "35455")
                                .param("brandId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    void test5_shouldReturnCorrectPriceFor2100OnDay16() throws Exception {
        mockMvc.perform(
                        get("/prices/query")
                                .param("applicationDate", "2020-06-16T21:00:00")
                                .param("productId", "35455")
                                .param("brandId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95));
    }

    @Test
    void whenPriceIsNotFound_thenReturns404AndErrorResponse() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2025-12-31T23:59:59")
                        .param("productId", "35459")
                        .param("brandId", "1"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Price Not Found")))
                .andExpect(jsonPath("$.message", is("No applicable price found for the given criteria.")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenProductIdIsNegative_thenReturns400AndValidationError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "-1")
                        .param("brandId", "1"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Error")))
                .andExpect(jsonPath("$.message", is("Product ID must be a positive number")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenBrandIdIsNegative_thenReturns400AndValidationError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "-1"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Error")))
                .andExpect(jsonPath("$.message", is("Brand ID must be a positive number")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenProductIdIsZero_thenReturns400AndValidationError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "0")
                        .param("brandId", "1"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Error")))
                .andExpect(jsonPath("$.message", is("Product ID must be a positive number")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenRequiredParameterIsMissing_thenReturns400AndMissingParameterError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        // brandId missing
                )

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Missing Required Parameter")))
                .andExpect(jsonPath("$.message", is("Required parameter 'brandId' is not present")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenParameterTypeIsInvalid_thenReturns400AndTypeMismatchError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "invalid-number")
                        .param("brandId", "1"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Invalid Parameter Type")))
                .andExpect(jsonPath("$.message", is("Parameter 'productId' must be of type Long")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenDateFormatIsInvalid_thenReturns400AndTypeMismatchError() throws Exception {
        mockMvc.perform(get("/prices/query")
                        .param("applicationDate", "invalid-date")
                        .param("productId", "35455")
                        .param("brandId", "1"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Invalid Parameter Type")))
                .andExpect(jsonPath("$.message", is("Parameter 'applicationDate' must be of type LocalDateTime")))
                .andExpect(jsonPath("$.path", is("/prices/query")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
