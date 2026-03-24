package com.example.bai4.tests;

import com.example.bai4.base.ApiBaseTest;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorizationErrorHandlingTest extends ApiBaseTest {

    @Test(description = "Login success: POST /api/login with valid email/password returns 200 and non-empty token")
    public void shouldLoginSuccessfullyWithValidCredentials() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body("token", notNullValue())
                .body("token", not(isEmptyOrNullString()));
    }

    @Test(description = "Login missing password: POST /api/login returns 400 with error 'Missing password'")
    public void shouldReturnMissingPasswordWhenLoginWithoutPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .spec(responseSpec)
                .body("error", containsString("Missing password"));
    }

    @Test(description = "Login missing email: POST /api/login returns 400 with error 'Missing email or username'")
    public void shouldReturnMissingEmailWhenLoginWithoutEmail() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "cityslicka");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .spec(responseSpec)
                .body("error", containsString("Missing email or username"));
    }

    @Test(description = "Register success: POST /api/register returns 200 with id and token")
    public void shouldRegisterSuccessfully() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body("id", notNullValue())
                .body("token", notNullValue())
                .body("token", not(isEmptyOrNullString()));
    }

    @Test(description = "Register missing password: POST /api/register returns 400 with error 'Missing password'")
    public void shouldReturnMissingPasswordWhenRegisterWithoutPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .spec(responseSpec)
                .body("error", containsString("Missing password"));
    }

    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][] {
                { "eve.holt@reqres.in", "cityslicka", 200, null },
                { "eve.holt@reqres.in", "", 400, "Missing password" },
                { "", "cityslicka", 400, "Missing email or username" },
                { "notexist@reqres.in", "wrongpass", 400, "user not found" },
                { "invalid-email", "pass123", 400, "user not found" }
        };
    }

    @Test(dataProvider = "loginScenarios", description = "Data-driven login scenarios: verify status and error handling across valid/invalid inputs")
    public void shouldHandleLoginScenarios(String email, String password, int expectedStatus, String expectedError) {
        Map<String, String> body = new HashMap<>();
        if (email != null && !email.isEmpty()) {
            body.put("email", email);
        }
        if (password != null && !password.isEmpty()) {
            body.put("password", password);
        }

        ValidatableResponse response = given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(expectedStatus)
                .spec(responseSpec);

        if (expectedError != null) {
            response.body("error", containsString(expectedError));
        } else {
            response.body("token", notNullValue())
                    .body("token", not(isEmptyOrNullString()));
        }
    }
}
