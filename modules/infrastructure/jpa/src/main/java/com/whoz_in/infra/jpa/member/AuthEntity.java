package com.whoz_in.infra.jpa.member;

import com.whoz_in.infra.jpa.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class AuthEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 회원은 여러 계정을 만들 수 있지만
  // 계정 하나당 회원 정보 하나씩 고유로 가지므로 일대일
  @OneToOne
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @Column(name = "name", nullable = false)
  private String loginId;

  @Column(name = "name", nullable = false)
  private String password;

  protected AuthEntity() { }

  public AuthEntity(MemberEntity memberEntity, String loginId, String password) {
    this.memberEntity = memberEntity;
    this.loginId = loginId;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public MemberEntity getMemberEntity() {
    return memberEntity;
  }

  public String getLoginId() {
    return loginId;
  }

  public String getPassword() {
    return password;
  }
}
