package com.naturecode.claim_service.service;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.mapper.ClaimMapper;
import com.naturecode.claim_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaimService {

  private final ClaimRepository claimRepository;
  private final ClaimMapper claimMapper;

  @Cacheable(value = "claims", key = "#claimId")
  public Optional<ClaimResponse> getClaimById(String claimId) {
    return claimRepository.findById(claimId).map(claimMapper::toResponse);
  }

  @Cacheable(value = "claimsByCustomer", key = "#customerId")
  public List<ClaimResponse> getClaimsByCustomerId(String customerId) {
    return claimRepository.findAllByCustomerId(customerId).stream()
      .map(claimMapper::toResponse)
      .toList();
  }

  @Caching(evict = {
    @CacheEvict(value = "claims", key = "#claimId"),
    @CacheEvict(value = "claimsByCustomer", key = "#request.customerId")
  })
  public Optional<ClaimResponse> updateClaim(String claimId, ClaimRequest request) {
    return claimRepository.update(claimId, claimMapper.toModel(claimId, request))
      .map(claimMapper::toResponse);
  }
}
