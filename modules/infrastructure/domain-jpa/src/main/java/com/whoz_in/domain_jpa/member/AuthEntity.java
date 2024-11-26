package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class AuthEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @Column(name = "login_id", nullable = false)
  private String loginId;

  @Column(name = "password", nullable = false)
  private String password;

  protected AuthEntity() { }

  public AuthEntity(MemberEntity memberEntity, String loginId, String password) {
    this.memberEntity = memberEntity;
    this.loginId = loginId;
    this.password = password;
  }

}
