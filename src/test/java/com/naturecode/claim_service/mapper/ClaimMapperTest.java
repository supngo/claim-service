package com.naturecode.claim_service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.model.Claim;

class ClaimMapperTest {

  private final ClaimMapper mapper = new ClaimMapperImpl();

  @Test
  void toResponse_mapsAllFields() {
    Claim claim = Claim.builder()
      .id("CLM-001")
      .customerId("CUST-1")
      .status("APPROVED")
      .reason("Valid claim")
      .build();

    ClaimResponse response = mapper.toResponse(claim);

    assertThat(response.getId()).isEqualTo("CLM-001");
    assertThat(response.getCustomerId()).isEqualTo("CUST-1");
    assertThat(response.getStatus()).isEqualTo("APPROVED");
    assertThat(response.getReason()).isEqualTo("Valid claim");
  }

  @Test
  void toResponse_returnsNull_whenClaimIsNull() {
    assertThat(mapper.toResponse(null)).isNull();
  }

  @Test
  void toModel_mapsClaimIdAndRequestFields() {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");
    request.setStatus("DENIED");
    request.setReason("Fraud detected");

    Claim claim = mapper.toModel("CLM-001", request);

    assertThat(claim.getId()).isEqualTo("CLM-001");
    assertThat(claim.getCustomerId()).isEqualTo("CUST-1");
    assertThat(claim.getStatus()).isEqualTo("DENIED");
    assertThat(claim.getReason()).isEqualTo("Fraud detected");
  }

  @Test
  void toModel_returnsNull_whenBothNull() {
    assertThat(mapper.toModel(null, null)).isNull();
  }

  @Test
  void toModel_mapsRequestOnly_whenClaimIdIsNull() {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");
    request.setStatus("APPROVED");
    request.setReason("Valid claim");

    Claim claim = mapper.toModel(null, request);

    assertThat(claim.getId()).isNull();
    assertThat(claim.getCustomerId()).isEqualTo("CUST-1");
  }

  @Test
  void toModel_mapsIdOnly_whenRequestIsNull() {
    Claim claim = mapper.toModel("CLM-001", null);

    assertThat(claim.getId()).isEqualTo("CLM-001");
    assertThat(claim.getCustomerId()).isNull();
    assertThat(claim.getStatus()).isNull();
    assertThat(claim.getReason()).isNull();
  }
}
