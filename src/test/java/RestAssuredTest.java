import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredTest {
    private static final String ENDPOINT = "https://96af19d2b58e5876c858b28e05e60c81.m.pipedream.net";

    @Test
    public void testWithoutConnectionReusing() {
        long total = 0;

        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();

            given()
                    .header("Content-Type", "application/json")
                    .body("{ message: 'Test' }")
                    .get(ENDPOINT)
                    .then()
                    .statusCode(200);

            total += System.currentTimeMillis() - start;
        }

        System.out.println("Total: " + total);
    }
}
