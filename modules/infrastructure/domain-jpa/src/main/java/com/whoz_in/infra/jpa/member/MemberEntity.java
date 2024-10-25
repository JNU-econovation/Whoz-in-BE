package com.whoz_in.infra.jpa.member;

import com.whoz_in.infra.jpa.shared.BaseEntity;
import com.whoz_in.infra.jpa.shared.enums.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class MemberEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "generation", nullable = false)
  private int generation;

  @Column(name = "position", nullable = false)
  @Enumerated(EnumType.STRING)
  private Position position;

  @Column(name = "status_message", nullable = false)
  private String statusMessage;

  public MemberEntity(String name, int generation, Position position, String statusMessage) {
    this.name = name;
    this.generation = generation;
    this.position = position;
    this.statusMessage = statusMessage;
  }

  protected MemberEntity() { }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getGeneration() {
    return generation;
  }

  public Position getPosition() {
    return position;
  }

  public String getStatusMessage() {
    return statusMessage;
  }
}
