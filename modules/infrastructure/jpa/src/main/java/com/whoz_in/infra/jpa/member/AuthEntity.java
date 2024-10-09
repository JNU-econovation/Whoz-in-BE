package com.whoz_in.infra.jpa.member;

import com.whoz_in.infra.jpa.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted_at is null")
@Table(name = "auth")
@SQLDelete(sql = "UPDATE auth SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class AuthEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
