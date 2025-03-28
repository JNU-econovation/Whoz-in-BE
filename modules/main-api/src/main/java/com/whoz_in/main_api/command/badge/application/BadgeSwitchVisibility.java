package com.whoz_in.main_api.command.badge.application;

import com.whoz_in.main_api.command.shared.application.Command;
import java.util.UUID;

public record BadgeSwitchVisibility(UUID badgeId, boolean show) implements Command {
}
