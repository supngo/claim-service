package com.naturecode.claim_service.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.naturecode.claim_service.model.Claim;

@Repository
public class InMemoryClaimRepository implements ClaimRepository {

  private final Map<String, Claim> store;

  public InMemoryClaimRepository() {
    List<Claim> seed = List.of(
      Claim.builder().id("CLM-001").customerId("CUST-1").status("APPROVED").reason("Valid claim").build(),
      Claim.builder().id("CLM-002").customerId("CUST-1").status("DENIED").reason("Fraud Detected").build(),
      Claim.builder().id("CLM-003").customerId("CUST-2").status("PENDING").reason("Under review").build(),
      Claim.builder().id("CLM-004").customerId("CUST-3").status("APPROVED").reason("Valid claim").build(),
      Claim.builder().id("CLM-005").customerId("CUST-4").status("DENIED").reason("Expired after 30 days").build()
    );
    store = seed.stream().collect(Collectors.toMap(Claim::getId, c -> c));
  }

  @Override
  public Optional<Claim> findById(String claimId) {
    return Optional.ofNullable(store.get(claimId));
  }

  @Override
  public List<Claim> findAllByCustomerId(String customerId) {
    return store.values().stream()
      .filter(c -> customerId.equals(c.getCustomerId()))
      .collect(Collectors.toList());
  }
}
