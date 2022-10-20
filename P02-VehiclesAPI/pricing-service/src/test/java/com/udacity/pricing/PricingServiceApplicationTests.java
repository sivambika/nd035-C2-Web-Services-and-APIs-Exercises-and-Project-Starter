package com.udacity.pricing;

import com.udacity.pricing.api.PricingController;
import com.udacity.pricing.service.PricingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PricingController.class)
public class PricingServiceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PricingService pricingService;

     @Test
    public void contextLoads() {
    }

    @Test
    public void testGetPrice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/services/price?vehicleId=3"))
                        .andExpect(status().isOk());
    }

}
