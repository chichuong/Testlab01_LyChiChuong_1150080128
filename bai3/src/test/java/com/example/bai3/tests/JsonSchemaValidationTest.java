package com.example.bai3.tests;

import com.example.bai3.base.ApiBaseTest;
import com.example.bai3.models.CreateUserRequest;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JsonSchemaValidationTest extends ApiBaseTest {

    private static final String INTENTIONAL_FAIL_FLAG = "run.intentional.failures";

    @Test(description = "Validate schema GET /api/users with user-list-schema.json")
    public void shouldValidateUserListSchema() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
    }

    @Test(description = "Validate schema GET /api/users/2 with user-schema.json")
    public void shouldValidateSingleUserSchema() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(description = "Validate schema POST /api/users with create-user-schema.json")
    public void shouldValidateCreateUserSchema() {
        CreateUserRequest request = new CreateUserRequest("Tien", "Tester");

        given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }

    @Test(description = "Demo FAIL message for unexpected extra field when additionalProperties=false")
    public void shouldShowFailureMessageForExtraField() {
        String invalidJsonWithExtraField = "{\"name\":\"Tien\",\"job\":\"Tester\",\"id\":\"99\",\"createdAt\":\"2026-01-01T00:00:00Z\",\"extraField\":\"not-allowed\"}";

        AssertionError error = Assert.expectThrows(AssertionError.class, () -> MatcherAssert.assertThat(
                invalidJsonWithExtraField,
                matchesJsonSchemaInClasspath("schemas/create-user-schema.json")));

        String message = error.getMessage();
        Assert.assertTrue(
                message != null && (message.contains("extraField") || message.contains("additional properties")),
                "Expected failure message to mention unexpected field (thua field)");
    }

    @Test(description = "Demo FAIL message for missing required field in expected schema")
    public void shouldShowFailureMessageForMissingField() {
        String invalidJsonMissingField = "{\"name\":\"Tien\",\"job\":\"Tester\",\"createdAt\":\"2026-01-01T00:00:00Z\"}";

        AssertionError error = Assert.expectThrows(AssertionError.class, () -> MatcherAssert.assertThat(
                invalidJsonMissingField,
                matchesJsonSchemaInClasspath("schemas/create-user-schema.json")));

        String message = error.getMessage();
        Assert.assertTrue(message != null && (message.contains("id") || message.contains("required")),
                "Expected failure message to mention missing field (thieu field)");
    }

    @Test(description = "Intentional FAIL demo: extra field must break strict schema when additionalProperties=false")
    public void shouldFailIntentionallyForExtraField() {
        if (!Boolean.getBoolean(INTENTIONAL_FAIL_FLAG)) {
            throw new SkipException("Set -Drun.intentional.failures=true to execute intentional failing demos.");
        }

        String invalidJsonWithExtraField = "{\"name\":\"Tien\",\"job\":\"Tester\",\"id\":\"99\",\"createdAt\":\"2026-01-01T00:00:00Z\",\"extraField\":\"not-allowed\"}";
        MatcherAssert.assertThat(
                invalidJsonWithExtraField,
                matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }

    @Test(description = "Intentional FAIL demo: missing required field must break strict schema")
    public void shouldFailIntentionallyForMissingField() {
        if (!Boolean.getBoolean(INTENTIONAL_FAIL_FLAG)) {
            throw new SkipException("Set -Drun.intentional.failures=true to execute intentional failing demos.");
        }

        String invalidJsonMissingField = "{\"name\":\"Tien\",\"job\":\"Tester\",\"createdAt\":\"2026-01-01T00:00:00Z\"}";
        MatcherAssert.assertThat(
                invalidJsonMissingField,
                matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }
}
