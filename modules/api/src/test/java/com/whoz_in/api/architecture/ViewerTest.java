package com.whoz_in.api.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.whoz_in.api.shared.application.query.View;

@AnalyzeClasses(packages = "com.whoz_in.api", importOptions = ImportOption.DoNotIncludeTests.class)
class ViewerTest {

    @ArchTest
    public static void Viewer는_View를_반환해야합니다(JavaClasses importedClasses) {
        ArchCondition<JavaMethod> returnTypeImplementsView = new ArchCondition<>("return a type implementing View") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaClass returnType = method.getReturnType().toErasure();
                boolean implementsView = returnType.isAssignableTo(View.class);
                if (!implementsView) {
                    String message = String.format("'%s'의 '%s' 는 'View' 구현체를 반환하지 않습니다.",
                            method.getOwner().getName(), method.getName());
                    events.add(SimpleConditionEvent.violated(method, message));
                }
            }
        };
        ArchRuleDefinition.methods()
                .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Viewer")
                .should(returnTypeImplementsView)
                .check(importedClasses);
    }
}
