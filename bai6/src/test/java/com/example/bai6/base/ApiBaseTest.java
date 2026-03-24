package com.example.bai6.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class ApiBaseTest {

    protected RequestSpecification requestSpec;

    @BeforeClass(alwaysRun = true)
    public void setUpSpecs() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        String apiKey = System.getProperty("reqres.api.key", System.getenv("REQRES_API_KEY"));
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            builder.addHeader("x-api-key", apiKey.trim());
        }

        requestSpec = builder.build();
    }
}
