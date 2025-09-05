package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.member.exception.EmptyFileException;
import com.whoz_in.main_api.command.shared.application.Command;

public record UploadProfileImage(byte[] bytes) implements Command {
    public UploadProfileImage {
        valideFileNotEmpty(bytes);
    }

    private void valideFileNotEmpty(byte[] bytes) {
        if (bytes == null) {
            throw EmptyFileException.EXCEPTION;
        }
        if (bytes.length == 0) {
            throw EmptyFileException.EXCEPTION;
        }
    }
}
