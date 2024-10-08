package br.com.rest.client;

import br.com.rest.model.request.UsersRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UsersClient extends BaseClient {

    private final String USERS = "/usuarios";
    private final String USERS_ID = USERS + "/{id}";

    public Response usersGet() {

        return
                given()
                        .spec(set())
                        .contentType(ContentType.JSON)
                .when()
                        .get(USERS)
        ;
    }

    public Response usersIdGet(String id) {

        return
                given()
                        .spec(set())
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                .when()
                        .get(USERS_ID)
        ;
    }

    public Response usersPost(UsersRequest usersRequest) {

        return
                given()
                        .spec(set())
                        .contentType(ContentType.JSON)
                        .body(usersRequest)
                .when()
                        .post(USERS)
        ;
    }

    public Response usersPut(String id, UsersRequest usersRequest) {

        return
                given()
                        .spec(set())
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(usersRequest)
                .when()
                        .put(USERS_ID)
        ;
    }

    public Response usersDelete(String id) {

        return
                given()
                        .spec(set())
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                .when()
                        .delete(USERS_ID)
        ;
    }
}
