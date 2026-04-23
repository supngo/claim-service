package com.naturecode.claim_service.service;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.mapper.ClaimMapper;
import com.naturecode.claim_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimService {

  private final ClaimRepository claimRepository;
  private final ClaimMapper claimMapper;

  @Cacheable(value = "claims", key = "#claimId")
  public Optional<ClaimResponse> getClaimById(String claimId) {
    log.debug("Cache miss — loading claim {}", claimId);
    return claimRepository.findById(claimId).map(claimMapper::toResponse);
  }

  @Cacheable(value = "claimsByCustomer", key = "#customerId")
  public List<ClaimResponse> getClaimsByCustomerId(String customerId) {
    log.debug("Cache miss — loading claims for customer {}", customerId);
    return claimRepository.findAllByCustomerId(customerId).stream()
      .map(claimMapper::toResponse)
      .toList();
  }

  @Caching(evict = {
    @CacheEvict(value = "claims", key = "#claimId"),
    @CacheEvict(value = "claimsByCustomer", key = "#request.customerId")
  })
  public Optional<ClaimResponse> updateClaim(String claimId, ClaimRequest request) {
    log.info("Updating claim {}", claimId);
    Optional<ClaimResponse> result = claimRepository.update(claimId, claimMapper.toModel(claimId, request))
        .map(claimMapper::toResponse);
    if (result.isEmpty()) {
      log.warn("Claim {} not found for update", claimId);
    }
    return result;
  }
}
