package com.vtlamdev.sourcebase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void loginRefreshAndCurrentUserFlowWorks() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"sysadmin@sourcebase.local","password":"sysadmin"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        String loginPayload = loginResult.getResponse().getContentAsString();
        String accessToken = extractJsonValue(loginPayload, "token");
        String refreshToken = extractJsonValue(loginPayload, "refreshToken");

        mockMvc.perform(get("/api/auth/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sysadmin@sourcebase.local"))
                .andExpect(jsonPath("$.authority").value("SYS_ADMIN"));

        MvcResult refreshResult = mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s"}
                                """.formatted(refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        String refreshedPayload = refreshResult.getResponse().getContentAsString();
        String refreshedToken = extractJsonValue(refreshedPayload, "token");
        assertThat(refreshedToken).isNotBlank();
    }

    @Test
    void registerReturnsJwtAndAllowsAuthenticatedAccess() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"new.user@sourcebase.local",
                                  "password":"register123",
                                  "firstName":"New",
                                  "lastName":"User",
                                  "phone":"0123456789"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        String registerPayload = registerResult.getResponse().getContentAsString();
        String accessToken = extractJsonValue(registerPayload, "token");

        mockMvc.perform(get("/api/auth/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new.user@sourcebase.local"))
                .andExpect(jsonPath("$.authority").value("CUSTOMER_USER"));
    }

    @Test
    void currentUserRequiresJwtAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerApiDocsEndpointLoads() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void currentUserWithoutTenantCanRegisterTenantAndBecomesTenantAdmin() throws Exception {
        MvcResult registerUserResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"tenant.owner@sourcebase.local",
                                  "password":"register123",
                                  "firstName":"Tenant",
                                  "lastName":"Owner",
                                  "phone":"0123456788"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String registerPayload = registerUserResult.getResponse().getContentAsString();
        String accessToken = extractJsonValue(registerPayload, "token");

        mockMvc.perform(post("/api/tenant/register")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"Acme Tenant",
                                  "region":"HCM",
                                  "country":"VN",
                                  "state":"ACTIVE",
                                  "email":"tenant@acme.local",
                                  "phone":"0900000000",
                                  "address":"1 Nguyen Hue"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Acme Tenant"))
                .andExpect(jsonPath("$.email").value("tenant@acme.local"));

        MvcResult loginAgainResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"tenant.owner@sourcebase.local","password":"register123"}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String reloginPayload = loginAgainResult.getResponse().getContentAsString();
        String refreshedAccessToken = extractJsonValue(reloginPayload, "token");

        mockMvc.perform(get("/api/auth/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshedAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authority").value("TENANT_ADMIN"))
                .andExpect(jsonPath("$.tenantId.id").isNotEmpty());
    }

    @Test
    void tenantUpdateWithoutVersionUsesExistingVersionAndDoesNotInsertDuplicate() throws Exception {
        MvcResult registerUserResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"tenant.updater@sourcebase.local",
                                  "password":"register123",
                                  "firstName":"Tenant",
                                  "lastName":"Updater",
                                  "phone":"0123456787"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String registerPayload = registerUserResult.getResponse().getContentAsString();
        String accessToken = extractJsonValue(registerPayload, "token");

        MvcResult createTenantResult = mockMvc.perform(post("/api/tenant/register")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"Original Tenant",
                                  "region":"HCM",
                                  "country":"VN",
                                  "state":"ACTIVE",
                                  "email":"tenant-update@acme.local",
                                  "phone":"0900000001",
                                  "address":"2 Nguyen Hue"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String tenantPayload = createTenantResult.getResponse().getContentAsString();
        String tenantId = extractNestedJsonValue(tenantPayload, "id", "id");

        mockMvc.perform(post("/api/tenant/register")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": {
                                    "id":"%s",
                                    "entityType":"TENANT",
                                    "nullUid":false
                                  },
                                  "title":"Updated Tenant",
                                  "region":"North America",
                                  "country":"United States",
                                  "state":"ACTIVE",
                                  "email":"tenant-update@acme.local",
                                  "phone":"+1-555-012-3456",
                                  "address":"1600 Amphitheatre Parkway"
                                }
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.id").value(tenantId))
                .andExpect(jsonPath("$.title").value("Updated Tenant"))
                .andExpect(jsonPath("$.email").value("tenant-update@acme.local"));
    }

    @Test
    void adminSettingsDeleteUsesSoftDeleteAndHidesRecordFromReads() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"sysadmin@sourcebase.local","password":"sysadmin"}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = extractJsonValue(loginResult.getResponse().getContentAsString(), "token");

        mockMvc.perform(post("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", "13814000-1dd2-11b2-8080-808080808080")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "key":"soft-delete-test",
                                  "jsonValue":{"enabled":true}
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("soft-delete-test"));

        mockMvc.perform(get("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", "13814000-1dd2-11b2-8080-808080808080")
                        .queryParam("textSearch", "soft-delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("PAGE"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.data[0].key").value("soft-delete-test"));

        mockMvc.perform(delete("/api/v1/admin/settings/{key}", "soft-delete-test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", "13814000-1dd2-11b2-8080-808080808080"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        mockMvc.perform(get("/api/v1/admin/settings/{key}", "soft-delete-test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", "13814000-1dd2-11b2-8080-808080808080"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        mockMvc.perform(get("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", "13814000-1dd2-11b2-8080-808080808080")
                        .queryParam("textSearch", "soft-delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("PAGE"))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void adminSettingsSupportsPageAndScrollModes() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"sysadmin@sourcebase.local","password":"sysadmin"}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = extractJsonValue(loginResult.getResponse().getContentAsString(), "token");
        String tenantId = "13814000-1dd2-11b2-8080-808080808080";

        mockMvc.perform(post("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"key":"scroll-test-1","jsonValue":{"order":1}}
                                """))
                .andExpect(status().isOk());
        Thread.sleep(2L);
        mockMvc.perform(post("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"key":"scroll-test-2","jsonValue":{"order":2}}
                                """))
                .andExpect(status().isOk());
        Thread.sleep(2L);
        mockMvc.perform(post("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"key":"scroll-test-3","jsonValue":{"order":3}}
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .queryParam("mode", "page")
                        .queryParam("pageSize", "2")
                        .queryParam("textSearch", "scroll-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("PAGE"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));

        MvcResult firstScrollResult = mockMvc.perform(get("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .queryParam("mode", "scroll")
                        .queryParam("limit", "2")
                        .queryParam("textSearch", "scroll-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("SCROLL"))
                .andExpect(jsonPath("$.limit").value(2))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andReturn();

        String firstScrollPayload = firstScrollResult.getResponse().getContentAsString();
        String nextCursor = extractJsonValue(firstScrollPayload, "nextCursor");

        mockMvc.perform(get("/api/v1/admin/settings")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("tenantId", tenantId)
                        .queryParam("mode", "scroll")
                        .queryParam("limit", "2")
                        .queryParam("textSearch", "scroll-test")
                        .queryParam("cursor", nextCursor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("SCROLL"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.nextCursor").doesNotExist())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    private String extractJsonValue(String payload, String field) {
        String marker = "\"" + field + "\":\"";
        int start = payload.indexOf(marker);
        int from = start + marker.length();
        int end = payload.indexOf('"', from);
        return payload.substring(from, end);
    }

    private String extractNestedJsonValue(String payload, String objectField, String nestedField) {
        String objectMarker = "\"" + objectField + "\":{";
        int objectStart = payload.indexOf(objectMarker);
        String nestedMarker = "\"" + nestedField + "\":\"";
        int start = payload.indexOf(nestedMarker, objectStart);
        int from = start + nestedMarker.length();
        int end = payload.indexOf('"', from);
        return payload.substring(from, end);
    }

}
