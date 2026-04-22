package com.naturecode.claim_service.repository;

import com.naturecode.claim_service.model.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryClaimRepositoryTest {

  private InMemoryClaimRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryClaimRepository();
  }

  @Test
  void findById_returnsClaim_whenFound() {
    Optional<Claim> result = repository.findById("CLM-001");

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo("CLM-001");
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    Optional<Claim> result = repository.findById("UNKNOWN");

    assertThat(result).isEmpty();
  }

  @Test
  void findAllByCustomerId_returnsClaims_whenFound() {
    List<Claim> result = repository.findAllByCustomerId("CUST-1");

    assertThat(result).hasSize(2);
    assertThat(result).allMatch(c -> "CUST-1".equals(c.getCustomerId()));
  }

  @Test
  void findAllByCustomerId_returnsEmpty_whenNotFound() {
    List<Claim> result = repository.findAllByCustomerId("UNKNOWN");

    assertThat(result).isEmpty();
  }

  @Test
  void update_returnsUpdatedClaim_whenFound() {
    Claim updated = Claim.builder()
      .id("CLM-001").customerId("CUST-1").status("DENIED").reason("Updated reason").build();

    Optional<Claim> result = repository.update("CLM-001", updated);

    assertThat(result).isPresent();
    assertThat(result.get().getStatus()).isEqualTo("DENIED");
    assertThat(result.get().getReason()).isEqualTo("Updated reason");
  }

  @Test
  void update_returnsEmpty_whenNotFound() {
    Claim claim = Claim.builder().id("UNKNOWN").build();

    Optional<Claim> result = repository.update("UNKNOWN", claim);

    assertThat(result).isEmpty();
  }
}
