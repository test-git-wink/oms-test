package com.sysco.oms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomRestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void handleMissingServletRequestParameter() throws Exception {

        mockMvc.perform(get("/v1/customer-orders/order/")
                .param("toDate", "2020-12-01")
                .param("page", "1").param("pageLimit", "2"))
                .andExpect(status().isBadRequest());

    }
}