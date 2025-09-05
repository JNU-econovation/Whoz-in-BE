package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.member.exception.EmptyFileException;
import com.whoz_in.main_api.command.member.exception.InvalidFileSizeException;
import com.whoz_in.main_api.command.shared.application.Command;

public record UploadProfileImage(byte[] bytes) implements Command {
    private static final int MAX_SIZE = 2 * 1024 * 1024; // 2MB
    private static final int MIN_SIZE = 1024; // 1KB

    public UploadProfileImage {
        valideFileNotEmpty(bytes);
        validateFileSize(bytes);
    }

    private void valideFileNotEmpty(byte[] bytes) {
        if (bytes == null) {
            throw EmptyFileException.EXCEPTION;
        }
        if (bytes.length == 0) {
            throw EmptyFileException.EXCEPTION;
        }
    }

    private void validateFileSize(byte[] bytes) {
        if (bytes.length < MIN_SIZE) {
            throw InvalidFileSizeException.EXCEPTION;
        }
        if (bytes.length > MAX_SIZE) {
            throw InvalidFileSizeException.EXCEPTION;
        }
    }
}
