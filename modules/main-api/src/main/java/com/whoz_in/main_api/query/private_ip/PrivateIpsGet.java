package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.query.shared.application.Query;

public record PrivateIpsGet(
  @Nullable String room
) implements Query {}
