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

public class GetUsersTest {

    UsersClient usersClient = new UsersClient();
    UsersResponse usersResponse;
    private String id;

    @BeforeClass
    public void setUp() {

        UsersRequest usersRequest = UsersDataFactory.createUser();

        id = usersClient.usersPost(usersRequest)
                .then()
                    .extract()
                        .path("_id")
        ;
    }

    @AfterClass
    public void tearDown() { usersClient.usersDelete(id); }

    @Test
    public void testListUsersSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersGet()
                .then()
                   .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, usersResponse.getStatusCode(), "Status code deve ser 200");
        softAssert.assertFalse(usersResponse.getUsuarios().isEmpty(), "A lista de usuários não deve ser vazia");
        softAssert.assertEquals(usersResponse.getQuantidade(), usersResponse.getUsuarios().size(), "A quantidade deve ser igual ao tamanho da lista");
        softAssert.assertTrue(
                usersResponse.getUsuarios().stream().allMatch(user ->
                        user.getNome() != null && !user.getNome().isEmpty() &&
                        user.getEmail() != null && !user.getEmail().isEmpty() &&
                        user.getPassword() != null && !user.getPassword().isEmpty() &&
                        user.getAdministrador() != null && !user.getAdministrador().isEmpty() &&
                        user.get_id() != null && !user.get_id().isEmpty()
                ), "Todos os usuários devem ter nome, email, password e administrador preenchidos e um id válido"
        );

        softAssert.assertAll();
    }

    @Test
    public void testListUsersByIdSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersIdGet(id)
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, usersResponse.getStatusCode(), "Status code deve ser 200");
        softAssert.assertTrue(usersResponse.getNome() != null && !usersResponse.getNome().isEmpty());
        softAssert.assertTrue(usersResponse.getEmail() != null && !usersResponse.getEmail().isEmpty());
        softAssert.assertTrue(usersResponse.getPassword() != null && !usersResponse.getPassword().isEmpty());
        softAssert.assertTrue(usersResponse.getAdministrador() != null && !usersResponse.getAdministrador().isEmpty());
        softAssert.assertTrue(usersResponse.get_id() != null && !usersResponse.get_id().isEmpty());

        softAssert.assertAll();
    }

    @Test
    public void testListUsersWithEmptyId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersIdGet(UsersDataFactory.empty())
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, usersResponse.getStatusCode(), "Status code deve ser 200");
        softAssert.assertFalse(usersResponse.getUsuarios().isEmpty(), "A lista de usuários não deve ser vazia");
        softAssert.assertEquals(usersResponse.getQuantidade(), usersResponse.getUsuarios().size(), "A quantidade deve ser igual ao tamanho da lista");
        softAssert.assertTrue(
                usersResponse.getUsuarios().stream().allMatch(user ->
                        user.getNome() != null && !user.getNome().isEmpty() &&
                        user.getEmail() != null && !user.getEmail().isEmpty() &&
                        user.getPassword() != null && !user.getPassword().isEmpty() &&
                        user.getAdministrador() != null && !user.getAdministrador().isEmpty() &&
                        user.get_id() != null && !user.get_id().isEmpty()
                ), "Todos os usuários devem ter nome, email, password e administrador preenchidos e um id válido"
        );

        softAssert.assertAll();
    }

    @Test
    public void testListUsersWithInvalidId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = usersClient.usersIdGet(UsersDataFactory.name())
                .then()
                    .extract()
                        .response()
        ;

        usersResponse = response.getBody().as(UsersResponse.class);
        usersResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, usersResponse.getStatusCode(), "Status code deve ser 400");
        softAssert.assertEquals(UsersMessage.USER_NOT_FOUND, usersResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testGetUsersContract() {

        usersClient.usersGet()
                .then()
                    .body(matchesJsonSchemaInClasspath("schemas/getUsersSchema.json"))
        ;
    }
}
