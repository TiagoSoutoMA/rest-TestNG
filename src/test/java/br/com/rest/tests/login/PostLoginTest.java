package br.com.rest.tests.login;

import br.com.rest.client.LoginClient;
import br.com.rest.data.factory.LoginDataFactory;
import br.com.rest.model.request.LoginRequest;
import br.com.rest.model.response.LoginResponse;
import br.com.rest.utils.messages.LoginMessage;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PostLoginTest {

    LoginClient loginClient = new LoginClient();
    LoginResponse loginResponse;
    LoginRequest loginRequest;

    @Test
    public void testLoginSucess() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginAdmin();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.SUCESS, loginResponse.getMessage());
        softAssert.assertNotNull(loginResponse.getAuthorization());

        softAssert.assertAll();
    }

    @Test
    public void testLoginCredentialsNotRegistered() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginCredentialsNotRegistered();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_UNAUTHORIZED, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.CREDENTIALS_NOT_REGISTERED, loginResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testLoginInvalidEmail() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginInvalidEmail();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.INVALID_EMAIL_FORMAT, loginResponse.getEmail());

        softAssert.assertAll();
    }

    @Test
    public void testLoginEmptyEmail() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginEmptyEmail();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.EMPTY_EMAIL, loginResponse.getEmail());

        softAssert.assertAll();
    }

    @Test
    public void testLoginEmptyPassword() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginEmptyPassword();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.EMPTY_PASSWORD, loginResponse.getPassword());

        softAssert.assertAll();
    }

    @Test
    public void testLoginEmptyCredentials() {

        SoftAssert softAssert = new SoftAssert();

        loginRequest = LoginDataFactory.loginEmptyCredentials();

        Response response = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .response()
        ;

        loginResponse = response.getBody().as(LoginResponse.class);
        loginResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, loginResponse.getStatusCode());
        softAssert.assertEquals(LoginMessage.EMPTY_EMAIL, loginResponse.getEmail());
        softAssert.assertEquals(LoginMessage.EMPTY_PASSWORD, loginResponse.getPassword());

        softAssert.assertAll();
    }

    @Test
    public void testLoginContract() {

        loginRequest = LoginDataFactory.loginAdmin();

        loginClient.loginPost(loginRequest)
                .then()
                   .body(matchesJsonSchemaInClasspath("schemas/postLoginSchema.json"))
        ;
    }
}
