package br.com.rest.tests.cart;

import br.com.rest.client.CartClient;
import br.com.rest.data.factory.CartDataFactory;
import br.com.rest.model.response.CartResponse;
import br.com.rest.utils.messages.CartMessage;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class GetCartTest {

    private final CartClient cartClient = new CartClient();
    private CartResponse cartResponse;

    @Test
    public void testListCartSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = cartClient.cartGet()
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode(), "Status code deve ser 200");
        softAssert.assertFalse(cartResponse.getCarrinhos().isEmpty(), "A lista de usuários não deve ser vazia");
        softAssert.assertEquals(cartResponse.getQuantidade(), cartResponse.getCarrinhos().size(), "A quantidade deve ser igual ao tamanho da lista");

        softAssert.assertAll();
    }

    @Test
    public void testListCartByIdSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = cartClient.cartIdGet(CartDataFactory.idCarrinho())
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode(), "Status code deve ser 200");
        softAssert.assertFalse(cartResponse.getProdutos().isEmpty(), "A lista de usuários não deve ser vazia");

        softAssert.assertAll();
    }

    @Test
    public void testListCartIncorrectId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = cartClient.cartIdGet(CartDataFactory.name())
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, cartResponse.getStatusCode(), "Status code deve ser 400");
        softAssert.assertEquals(CartMessage.CART_NOT_FOUND, cartResponse.getMessage());

        softAssert.assertAll();
    }
}
