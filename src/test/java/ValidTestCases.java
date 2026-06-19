import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class ValidTestCases extends BaseTest{

    public static String boardID;
    public static String listID;
    private static String cardID;
    private static String checklistID;

    //1

@Test(description = "Create a new Trello board and verify name and ID are returned correctly")
public void createFirstBoard() {

    Response response =
        given()
            .queryParam("name","My first board")
            .queryParam("key",Config.key)
            .queryParam("token",Config.token)
            .queryParam("desc","this is my first board")
            .queryParam("defaultLists",false)
        .when()
            .post("/1/boards/")
        .then()
            .statusCode(200)
            .body("name", equalTo("My first board"))
            .extract().response();

    boardID = response.jsonPath().getString("id");
    System.out.println("Board ID: " + boardID);
}

//2(invalid scenario)

@Test(description = "Create a board with invalid key - should return 401 Unauthorized",
        dependsOnMethods = "createFirstBoard")
public void createBoardWithInvalidKey() {

     given()
        .queryParam("name","My first board")
        .queryParam("key","01021515151")
        .queryParam("token",Config.token)
        .queryParam("desc","this is my first board")
        .queryParam("defaultLists", false)
    .when()
        .post("/1/boards/")
    .then()
        .statusCode(401);

}

//3

    @Test(description = "Get board by ID and verify status, permission level, and required fields exist",
    dependsOnMethods = "createBoardWithInvalidKey")
    public void getBoardById() {

        given()
            .queryParam("key",Config.key)
            .queryParam("token",Config.token)
        .when()
            .get("/1/boards/" + boardID)
        .then()
            .statusCode(200)
            .body("prefs.permissionLevel", equalTo("private"))
            .body("name",notNullValue())
            .body("desc",notNullValue())
            .body("url",notNullValue());
    }

    //4

    @Test(description = "Create a new list inside the board and verify name and closed fields exist",
    dependsOnMethods = "getBoardById")
    public void createList() {

    Response response =
        given()
            .queryParam("name","my list")
            .queryParam("idBoard",boardID)
            .queryParam("key",Config.key)
            .queryParam("token",Config.token)
        .when()
            .post("/1/lists")
        .then()
            .statusCode(200)
            .body("name",notNullValue())
            .body("closed",notNullValue())
            .extract().response();

        listID = response.jsonPath().getString("id");
        System.out.println("List ID: " + listID);
    }

    //5(invalid scenario)

    @Test(description = "Create a list with invalid board ID and verify status code is 400",
            dependsOnMethods = "createList")
    public void createListWithInvalidBoardId() {

        given()
            .queryParam("name",    "my list")
            .queryParam("idBoard", "1215515151")
            .queryParam("key",     Config.key)
            .queryParam("token",   Config.token)
        .when()
            .post("/1/lists")
        .then()
            .statusCode(400);
    }

    //6

    @Test(description = "Get list by ID and verify name and closed fields exist",
            dependsOnMethods = "createListWithInvalidBoardId")
    public void getListById() {

        given()
            .queryParam("key", Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/lists/" + listID)
        .then()
            .statusCode(200)
            .body("name", notNullValue())
            .body("closed", notNullValue());
    }

    //7

    @Test(description = "Create a new card inside the list and verify status code is 200",
            dependsOnMethods = "getListById")
    public void createCard() {

    Response response =

        given()
            .queryParam("idList", listID)
            .queryParam("key",Config.key)
            .queryParam("token",Config.token)
            .queryParam("name","myfirstCard")
            .queryParam("desc","this is my first card")
        .when()
            .post("/1/cards")
        .then()
            .statusCode(200)
            .body("name", equalTo("myfirstCard"))
            .extract().response();

        cardID = response.jsonPath().getString("id");
        System.out.println("Card ID: " + cardID);
    }

    //8 (invalid scenario)

    @Test(description = "Create a card with invalid token and verify status code is 401",
            dependsOnMethods = "createCard")
    public void createCardWithInvalidToken() {

        given()
            .queryParam("idList", listID)
            .queryParam("key",Config.key)
            .queryParam("token","112151515151")
            .queryParam("name","myfirstCard")
            .queryParam("desc","this is my first card")
        .when()
            .post("/1/cards")
        .then()
            .statusCode(401);
    }


    //9

    @Test(description = "Get card by ID and verify status code and desc field",
            dependsOnMethods = "createCardWithInvalidToken")
    public void getCardById() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/cards/" + cardID)
        .then()
            .statusCode(200)
            .body("desc", equalTo("this is my first card"));

    }

    //10

    @Test(description = "Create a checklist inside the card and verify status code is 200",
            dependsOnMethods = "getCardById")
    public void createChecklist() {
        Response response =
            given()
                .queryParam("idCard", cardID)
                .queryParam("key",    Config.key)
                .queryParam("token",  Config.token)
                .queryParam("name",   "my first check list")
            .when()
                .post("/1/checklists")
            .then()
                .statusCode(200)
                .body("name", equalTo("my first check list"))
                .extract().response();

        checklistID = response.jsonPath().getString("id");
        System.out.println("Checklist ID: " + checklistID);
    }

    //11(invalid scenario)

    @Test(description = "Create a checklist with invalid card ID and verify status code is 400",
            dependsOnMethods = "createChecklist")
    public void createChecklistWithInvalidCardId() {

        given()
            .queryParam("idCard", "457575757")
            .queryParam("key",    Config.key)
            .queryParam("token",  Config.token)
            .queryParam("name",   "my first check list")
        .when()
            .post("/1/checklists")
        .then()
            .statusCode(400);
    }

    //12

    @Test(description = "Get checklist by ID and verify status, idBoard, idCard and name",
            dependsOnMethods = "createChecklistWithInvalidCardId")
    public void getChecklistById() {

        given()
            .queryParam("key",Config.key)
            .queryParam("token",Config.token)
        .when()
            .get("/1/checklists/" + checklistID)
        .then()
            .statusCode(200)
            .body("idBoard",equalTo(boardID))
            .body("idCard",equalTo(cardID))
            .body("name",equalTo("my first check list"));

    }

    //13

    @Test(description = "Update board name to abdoo and verify the new name is returned",
            dependsOnMethods = "getChecklistById")
    public void updateBoardName() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
            .queryParam("name",  "abdoo")
        .when()
            .put("/1/boards/" + boardID)
        .then()
            .statusCode(200)
            .body("name", equalTo("abdoo"));

    }

    //14

    @Test(description = "Get board by ID and verify name is updated to abdoo",
            dependsOnMethods = "updateBoardName")
    public void getBoardAfterUpdate() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/boards/" + boardID)
        .then()
            .statusCode(200)
            .body("name", equalTo("abdoo"));

    }


    //15

    @Test(description = "Delete checklist by ID and verify status code is 200",
            dependsOnMethods = "getBoardAfterUpdate")
    public void deleteChecklist() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .delete("/1/checklists/" + checklistID)
        .then()
            .statusCode(200);
    }

    //16(Invalid Scenario)

    @Test(description = "Get deleted checklist by ID and verify it returns 404 not found",
            dependsOnMethods = "deleteChecklist")
    public void getDeletedChecklist() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/checklists/" + checklistID)
        .then()
            .statusCode(404);
    }

    //17

    @Test(description = "Delete card by ID and verify status code is 200",
            dependsOnMethods = "getDeletedChecklist")
    public void deleteCard() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .delete("/1/cards/" + cardID)
        .then()
            .statusCode(200);
    }

    //18(Invalid Scenario)

    @Test(description = "Get deleted card by ID and verify it returns 404 not found",
            dependsOnMethods = "deleteCard")
    public void getDeletedCard() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/cards/" + cardID)
        .then()
            .statusCode(404);
    }

    //19

    @Test(description = "Archive list using query param and verify status code is 200",
            dependsOnMethods = "getDeletedCard")
    public void archiveList() {

        given()
            .queryParam("key",    Config.key)
            .queryParam("token",  Config.token)
            .queryParam("closed", true)
        .when()
            .put("/1/lists/" + listID)
        .then()
            .statusCode(200)
            .body("closed", equalTo(true));
    }

    //20

    @Test(description = "Get archived list by ID and verify closed is true",
            dependsOnMethods = "archiveList")
    public void getArchivedList() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/lists/" + listID)
        .then()
            .statusCode(200)
            .body("closed", equalTo(true));
    }

    //21

    @Test(description = "Delete board by ID and verify status code is 200",
            dependsOnMethods = "getArchivedList")
    public void deleteBoard() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .delete("/1/boards/" + boardID)
        .then()
            .statusCode(200);
    }

    //22(Invalid Scenario)

    @Test(description = "Get list after board deletion and verify it returns 404 not found",
            dependsOnMethods = "deleteBoard")
    public void getDeletedList() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/lists/" + listID)
        .then()
            .statusCode(404);
    }

    //23(Invalid Scenario)

    @Test(description = "Get deleted board by ID and verify it returns 404 not found",
            dependsOnMethods = "getDeletedList")
    public void getDeletedBoard() {

        given()
            .queryParam("key",   Config.key)
            .queryParam("token", Config.token)
        .when()
            .get("/1/boards/" + boardID)
        .then()
            .statusCode(404);
    }
    }




