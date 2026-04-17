package com.naturecode.claim_service.service;

import com.naturecode.claim_service.model.Claim;
import com.naturecode.claim_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaimService {

  private final ClaimRepository claimRepository;

  public Optional<Claim> getClaimById(String claimId) {
    return claimRepository.findById(claimId);
  }

  public List<Claim> getClaimsByCustomerId(String customerId) {
    return claimRepository.findAllByCustomerId(customerId);
  }
}
