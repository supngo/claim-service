package com.naturecode.claim_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim implements Serializable {
  private String id;
  private String customerId;
  private String status;
  private String reason;
}
