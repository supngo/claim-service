package com.naturecode.claim_service.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

  @Bean
  public ClaimMapper claimMapper() {
    return Mappers.getMapper(ClaimMapper.class);
  }
}
