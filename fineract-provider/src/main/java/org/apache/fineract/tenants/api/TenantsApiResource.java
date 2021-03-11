/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.tenants.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.fineract.infrastructure.core.boot.JDBCDriverConfig;
import org.apache.fineract.infrastructure.core.service.TenantDatabaseUpgradeService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Tomer Moshe on 12/01/2020.
 */
@Path("/tenants")
@Component
@Scope("singleton")
@Tag(name = "Tenants Api")
public class TenantsApiResource {

    private final PlatformSecurityContext context;
    private final TenantDatabaseUpgradeService tenantsUpgradeService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private JDBCDriverConfig driverConfig;
    @Autowired
    ApplicationContext appContext;

    @Autowired
    TenantsApiResource(final TenantDatabaseUpgradeService tenantsUpgradeService, final PlatformSecurityContext context,
            @Qualifier("hikariTenantDataSource") final DataSource dataSource) {
        this.context = context;
        this.tenantsUpgradeService = tenantsUpgradeService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @POST
    @Path("/refresh")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String refreshTenantsDataBases(@Parameter(hidden = true) final String apiRequestBodyAsJson) throws Exception {
        // Better Authentication needed here, probably some kind of Super User
        this.tenantsUpgradeService.upgradeAllTenants();
        return new JSONObject().append("result", "ok").toString();
    }
}
