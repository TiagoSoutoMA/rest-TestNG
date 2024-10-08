package br.com.rest.tests.users;

import br.com.rest.client.UsersClient;
import br.com.rest.data.factory.UsersDataFactory;
import br.com.rest.model.request.UsersRequest;
import br.com.rest.utils.messages.UsersMessage;
import br.com.rest.model.response.UsersResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PostUsersTest {

    UsersClient usersClient = new UsersClient();
    UsersRequest usersRequest;
    UsersResponse usersResponse;
    String id;

    @AfterMethod
    public void tearDown() { usersClient.usersDelete(id); }

    @Test
    public void testRegisterSucess() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUser();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithEmptyCredentials() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyCredentials();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithEmptyName() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyName();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithEmptyEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyEmail();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithEmptyPassword() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyPassword();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithEmptyAdministrator() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithEmptyAdministrator();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithInvalidAdministrator() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithInvalidAdministrator();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithInvalidEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithInvalidEmail();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterWithDuplicatedEmail() {

        SoftAssert softAssert = new SoftAssert();

        usersRequest = UsersDataFactory.createUserWithDuplicatedEmail();

        Response response = usersClient.usersPost(usersRequest)
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
    public void testRegisterContract() {

        usersRequest = UsersDataFactory.createUser();

        id = usersClient.usersPost(usersRequest)
                .then()
                    .body(matchesJsonSchemaInClasspath("schemas/postUsersSchema.json"))
                        .extract()
                            .path("_id")
        ;
    }
}
