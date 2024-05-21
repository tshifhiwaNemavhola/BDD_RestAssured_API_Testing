package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static final String BASE_URL = "https://swapi.dev/api/";
    protected static final String FILMS = "films";
    protected static RequestSpecification requestSpecification;

    @BeforeSuite
    public void setup() {

        RestAssured.useRelaxedHTTPSValidation();
        requestSpecification = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .build();
    }
}