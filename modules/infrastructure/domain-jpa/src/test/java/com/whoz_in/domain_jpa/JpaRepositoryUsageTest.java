package com.whoz_in.domain_jpa;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaRepositoryUsageTest {

    @Test
    void findById는_사용하지않아야합니다() {
        JavaClasses imported = new ClassFileImporter().importPackages("com.whoz_in.domain_jpa");

        noClasses()
                .should(new ArchCondition<>("call findById on JpaRepository") {
                    @Override
                    public void check(JavaClass clazz, ConditionEvents events) {
                        for (JavaMethodCall call : clazz.getMethodCallsFromSelf()) {
                            if (call.getTarget().getName().equals("findById")
                                    && call.getTarget().getOwner().isAssignableTo(JpaRepository.class)) {
                                events.add(new SimpleConditionEvent(call, true, "[위반] " + call.getDescription()));
                            }
                        }
                    }
                })
                .because("JpaRepository의 findById 호출은 soft delete를 무시할 수 있기 때문에 사용 금지 (findOneById로 작명하세요)")
                .check(imported);
    }
}
