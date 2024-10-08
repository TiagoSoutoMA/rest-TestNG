package br.com.rest.tests.users;

import br.com.rest.client.UsersClient;
import br.com.rest.data.factory.UsersDataFactory;
import br.com.rest.model.request.UsersRequest;
import br.com.rest.model.response.UsersResponse;
import br.com.rest.utils.messages.UsersMessage;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class DeleteUsersTest {

    UsersClient usersClient = new UsersClient();
    UsersRequest usersRequest;
    UsersResponse usersResponse;
    private String id;

    @BeforeMethod
    public void setUp() {

        usersRequest = UsersDataFactory.createUser();

        id = usersClient.usersPost(usersRequest)
                .then()
                    .extract()
                        .path("_id")
        ;
    }

    @AfterMethod
    public void tearDown() {

        if (id != null) {
            usersClient.usersDelete(id);
        }
    }

    @Test()
    public void testDeleteUserSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersDelete(id)
                .then()
                   .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(usersResponse.getStatusCode(), HttpStatus.SC_OK);
        softAssert.assertEquals(usersResponse.getMessage(), UsersMessage.DELETE_SUCESS);

        softAssert.assertAll();
    }

    @Test
    public void testDeleteUserIncorrectId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersDelete(UsersDataFactory.name())
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.DELETE_FAIL, usersResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testDeleteUserRegisteredCart() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersDelete(UsersDataFactory.idAdmin())
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(usersResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
        softAssert.assertEquals(usersResponse.getMessage(), UsersMessage.DELETE_FAIL_CART);
        softAssert.assertNotNull(usersResponse.getIdCarrinho());

        softAssert.assertAll();
    }

    @Test
    public void testDeleteContract() {

        usersClient.usersDelete(id)
                .then()
                    .body(matchesJsonSchemaInClasspath("schemas/deleteSchema.json"))
        ;
    }
}
