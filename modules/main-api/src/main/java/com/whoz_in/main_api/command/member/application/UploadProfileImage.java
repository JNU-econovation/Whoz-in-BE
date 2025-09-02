package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.Command;
import org.springframework.web.multipart.MultipartFile;

public record UploadProfileImage(MultipartFile image) implements Command {
}
