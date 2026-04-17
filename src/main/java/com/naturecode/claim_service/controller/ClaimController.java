package com.naturecode.claim_service.controller;

import com.naturecode.claim_service.model.Claim;
import com.naturecode.claim_service.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<Claim> getClaimById(@PathVariable String claimId) {
    return claimService.getClaimById(claimId)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Claim>> getClaimsByCustomerId(@RequestParam String customerId) {
    List<Claim> claims = claimService.getClaimsByCustomerId(customerId);
    return claims.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(claims);
  }
}
