package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.main_api.query.shared.application.Query;

public record PrivateIpListGet(
  String room
) implements Query {}
