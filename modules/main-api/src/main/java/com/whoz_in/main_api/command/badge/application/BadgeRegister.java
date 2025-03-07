package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record BadgeRegister(String name, String colorCode) implements Command {
}
