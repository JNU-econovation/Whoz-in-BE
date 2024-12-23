package com.whoz_in.log_writer.archtect;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.whoz_in.log_writer.config.DataSourceConfig.LOG_WRITER_TRANSACTION_MANAGER;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;


class LogWriterTransactionManagerTest {

    //@Transactional를 사용할 때 transactionManager를 DataSourceConfig.LOG_WRITER_TRANSACTION_MANAGER로 명시해야 한다
    @Test
    void Transactional_어노테이션은_logWriterTransactionManager를_사용해야합니다() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.whoz_in.log_writer");
        ArchRule rule = methods()
                .that().areAnnotatedWith(Transactional.class)
                .should(validateTransactionManager())
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    private static ArchCondition<JavaMethod> validateTransactionManager() {
        return new ArchCondition<>("use logWriterTransactionManager") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                // 메서드에 붙은 어노테이션 가져오기
                Transactional transactionalAnnotation = method.reflect()
                        .getAnnotation(Transactional.class);

                // 어노테이션이 존재하는지
                if (transactionalAnnotation != null) {
                    String transactionManager = Optional.of(
                                    transactionalAnnotation.transactionManager())
                            .filter(tm -> !tm.isBlank())
                            .orElse(transactionalAnnotation.value());
                    if (!transactionManager.equals(LOG_WRITER_TRANSACTION_MANAGER)) {
                        throw new AssertionError(String.format(
                                "%s - %s의 transactionManager가 '%s'임",
                                method.getOwner().getName(),
                                method.getName(),
                                transactionManager
                        ));
                    }
                }
            }
        };
    }
}
