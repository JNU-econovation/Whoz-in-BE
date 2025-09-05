package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.member.exception.EmptyFileException;
import com.whoz_in.main_api.command.member.exception.InvalidFileFormatException;
import com.whoz_in.main_api.command.member.exception.InvalidFileSizeException;
import com.whoz_in.main_api.command.shared.application.Command;

public record UploadProfileImage(byte[] bytes) implements Command {
    private static final int MAX_SIZE = 2 * 1024 * 1024; // 2MB
    private static final int MIN_SIZE = 1024; // 1KB

    // 위험한 파일 시그니처 (파일 헤더)
    private static final byte[][] MALICIOUS_SIGNATURES = {
            {0x4D, 0x5A}, // PE/EXE 파일
            {0x50, 0x4B}, // ZIP/JAR 파일 (일부 악성코드가 ZIP으로 위장)
            {0x7F, 0x45, 0x4C, 0x46}, // ELF 실행파일
            {0x23, 0x21} // 스크립트 파일 (#!)
    };

    public UploadProfileImage {
        valideFileNotEmpty(bytes);
        validateFileSize(bytes);
        validateMaliciousSignatures(bytes);
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

    private void validateMaliciousSignatures(byte[] bytes) {
        for (byte[] signature : MALICIOUS_SIGNATURES) {
            if (bytes.length >= signature.length) {
                boolean matches = true;
                for (int i = 0; i < signature.length; i++) {
                    if (bytes[i] != signature[i]) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    throw InvalidFileFormatException.EXCEPTION;
                }
            }
        }
    }
}
