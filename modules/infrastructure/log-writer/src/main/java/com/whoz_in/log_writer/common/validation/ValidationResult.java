package com.whoz_in.log_writer.common.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationResult {
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