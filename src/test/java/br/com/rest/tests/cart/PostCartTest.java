package br.com.rest.tests.cart;

import br.com.rest.client.*;
import br.com.rest.data.factory.*;
import br.com.rest.model.request.*;
import br.com.rest.model.response.*;
import br.com.rest.utils.messages.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class PostCartTest {

    private static final CartClient cartClient = new CartClient();
    private static final ProdutctsClient produtctsClient = new ProdutctsClient();
    private static final LoginClient loginClient = new LoginClient();
    private static final UsersClient usersClient = new UsersClient();
    private final SoftAssert softAssert = new SoftAssert();
    private CartRequest cartRequest;
    private CartResponse cartResponse;
    private static String idProduct, idUser, token;
    private static Integer quantityProduct;

    @BeforeClass
    public void setUp() {

        UsersRequest usersRequest = UsersDataFactory.createUserAdmin();

        idUser = usersClient.usersPost(usersRequest)
                .then()
                    .extract()
                        .path("_id")
        ;

        String email = usersRequest.getEmail();
        String password = usersRequest.getPassword();

        LoginRequest loginRequest = LoginDataFactory.login(email, password);

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

        quantityProduct = produtctsClient.productsIdGet(idProduct)
                .then()
                    .extract()
                        .path("quantidade")
        ;
    }

    @BeforeMethod
    public void clearCart() { cartClient.cartDeleteCancel(token); }

    @AfterClass
    public void tearDown() {

        produtctsClient.productsDelete(token, idProduct);
        usersClient.usersDelete(idUser);
        cartClient.cartDeleteCancel(token);
    }

    @Test
    public void testCreateCartSucessfully() {

        cartRequest = CartDataFactory.createCart(idProduct, quantityProduct);

        Response response = cartClient.cartPost(token, cartRequest)
                .then()
                    .extract()
                       .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_CREATED, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.CREATED_SUCESS, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCreateCartIncorrectId() {

        cartRequest = CartDataFactory.createCartIncorrectId(CartDataFactory.name(), quantityProduct);

        Response response = cartClient.cartPost(token, cartRequest)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());


        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, cartResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.PRODUCT_NOT_FOUND, cartResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testCreateCartIncorrectQuantity() {

        cartRequest = CartDataFactory.createCartIncorrectQuantity(idProduct, quantityProduct);

        Response response = cartClient.cartPost(token, cartRequest)
                .then()
                    .extract()
                        .response()
        ;

        cartResponse = response.getBody().as(CartResponse.class);
        cartResponse.setStatusCode(response.getStatusCode());


        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, cartResponse.getStatusCode());
        softAssert.assertEquals(CartMessage.INSUFFICIENT_QUANTITY, cartResponse.getMessage());

        softAssert.assertAll();
    }
}
