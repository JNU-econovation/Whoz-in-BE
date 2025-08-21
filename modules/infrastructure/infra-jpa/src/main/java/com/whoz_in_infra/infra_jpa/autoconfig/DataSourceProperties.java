package com.whoz_in_infra.infra_jpa.autoconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
