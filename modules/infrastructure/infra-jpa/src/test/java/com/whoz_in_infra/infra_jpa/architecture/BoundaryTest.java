package com.whoz_in_infra.infra_jpa.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class BoundaryTest {
        private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.whoz_in_infra.infra_jpa");

    @Test
    void domain과_query는_서로_의존하지않아야합니다() {
        ArchRule domainToQuery = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..infra_jpa.domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infra_jpa.query..");

        ArchRule queryToDomain = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..infra_jpa.query..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infra_jpa.domain..");

        domainToQuery.check(importedClasses);
        queryToDomain.check(importedClasses);
    }
}
