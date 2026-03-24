package com.example.bai5.tests;

import com.example.bai5.base.ApiBaseTest;
import com.example.bai5.utils.PerformanceMonitorUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SlaPerformanceTest extends ApiBaseTest {

    @DataProvider(name = "slaScenarios")
    public Object[][] slaScenarios() {
        return new Object[][] {
                { "GET", "/users", Map.of("page", 1), null, 200, 2000L, "users-list-size" },
                { "GET", "/users/2", null, null, 200, 1500L, "user-id-equals-2" },
                { "POST", "/users", null, createUserBody(), 201, 3000L, "created-has-id" },
                { "POST", "/login", null, createLoginBody(), 200, 2000L, "login-has-token" },
                { "DELETE", "/users/2", null, null, 204, 1000L, "no-content" }
        };
    }

    @Test(dataProvider = "slaScenarios", description = "SLA assertion by DataProvider across 5 core APIs")
    public void shouldMeetSlaForCoreApis(String method,
            String endpoint,
            Map<String, Object> queryParams,
            Map<String, Object> body,
            int expectedStatus,
            long maxMs,
            String assertionType) {
        executeAndAssertSla(method, endpoint, queryParams, body, expectedStatus, maxMs, assertionType);
    }

    @Step("Call {method} {endpoint} - SLA: {maxMs}ms")
    public void executeAndAssertSla(String method,
            String endpoint,
            Map<String, Object> queryParams,
            Map<String, Object> body,
            int expectedStatus,
            long maxMs,
            String assertionType) {
        io.restassured.specification.RequestSpecification req = given().spec(requestSpec);
        if (queryParams != null && !queryParams.isEmpty()) {
            req.queryParams(queryParams);
        }
        if (body != null && !body.isEmpty()) {
            req.body(body);
        }

        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = req.when().get(endpoint).andReturn();
                break;
            case "POST":
                response = req.when().post(endpoint).andReturn();
                break;
            case "DELETE":
                response = req.when().delete(endpoint).andReturn();
                break;
            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }

        long actualMs = response.time();
        System.out.printf("[SLA] %s %s -> actual=%dms, max=%dms, status=%d%n",
                method.toUpperCase(), endpoint, actualMs, maxMs, response.statusCode());

        Assert.assertEquals(response.statusCode(), expectedStatus,
                String.format("Unexpected status for %s %s", method, endpoint));
        Assert.assertTrue(actualMs <= maxMs,
                String.format("SLA violated for %s %s. actual=%dms, max=%dms", method, endpoint, actualMs, maxMs));

        switch (assertionType) {
            case "users-list-size":
                Integer size = response.jsonPath().getInt("data.size()");
                Assert.assertNotNull(size, "data.size() should not be null");
                Assert.assertTrue(size > 1, "Expected data.size() > 1");
                break;
            case "user-id-equals-2":
                Integer id = response.jsonPath().getInt("data.id");
                Assert.assertEquals(id, Integer.valueOf(2), "Expected data.id = 2");
                break;
            case "created-has-id":
                String createdId = response.jsonPath().getString("id");
                Assert.assertNotNull(createdId, "Expected id in create user response");
                Assert.assertFalse(createdId.trim().isEmpty(), "Expected non-empty id");
                break;
            case "login-has-token":
                String token = response.jsonPath().getString("token");
                Assert.assertNotNull(token, "Expected token in login response");
                Assert.assertFalse(token.trim().isEmpty(), "Expected non-empty token");
                break;
            case "no-content":
                Assert.assertTrue(response.getBody().asString().isEmpty(), "DELETE should return empty body");
                break;
            default:
                throw new IllegalArgumentException("Unsupported assertionType: " + assertionType);
        }
    }

    @Test(description = "Monitoring simulation: call GET /users?page=1 ten times and print min/avg/max response time")
    public void shouldMonitorUsersEndpointOverTenConsecutiveRuns() {
        List<Long> responseTimes = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Response response = given()
                    .spec(requestSpec)
                    .queryParam("page", 1)
                    .when()
                    .get("/users")
                    .andReturn();

            long ms = response.time();
            responseTimes.add(ms);

            System.out.printf("[MONITOR] run=%d GET /users?page=1 -> %dms, status=%d%n",
                    i, ms, response.statusCode());

            Assert.assertEquals(response.statusCode(), 200, "Monitoring call should return 200");
        }

        DoubleSummaryStatistics stats = PerformanceMonitorUtil.summarize(responseTimes);
        PerformanceMonitorUtil.logSummary("GET /users?page=1", stats, responseTimes.size());

        Assert.assertEquals(responseTimes.size(), 10, "Should collect 10 response times");
    }

    private Map<String, Object> createUserBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "SLA User");
        body.put("job", "Performance Tester");
        return body;
    }

    private Map<String, Object> createLoginBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");
        return body;
    }
}
