package com.whoz_in.main_api.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class BoundaryTest {
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.whoz_in.main_api");

    @Test
    void command와_query는_서로_의존하지않아야합니다() {
        ArchRule commandToQuery = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..main_api.command..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..main_api.query..");

        ArchRule queryToDomain = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..main_api.query..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..main_api.command..");

        commandToQuery.check(importedClasses);
        queryToDomain.check(importedClasses);
    }
}
