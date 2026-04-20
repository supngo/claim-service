package com.naturecode.claim_service.mapper;

import com.naturecode.claim_service.dto.ClaimRequest;
import com.naturecode.claim_service.dto.ClaimResponse;
import com.naturecode.claim_service.model.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClaimMapper {

  ClaimResponse toResponse(Claim claim);

  @Mapping(target = "id", source = "claimId")
  Claim toModel(String claimId, ClaimRequest request);
}
