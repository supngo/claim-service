package com.naturecode.claim_service.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.service.ClaimService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ClaimController.class)
class ClaimControllerTest {

  @TestConfiguration
  public static class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
      return new NoOpCacheManager();
    }
  }

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ClaimService claimService;

  private final ClaimResponse response = ClaimResponse.builder()
    .id("CLM-001").customerId("CUST-1").status("APPROVED").reason("Valid claim").build();

  @Test
  void getClaimById_returns200_whenFound() throws Exception {
    when(claimService.getClaimById("CLM-001")).thenReturn(Optional.of(response));

    mockMvc.perform(get("/claims/CLM-001"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("CLM-001"))
      .andExpect(jsonPath("$.status").value("APPROVED"));
  }

  @Test
  void getClaimById_returns404_whenNotFound() throws Exception {
    when(claimService.getClaimById("UNKNOWN")).thenReturn(Optional.empty());

    mockMvc.perform(get("/claims/UNKNOWN"))
      .andExpect(status().isNotFound());
  }

  @Test
  void getClaimsByCustomerId_returns200_whenFound() throws Exception {
    when(claimService.getClaimsByCustomerId("CUST-1")).thenReturn(List.of(response));

    mockMvc.perform(get("/claims").param("customerId", "CUST-1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].customerId").value("CUST-1"));
  }

  @Test
  void getClaimsByCustomerId_returns404_whenNotFound() throws Exception {
    when(claimService.getClaimsByCustomerId("UNKNOWN")).thenReturn(List.of());

    mockMvc.perform(get("/claims").param("customerId", "UNKNOWN"))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateClaim_returns200_whenFound() throws Exception {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");
    request.setStatus("DENIED");
    request.setReason("Fraud");

    ClaimResponse updated = ClaimResponse.builder()
      .id("CLM-001").customerId("CUST-1").status("DENIED").reason("Fraud").build();

    when(claimService.updateClaim("CLM-001", request)).thenReturn(Optional.of(updated));

    mockMvc.perform(put("/claims/CLM-001")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("DENIED"));
  }

  @Test
  void updateClaim_returns404_whenNotFound() throws Exception {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");

    when(claimService.updateClaim("UNKNOWN", request)).thenReturn(Optional.empty());

    mockMvc.perform(put("/claims/UNKNOWN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isNotFound());
  }
}
