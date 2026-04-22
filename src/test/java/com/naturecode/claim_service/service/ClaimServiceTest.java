package com.naturecode.claim_service.service;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.mapper.ClaimMapper;
import com.naturecode.claim_service.model.Claim;
import com.naturecode.claim_service.repository.ClaimRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

  @Mock
  private ClaimRepository claimRepository;

  @Mock
  private ClaimMapper claimMapper;

  @InjectMocks
  private ClaimService claimService;

  private final Claim claim = Claim.builder()
    .id("CLM-001").customerId("CUST-1").status("APPROVED").reason("Valid claim").build();

  private final ClaimResponse response = ClaimResponse.builder()
    .id("CLM-001").customerId("CUST-1").status("APPROVED").reason("Valid claim").build();

  @Test
  void getClaimById_returnsResponse_whenFound() {
    when(claimRepository.findById("CLM-001")).thenReturn(Optional.of(claim));
    when(claimMapper.toResponse(claim)).thenReturn(response);

    Optional<ClaimResponse> result = claimService.getClaimById("CLM-001");

    assertThat(result).contains(response);
  }

  @Test
  void getClaimById_returnsEmpty_whenNotFound() {
    when(claimRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

    Optional<ClaimResponse> result = claimService.getClaimById("UNKNOWN");

    assertThat(result).isEmpty();
    verifyNoInteractions(claimMapper);
  }

  @Test
  void getClaimsByCustomerId_returnsResponses_whenFound() {
    when(claimRepository.findAllByCustomerId("CUST-1")).thenReturn(List.of(claim));
    when(claimMapper.toResponse(claim)).thenReturn(response);

    List<ClaimResponse> result = claimService.getClaimsByCustomerId("CUST-1");

    assertThat(result).containsExactly(response);
  }

  @Test
  void getClaimsByCustomerId_returnsEmpty_whenNotFound() {
    when(claimRepository.findAllByCustomerId("UNKNOWN")).thenReturn(List.of());

    List<ClaimResponse> result = claimService.getClaimsByCustomerId("UNKNOWN");

    assertThat(result).isEmpty();
    verifyNoInteractions(claimMapper);
  }

  @Test
  void updateClaim_returnsUpdatedResponse_whenFound() {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");
    request.setStatus("DENIED");
    request.setReason("Fraud");

    ClaimResponse updatedResponse = ClaimResponse.builder()
      .id("CLM-001").customerId("CUST-1").status("DENIED").reason("Fraud").build();

    when(claimMapper.toModel("CLM-001", request)).thenReturn(claim);
    when(claimRepository.update("CLM-001", claim)).thenReturn(Optional.of(claim));
    when(claimMapper.toResponse(claim)).thenReturn(updatedResponse);

    Optional<ClaimResponse> result = claimService.updateClaim("CLM-001", request);

    assertThat(result).contains(updatedResponse);
  }

  @Test
  void updateClaim_returnsEmpty_whenNotFound() {
    ClaimRequest request = new ClaimRequest();
    request.setCustomerId("CUST-1");

    when(claimMapper.toModel("UNKNOWN", request)).thenReturn(claim);
    when(claimRepository.update("UNKNOWN", claim)).thenReturn(Optional.empty());

    Optional<ClaimResponse> result = claimService.updateClaim("UNKNOWN", request);

    assertThat(result).isEmpty();
  }
}
