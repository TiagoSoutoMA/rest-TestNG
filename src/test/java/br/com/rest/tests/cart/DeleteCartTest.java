package br.com.rest.tests.cart;

import br.com.rest.client.*;
import br.com.rest.data.factory.*;
import br.com.rest.model.request.*;
import br.com.rest.model.response.CartResponse;
import br.com.rest.utils.messages.CartMessage;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class DeleteCartTest {

    private final CartClient cartClient = new CartClient();
    private final ProdutctsClient produtctsClient = new ProdutctsClient();
    private final LoginClient loginClient = new LoginClient();
    private final UsersClient usersClient = new UsersClient();
    private CartResponse cartResponse;
    private String idProduct, idUser, userIdNoCart, token, tokenUserIdNoCart;
    private final SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void setUp() {

        UsersRequest usersRequest1 = UsersDataFactory.createUserAdmin();

        userIdNoCart = usersClient.usersPost(usersRequest1)
                .then()
                    .extract()
                        .path("_id")
        ;

        String email = usersRequest1.getEmail();
        String password = usersRequest1.getPassword();

        LoginRequest loginRequest = LoginDataFactory.login(email, password);

        tokenUserIdNoCart = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .path("authorization")
        ;

        UsersRequest usersRequest = UsersDataFactory.createUserAdmin();

        idUser = usersClient.usersPost(usersRequest)
                .then()
                    .extract()
                        .path("_id")
        ;

        email = usersRequest.getEmail();
        password = usersRequest.getPassword();

        loginRequest = LoginDataFactory.login(email, password);

        token = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .path("authorization")
        ;

        ProductsRequest productsRequest = ProductsDataFactory.createProduct();

        idProduct = produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .path("_id")
        ;

        Integer quantityProduct = produtctsClient.productsIdGet(idProduct)
                .then()
                .extract()
                .path("quantidade");

        CartRequest cartRequest = CartDataFactory.createCart(idProduct, quantityProduct);

        cartClient.cartPost(token, cartRequest);
    }

    @AfterMethod
    public void tearDown() {

        produtctsClient.productsDelete(token, idProduct);
        usersClient.usersDelete(userIdNoCart);
        usersClient.usersDelete(idUser);
    }

    @Test
    public void testCompletePurchaseSucessfully() {

        Response response = cartClient.cartDeleteConclude(token)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());


        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.EXCLUDED_SUCESS, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCompletePurchaseNoCart() {

        Response response = cartClient.cartDeleteConclude(tokenUserIdNoCart)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.USER_CART_NOT_FOUND, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCompletePurchaseNoToken() {

        Response response = cartClient.cartDeleteConclude(CartDataFactory.empty())
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_UNAUTHORIZED, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.TOKEN_NOT_FOUND_OR_INVALID, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCancelPurchaseSucessfully() {

        Response response = cartClient.cartDeleteCancel(token)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.CART_EXCLUDED, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCancelPurchaseNoCart() {

        Response response = cartClient.cartDeleteCancel(tokenUserIdNoCart)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.USER_CART_NOT_FOUND, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCancelPurchaseNoToken() {

        Response response = cartClient.cartDeleteCancel(CartDataFactory.empty())
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_UNAUTHORIZED, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.TOKEN_NOT_FOUND_OR_INVALID, cartResponse.getMessage());

        softAssert.assertAll();
    }
}
