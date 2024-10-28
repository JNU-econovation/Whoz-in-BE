package com.whoz_in.api.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ApplicationTest {
        private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.whoz_in.api");

        @Test
        void Application은_Presentation을_의존하지않아야합니다() {
            ArchRule rule = ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..presentation..",
                            "jakarta.servlet..",
                            "org.springframework.web..",
                            "org.springframework.boot.."
                    );

            rule.check(importedClasses);
        }
    }