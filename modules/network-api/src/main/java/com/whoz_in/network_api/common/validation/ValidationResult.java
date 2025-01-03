package com.whoz_in.network_api.common.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Validator가 검증 실패한 내용을 담는 객체
public final class ValidationResult {
    private final List<String> errors;

    public ValidationResult() {
        this.errors = new ArrayList<>();
    }

    public ValidationResult(Collection<String> errors) {
        this.errors = new ArrayList<>(errors);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addErrors(Collection<String> errors) {
        this.errors.addAll(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return String.join(", ", errors);
    }
}