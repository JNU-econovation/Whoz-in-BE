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
import com.whoz_in.main_api.query.shared.application.View;
import com.whoz_in.main_api.query.shared.application.Viewer;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

@AnalyzeClasses(packages = "com.whoz_in.main_api", importOptions = ImportOption.DoNotIncludeTests.class)
class ViewerTest {

    @ArchTest
    public static void Viewer를_상속했으면_모든_메서드는_View나_OptionalView를_반환해야합니다(JavaClasses importedClasses) {
        ArchCondition<JavaMethod> returnTypeIsView = new ArchCondition<>("return a type implementing View") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaClass returnType = method.getReturnType().toErasure();

                if (!returnType.isAssignableTo(View.class)) {
                    String message = String.format("'%s'의 '%s' 는 View를 반환하지 않았습니다.",
                            method.getOwner().getName(), method.getName());
                    events.add(SimpleConditionEvent.violated(method, message));
                }
            }
        };

        ArchCondition<JavaMethod> returnTypeIsCollectionView = new ArchCondition<JavaMethod>("return a type collection of view") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaClass returnType = method.getReturnType().toErasure();

                if (!returnType.isAssignableTo(Collection.class)) {
                    try {

                        // 순수 자바 표준으로만 구현된 클래스와 메소드 찾기
                        Class<?> onwerClass = Class.forName(method.getOwner().getName());
                        Method reflectedMethod = onwerClass.getDeclaredMethod(method.getName(),
                                method.getRawParameterTypes().stream()
                                        .map(param -> {
                                            try {
                                                return Class.forName(param.getName());
                                            } catch (ClassNotFoundException e) {
                                                throw new RuntimeException(
                                                        "Failed to load parameter type: " + param.getName(), e);
                                            }
                                        })
                                        .toArray(Class<?>[]::new));

                        Type genericReturnType = reflectedMethod.getGenericReturnType();
                        if (genericReturnType instanceof ParameterizedType parameterizedType) {
                            Type[] typeArguments = parameterizedType.getActualTypeArguments();
                            for (Type typeArgument : typeArguments) {
                                if (typeArgument instanceof Class<?>) {
                                    Class<?> typeClass = (Class<?>) typeArgument;
                                    if (View.class.isAssignableFrom(typeClass)) {
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        String error = String.format("리플렉션 실패: '%s'의 '%s': %s",
                                method.getOwner().getName(), method.getName(), e.getMessage());
                        events.add(SimpleConditionEvent.violated(method, error));
                        return;
                    }
                }
            }
        };

        ArchCondition<JavaMethod> returnTypeIsOptionalView = new ArchCondition<>("return Optional<T> where T implements View") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaClass returnType = method.getReturnType().toErasure();

                if (returnType.isAssignableTo(Optional.class)) {
                    try {
                        Class<?> ownerClass = Class.forName(method.getOwner().getName());
                        Method reflectedMethod = ownerClass.getDeclaredMethod(method.getName(),
                                method.getRawParameterTypes().stream()
                                        .map(param -> {
                                            try {
                                                return Class.forName(param.getName());
                                            } catch (ClassNotFoundException e) {
                                                throw new RuntimeException("Failed to load parameter type: " + param.getName(), e);
                                            }
                                        }).toArray(Class<?>[]::new));

                        Type genericReturnType = reflectedMethod.getGenericReturnType();
                        if (genericReturnType instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                            Type[] typeArguments = parameterizedType.getActualTypeArguments();
                            for (Type typeArgument : typeArguments) {
                                if (typeArgument instanceof Class<?>) {
                                    Class<?> typeClass = (Class<?>) typeArgument;
                                    if (View.class.isAssignableFrom(typeClass)) {
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        String error = String.format("리플렉션 실패: '%s'의 '%s': %s",
                                method.getOwner().getName(), method.getName(), e.getMessage());
                        events.add(SimpleConditionEvent.violated(method, error));
                        return;
                    }
                }

                String message = String.format("Optional<? extends View>를 반환하지 않았습니다.",
                        method.getOwner().getName(), method.getName());
                events.add(SimpleConditionEvent.violated(method, message));
            }
        };

        ArchRuleDefinition.methods()
                .that().areDeclaredInClassesThat().areAssignableTo(Viewer.class)
                .should(returnTypeIsView.or(returnTypeIsOptionalView).or(returnTypeIsCollectionView))
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
