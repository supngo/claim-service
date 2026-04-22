package com.naturecode.claim_service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ClaimResponse implements Serializable {
  private String id;
  private String customerId;
  private String status;
  private String reason;
}
