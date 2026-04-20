package com.naturecode.claim_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaimResponse {
  private String id;
  private String customerId;
  private String status;
  private String reason;
}
