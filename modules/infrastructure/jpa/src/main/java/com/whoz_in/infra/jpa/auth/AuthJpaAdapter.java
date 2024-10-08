package com.whoz_in.infra.jpa.auth;

import com.whoz_in.domain.auth.domain.AuthRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AuthJpaAdapter implements AuthRepository {

  @Override
  public void save() {
    System.out.println("1");
  }
}
