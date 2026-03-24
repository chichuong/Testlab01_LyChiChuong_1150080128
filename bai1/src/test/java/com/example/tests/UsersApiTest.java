package com.example.tests;

import com.example.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UsersApiTest extends ApiBaseTest {

    @Test(description = "GET /api/users?page=1 -> status 200, page=1, total_pages>0, data.size>1")
    public void testGetUsersPage1() {
        given()
                .spec(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .body("page", org.hamcrest.Matchers.equalTo(1))
                .body("total_pages", org.hamcrest.Matchers.greaterThan(0))
                .body("data.size()", org.hamcrest.Matchers.greaterThan(1));
    }

    @Test(description = "GET /api/users?page=2 -> page=2, mỗi user có đủ id,email,first_name,last_name,avatar")
    public void testGetUsersPage2Schema() {
        Response response = given()
                .spec(requestSpec)
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .extract()
                .response();

        Integer page = response.jsonPath().getInt("page");
        org.testng.Assert.assertEquals(page, Integer.valueOf(2), "Page phải bằng 2");

        List<Map<String, Object>> users = response.jsonPath().getList("data");
        for (Map<String, Object> user : users) {
            assertTrue(user.containsKey("id"), "Thiếu field id");
            assertTrue(user.containsKey("email"), "Thiếu field email");
            assertTrue(user.containsKey("first_name"), "Thiếu field first_name");
            assertTrue(user.containsKey("last_name"), "Thiếu field last_name");
            assertTrue(user.containsKey("avatar"), "Thiếu field avatar");
        }
    }

    @Test(description = "GET /api/users/3 -> id=3, email đúng định dạng @reqres.in, first_name không rỗng")
    public void testGetUserById3() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/users/3")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .extract()
                .response();

        Integer id = response.jsonPath().getInt("data.id");
        String email = response.jsonPath().getString("data.email");
        String firstName = response.jsonPath().getString("data.first_name");

        org.testng.Assert.assertEquals(id, Integer.valueOf(3), "id phải bằng 3");
        assertTrue(email != null && email.endsWith("@reqres.in"), "Email phải kết thúc bằng @reqres.in");
        assertFalse(firstName == null || firstName.trim().isEmpty(), "first_name không được rỗng");
    }

    @Test(description = "GET /api/users/9999 -> status 404 và body là object rỗng {}")
    public void testGetUserNotFound() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/users/9999")
                .then()
                .statusCode(404)
                .spec(responseSpec)
                .extract()
                .response();

        // Body {} sẽ map thành map rỗng khi parse JSON.
        Map<String, Object> body = response.jsonPath().getMap("$");
        org.testng.Assert.assertNotNull(body, "Body không được null");
        org.testng.Assert.assertTrue(body.isEmpty(), "Body phải là object rỗng {}");
    }
}
