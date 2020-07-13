import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.http.entity.InputStreamEntity;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestAssuredTest {
    private static final String ENDPOINT = "https://rest-assured-test.requestcatcher.com";

    @Test
    public void a_testWithoutConnectionReusing() {
        RestAssured.config = new RestAssuredConfig();

        long total = 0;

        for (int i = 0; i < 10; i++) {
            final Response response = given()
                    .header("Content-Type", "application/json")
                    .body(String.format("{ message: 'Test without reusing #%d' }", i))
                    .get(ENDPOINT);

            response.then().statusCode(200);

            total += response.time();
        }

        System.out.println("Total without reusing: " + total);
    }

    @Test
    public void b_testWithConnectionReusing() {
        // create config for all tests with connection reusing
        RestAssured.config = RestAssuredConfig.newConfig().httpClient(
                HttpClientConfig.httpClientConfig().reuseHttpClientInstance());

        long total = 0;

        for (int i = 0; i < 10; i++) {
            final Response response = given()
                    .header("Content-Type", "application/json")
                    .body(String.format("{ message: 'Test with reusing #%d' }", i))
                    .get(ENDPOINT);

            response.then().statusCode(200);

            // you have to consume input stream after each request
            // ('response' var is an instance of io.restassured.response.Response)
            org.apache.http.util.EntityUtils.consumeQuietly(new InputStreamEntity(response.asInputStream()));

            total += response.time();
        }

        System.out.println("Total with reusing: " + total);
    }
}
