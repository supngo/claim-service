package com.naturecode.claim_service.dto;

import lombok.Data;

@Data
public class ClaimRequest {
  private String customerId;
  private String status;
  private String reason;
}
