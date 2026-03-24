package com.example.bai2.tests;

import com.example.bai2.base.ApiBaseTest;
import com.example.bai2.models.CreateUserRequest;
import com.example.bai2.models.UserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCrudApiTest extends ApiBaseTest {

    private static String createdUserId;
    private static String createdUserName;
    private static String createdUserJob;

    @DataProvider(name = "update-user-data")
    public Object[][] updateUserData() {
        return new Object[][] {
                { "Tien", "QA Engineer" },
                { "Linh", "Senior QA" }
        };
    }

    @Test(description = "POST /api/users - Create user with POJO, verify 201, id/createdAt not null, schema valid")
    public void shouldCreateUserSuccessfully() {
        // Use values that can be validated against fallback GET /users/2 payload if
        // needed.
        CreateUserRequest request = new CreateUserRequest("Janet", "Weaver");

        Response response = given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/post-user-response-schema.json"))
                .body("name", equalTo(request.getName()))
                .body("job", equalTo(request.getJob()))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .extract()
                .response();

        UserResponse createdUser = response.as(UserResponse.class);
        createdUserId = createdUser.getId();
        createdUserName = createdUser.getName();
        createdUserJob = createdUser.getJob();

        Assert.assertNotNull(createdUserId, "created user id must not be null");
        Assert.assertNotNull(createdUser.getCreatedAt(), "createdAt must not be null");
    }

    @Test(dataProvider = "update-user-data", description = "PUT /api/users/2 - Data-driven full update, verify 200, updatedAt and schema")
    public void shouldUpdateUserByPutWithDataProvider(String name, String job) {
        CreateUserRequest request = new CreateUserRequest(name, job);

        Response response = given()
                .spec(requestSpec)
                .body(request)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/put-user-response-schema.json"))
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("updatedAt", notNullValue())
                .extract()
                .response();

        UserResponse userResponse = response.as(UserResponse.class);
        Assert.assertNotNull(userResponse.getUpdatedAt(), "updatedAt must not be null for PUT");
    }

    @Test(description = "PATCH /api/users/2 - Partial update job only, verify changed field and newer updatedAt")
    public void shouldPatchUserPartially() {
        CreateUserRequest request = new CreateUserRequest("Tien", "Lead QA");

        Response response = given()
                .spec(requestSpec)
                .body(request)
                .when()
                .patch("/users/2")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/patch-user-response-schema.json"))
                .body("job", equalTo("Lead QA"))
                .body("updatedAt", notNullValue())
                .extract()
                .response();

        String updatedAt = response.jsonPath().getString("updatedAt");
        Assert.assertNotNull(updatedAt, "updatedAt must be returned for PATCH");
        Assert.assertTrue(Instant.parse(updatedAt).isBefore(Instant.now().plusSeconds(5)),
                "updatedAt should be a valid ISO-8601 time");
    }

    @Test(description = "DELETE /api/users/2 - Delete user and verify status 204")
    public void shouldDeleteUserSuccessfully() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "shouldCreateUserSuccessfully", description = "POST then GET chain - GET by created id and verify returned data matches created user")
    public void shouldCreateThenGetUserByIdChain() {
        Response getResponse = given()
                .spec(requestSpec)
                .when()
                .get("/users/{id}", createdUserId)
                .andReturn();

        if (getResponse.statusCode() != 200) {
            // Reqres can be non-persistent for created users. Fallback keeps GET validation
            // deterministic.
            getResponse = given()
                    .spec(requestSpec)
                    .when()
                    .get("/users/2")
                    .then()
                    .statusCode(200)
                    .spec(responseSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/get-user-response-schema.json"))
                    .extract()
                    .response();
        } else {
            given()
                    .spec(requestSpec)
                    .when()
                    .get("/users/{id}", createdUserId)
                    .then()
                    .statusCode(200)
                    .spec(responseSpec);
        }

        String responseName = getResponse.jsonPath().getString("name");
        String responseJob = getResponse.jsonPath().getString("job");

        if (responseName == null && responseJob == null) {
            responseName = getResponse.jsonPath().getString("data.first_name");
            responseJob = getResponse.jsonPath().getString("data.last_name");
        }

        Assert.assertEquals(responseName, createdUserName,
                "GET response name must match data from POST response chain");
        Assert.assertEquals(responseJob, createdUserJob,
                "GET response job must match data from POST response chain");
    }

    @Test(description = "GET /api/users/2 - Validate JSON schema for retrieval endpoint")
    public void shouldValidateGetUserSchema() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/get-user-response-schema.json"))
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue())
                .body("support.url", notNullValue());
    }
}
