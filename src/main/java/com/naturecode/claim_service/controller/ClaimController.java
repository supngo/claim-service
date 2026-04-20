package com.naturecode.claim_service.controller;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

  private final ClaimService claimService;

  @GetMapping("/{claimId}")
  public ResponseEntity<ClaimResponse> getClaimById(@PathVariable String claimId) {
    return claimService.getClaimById(claimId)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<ClaimResponse>> getClaimsByCustomerId(@RequestParam String customerId) {
    List<ClaimResponse> claims = claimService.getClaimsByCustomerId(customerId);
    return claims.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(claims);
  }

  @PutMapping("/{claimId}")
  public ResponseEntity<ClaimResponse> updateClaim(
      @PathVariable String claimId,
      @RequestBody ClaimRequest request) {
    return claimService.updateClaim(claimId, request)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
}
