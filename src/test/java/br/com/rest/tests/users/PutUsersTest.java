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

public class PutUsersTest {

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

    @Test
    public void testEdiUserSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUser();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());
        id = usersResponse.get_id();

        softAssert.assertEquals(HttpStatus.SC_OK, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.RECORD_CHANGED, usersResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithIncorrectId() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUser();

        Response response = usersClient.usersPut(UsersDataFactory.name(), usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());
        id = usersResponse.get_id();

        softAssert.assertEquals(HttpStatus.SC_CREATED, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.SUCESS, usersResponse.getMessage());
        softAssert.assertNotNull(usersResponse.get_id());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithEmptyCredentials() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyCredentials();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.EMPTY_NAME, usersResponse.getNome());
        softAssert.assertEquals(UsersMessage.EMPTY_EMAIL, usersResponse.getEmail());
        softAssert.assertEquals(UsersMessage.EMPTY_PASSWORD, usersResponse.getPassword());
        softAssert.assertEquals(UsersMessage.INCORRECT_ADMINISTRATOR, usersResponse.getAdministrador());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithEmptyName() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyName();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.EMPTY_NAME, usersResponse.getNome());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithEmptyEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyEmail();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.EMPTY_EMAIL, usersResponse.getEmail());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithEmptyPassword() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyPassword();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.EMPTY_PASSWORD, usersResponse.getPassword());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithEmptyAdministrator() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyAdministrator();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.INCORRECT_ADMINISTRATOR, usersResponse.getAdministrador());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithInvalidAdministrator() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithInvalidAdministrator();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.INCORRECT_ADMINISTRATOR, usersResponse.getAdministrador());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithInvalidEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithInvalidEmail();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.INVALID_EMAIL, usersResponse.getEmail());

        softAssert.assertAll();
    }

    @Test
    public void testEdiUserWithDuplicatedEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithDuplicatedEmail();

        Response response = usersClient.usersPut(id, usersRequest)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode());
        softAssert.assertEquals(UsersMessage.DUPLICATED_EMAIL, usersResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testEditContract() {

        usersRequest = UsersDataFactory.createUser();

        id = usersClient.usersPut(id, usersRequest)
                .then()
                    .body(matchesJsonSchemaInClasspath("schemas/putUsersSchema.json"))
                        .extract()
                            .path("_id")
        ;
    }
}
