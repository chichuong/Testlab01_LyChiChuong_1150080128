package com.example.bai2.tests;

import com.example.bai2.base.ApiBaseTest;
import com.example.bai2.models.CreateUserRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiPreconditionUiGateTest extends ApiBaseTest {

    private boolean canRunUiTest;

    @BeforeClass(alwaysRun = true)
    public void verifyApiPreconditionForUiFlow() {
        CreateUserRequest request = new CreateUserRequest("Tien", "UI Gate");

        Response response = given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post("/users")
                .andReturn();

        canRunUiTest = response.getStatusCode() == 201;
        if (!canRunUiTest) {
            throw new SkipException("API precondition failed, skip UI test. POST /users did not return 201.");
        }
    }

    @Test(description = "UI test runs only when API precondition is met")
    public void shouldRunUiTestWhenApiPreconditionIsMet() {
        Assert.assertTrue(canRunUiTest,
                "UI scenario must run only when API precondition from POST /users is successful");

        // Minimal placeholder assertion for UI gate demonstration.
        Assert.assertTrue(true, "UI test passed because API precondition allowed execution");
    }
}
