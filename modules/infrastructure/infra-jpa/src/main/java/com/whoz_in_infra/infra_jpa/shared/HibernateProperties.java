package com.whoz_in_infra.infra_jpa.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HibernateProperties {
    private String ddlAuto;
    private String physicalNamingStrategy;
    private boolean formatSql;
    private boolean showSql;
}
