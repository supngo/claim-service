package com.naturecode.claim_service.repository;

import com.naturecode.claim_service.model.Claim;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository {
  Optional<Claim> findById(String claimId);
  List<Claim> findAllByCustomerId(String customerId);
  Optional<Claim> update(String claimId, Claim claim);
}
