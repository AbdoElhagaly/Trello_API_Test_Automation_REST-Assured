import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class BaseTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.trello.com";
    }

    @AfterClass
    public void tearDown() {
        RestAssured.reset();
    }
}

