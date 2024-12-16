package com.whoz_in.main_api.architecture;

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
import com.whoz_in.main_api.shared.application.query.View;
import com.whoz_in.main_api.shared.application.query.Viewer;

@AnalyzeClasses(packages = "com.whoz_in.api", importOptions = ImportOption.DoNotIncludeTests.class)
class ViewerTest {

    @ArchTest
    public static void Viewer를_상속했으면_모든_메서드는_View를_반환해야합니다(JavaClasses importedClasses) {
        ArchCondition<JavaMethod> returnTypeImplementsView = new ArchCondition<>("return a type implementing View") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaClass returnType = method.getReturnType().toErasure();
                boolean implementsView = returnType.isAssignableTo(View.class);
                if (!implementsView) {
                    String message = String.format("'%s'의 '%s' 는 'View' 구현체를 반환하지 않았습니다.",
                            method.getOwner().getName(), method.getName());
                    events.add(SimpleConditionEvent.violated(method, message));
                }
            }
        };
        ArchRuleDefinition.methods()
                .that().areDeclaredInClassesThat().areAssignableTo(Viewer.class)
                .should(returnTypeImplementsView)
                .check(importedClasses);
    }

    @ArchTest
    public static void Viewer로_끝나면_Viewer인터페이스를_상속해야합니다(JavaClasses importedClasses) {
        ArchCondition<JavaClass> condition = new ArchCondition<>("implement Viewer") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean implementsViewer = javaClass.isAssignableTo(Viewer.class);
                if (!implementsViewer) {
                    String message = String.format("'%s' 클래스는 'Viewer' 인터페이스를 구현하지 않았습니다.", javaClass.getName());
                    events.add(SimpleConditionEvent.violated(javaClass, message));
                }
            }
        };

        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Viewer")
                .should(condition)
                .check(importedClasses);
    }
}
