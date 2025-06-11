package com.whoz_in_infra.infra_jpa.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 어노테이션은 소프트 딜리트가 적용된 엔티티에 대해, 삭제된 데이터까지 함께 조회하고 싶을 때 사용합니다.<br>
 *
 * <p><b>전체 조건</b><br>
 * - @FilterDef는 <code>autoEnabled = true</code>로 설정되어 있어야 하며,<br>
 * - soft-deleted된 엔티티는 Hibernate 필터에 의해 자동으로 조회되지 않습니다.<br>
 *
 * <p><b>동작 방식</b><br>
 * - 이 어노테이션이 붙은 메서드가 실행되면 SoftDeleteFilterAspect AOP가 동작하여,<br>
 * - 해당 트랜잭션 내에서 <code>softDeleteFilter</code>를 일시적으로 비활성화합니다.<br>
 * - 그 결과, 삭제된 엔티티도 함께 조회됩니다.<br>
 *
 * <p><b>주의 사항</b><br>
 * - Hibernate 필터는 트랜잭션 내에서만 조작할 수 있으므로,<br>
 * - 반드시 <code>@Transactional</code>과 함께 사용해야 합니다.<br>
 *
 * <p><b>기타</b><br>
 * - 소프트 딜리트가 적용되지 않은 엔티티에는 필터 자체가 없으므로<br>
 * - 해당 어노테이션을 사용해도 아무런 영향이 없습니다.<br>
 * - 서비스 메서드나 Repository 메서드 등 메서드 수준에서 자유롭게 사용할 수 있습니다.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithDeleted {

}
