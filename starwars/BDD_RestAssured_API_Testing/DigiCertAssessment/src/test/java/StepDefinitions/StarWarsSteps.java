package StepDefinitions;

import base.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.qameta.allure.SeverityLevel.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static utility.BrowserDriver.takeScreenshot;

@Feature("Films")
public class StarWarsSteps extends BaseTest {

    private JsonPath json;
    private final static int FILMS_COUNT = 6;
    private final static String DIRECTOR = "Richard Marquand";
    private final static String PRODUCER1 = "Gary Kurtz";
    private final static String PRODUCER2 = "George Lucas";

    private WebDriver driver;
    private Response response;
    private RequestSpecification request;

    @Test
    @Severity(BLOCKER)
    @Given("I have access to the Star Wars API")
    public void i_have_access_to_the_star_wars_api() {
        RestAssured.baseURI = "https://swapi.dev/api/";
        request = RestAssured.given();
    }

    @Test
    @Severity(BLOCKER)
    @When("I send a GET request to the films endpoint")
    public void i_send_a_get_request_to_the_films_endpoint() {

        Response response = given()
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(FILMS_COUNT);
        takeScreenshot(driver, "films_count");

    }
    @Test
    @Severity(NORMAL)
    @When("I send a GET request for the movie {int}")
    public void i_send_a_get_request_for_the_movie(Integer MOVIE_ID) {
        MOVIE_ID = 5;
        Response response = given()
                .pathParam("id", 5)
                .when()
                .get(BASE_URL + FILMS + "/{id}")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getString("producer")).isNotEqualTo(PRODUCER1);
        assertThat(json.getString("producer")).isNotEqualTo(PRODUCER2);
        takeScreenshot(driver, "films_count");

    }
    @Test
    @Severity(MINOR)
    @When("I send a GET request for non existing movie")
    public void i_send_a_get_request_for_non_existing_movie() {

        String invalidTitle = "I don't exist";
        Response response = given()
                .queryParam("search", invalidTitle)
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getList("results").size()).isEqualTo(0);
    }

    @Then("I should receive a response with status code {int}")
    public void i_should_receive_a_response_with_status_code(Integer SC_OK) {
        Response response = given()
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(FILMS_COUNT);

    }

    @And("The response should contain {int} movies")
    public void the_response_should_contain_movies(Integer FILMS_COUNT) {

        Response response = given()
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(FILMS_COUNT);

    }

    @Given("I am on the Star Wars movie list page")
    public void i_am_on_the_star_wars_movie_list_page() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdriver\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/");
    }

    @When("I sort the movies by title")
    public void i_sort_the_movies_by_title() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement titleHeader = driver.findElement(By.xpath("//th[normalize-space()='Title']"));
        titleHeader.click();
    }

    @Then("the last movie in the list should be {string}")
    public void the_last_movie_in_the_list_should_be(String lastMovie) {
        List<WebElement> movies = driver.findElements(By.cssSelector("tr"));
        WebElement lastRow = movies.get(movies.size() - 1);
        String actualLastMovie = lastRow.findElement(By.xpath("//a[normalize-space()='The Phantom Menace']")).getText();
        Assert.assertEquals(lastMovie, actualLastMovie);
    }

    @When("I select the movie {string}")
    public void i_select_the_movie(String movieTitle) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement movieLink = driver.findElement(By.linkText(movieTitle));
        movieLink.click();
    }

    @Then("the species list should contain {string}")
    public void the_species_list_should_contain(String species) {
        List<WebElement> speciesList = driver.findElements(By.xpath("//li[normalize-space()='Wookie']"));
        boolean speciesInList = false;
        for (WebElement speciesElement : speciesList) {
            if (speciesElement.getText().equals(species)) {
                speciesInList = true;
                break;
            }
        }
        Assert.assertTrue(speciesInList);
    }

    @Then("the planets list should not contain {string}")
    public void the_planets_list_should_not_contain(String planet) {
        List<WebElement> planetsList = driver.findElements(By.xpath("//div[@class='layout_lists__rBjPn']//div[2]//ul[1]"));
        boolean planetInList = false;
        for (WebElement planetElement : planetsList) {
            if (planetElement.getText().equals(planet)) {
                planetInList = true;
                break;
            }
        }
        Assert.assertFalse(planetInList);
    }

    @And("The director\\/s of the movie is\\/are {string}")
    public void the_director_s_of_the_movie_is_are(String DIRECTOR) {
        Response response = given()
                .pathParam("id", 3)
                .when()
                .get(BASE_URL + FILMS + "/{id}")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getString("director")).isEqualTo(DIRECTOR);
        compareFilmObject("director");
    }

    @And("The producer\\/s of the movie is\\/are not {string} and {string}")
    public void the_producer_s_of_the_movie_is_are_not_and(String PRODUCER1, String PRODUCER2) {
        Response response = given()
                .pathParam("id", 5)
                .when()
                .get(BASE_URL + FILMS + "/{id}")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
        assertThat(json.getString("producer1")).isNotEqualTo(PRODUCER1);
        assertThat(json.getString("producer2")).isNotEqualTo(PRODUCER2);
        compareFilmObject("producer1");
        compareFilmObject("producer2");
    }

    private void compareFilmObject(String objectPath) {

        SoftAssert softAssert = new SoftAssert();
        //softAssert.assertEquals(json.getString(objectPath + "title"),TITLE);
        //softAssert.assertEquals(json.getInt(objectPath + "episode_id"),EPISODE_ID);
        //softAssert.assertEquals(json.getString(objectPath + "opening_crawl"),OPENING_CRAWL);
        softAssert.assertEquals(json.getString(objectPath + "director"),DIRECTOR);
        softAssert.assertEquals(json.getString(objectPath + "producer1"),PRODUCER1);
        softAssert.assertEquals(json.getString(objectPath + "producer2"),PRODUCER2);
        //softAssert.assertEquals(json.getString(objectPath + "release_date"),RELEASE_DATE);
        //softAssert.assertEquals(json.getList(objectPath + "characters").size(),CHARACTERS_COUNT);
        //softAssert.assertEquals(json.getList(objectPath + "planets").size(),PLANETS_COUNT);
        //softAssert.assertEquals(json.getList(objectPath + "starships").size(),STARSHIPS_COUNT);
        //softAssert.assertEquals(json.getList(objectPath + "vehicles").size(),VEHICLES_COUNT);
        //softAssert.assertEquals(json.getList(objectPath + "species").size(),SPECIES_COUNT);
        softAssert.assertAll();
    }

    // Close the browser
    @After
    public void close_browser() {
        if (driver != null) {
            driver.quit();
        }
    }
}