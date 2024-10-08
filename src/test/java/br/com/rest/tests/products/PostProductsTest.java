package br.com.rest.tests.products;

import br.com.rest.client.LoginClient;
import br.com.rest.client.ProdutctsClient;
import br.com.rest.data.factory.LoginDataFactory;
import br.com.rest.data.factory.ProductsDataFactory;
import br.com.rest.model.request.LoginRequest;
import br.com.rest.model.request.ProductsRequest;
import br.com.rest.model.response.ProductsResponse;
import br.com.rest.utils.messages.ProductsMessage;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class PostProductsTest {

    private static final ProdutctsClient produtctsClient = new ProdutctsClient();
    private static final LoginClient loginClient = new LoginClient();
    private ProductsRequest productsRequest;
    private ProductsResponse productsResponse;
    private static String idProduto, token;

    @BeforeClass
    public void setUp() {

        LoginRequest loginRequest = LoginDataFactory.loginAdmin();

        token = loginClient.loginPost(loginRequest)
                .then()
                    .extract()
                        .path("authorization")
        ;
    }

    @AfterClass
    public void tearDown() { produtctsClient.productsDelete(token, idProduto); }

    @Test
    public void testRegisterProductSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProduct();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());
        idProduto = productsResponse.get_id();

        softAssert.assertEquals(HttpStatus.SC_CREATED, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.CREATED_SUCESS, productsResponse.getMessage());
        softAssert.assertNotNull(productsResponse.get_id());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductInvalidData() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidData();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.EMPTY_NAME, productsResponse.getNome());
        softAssert.assertEquals(ProductsMessage.INVALID_PRICE, productsResponse.getPreco());
        softAssert.assertEquals(ProductsMessage.EMPTY_DESCRIPTION, productsResponse.getDescricao());
        softAssert.assertEquals(ProductsMessage.INVALID_QUANTITY, productsResponse.getQuantidade());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductEmptyName() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithEmptyName();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.EMPTY_NAME, productsResponse.getNome());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductInvalidPrice() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidPrice();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.INVALID_PRICE, productsResponse.getPreco());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductEmptyDescription() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithEmptyDescription();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.EMPTY_DESCRIPTION, productsResponse.getDescricao());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductInvalidQuantity() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidQuantity();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.INVALID_QUANTITY, productsResponse.getQuantidade());

        softAssert.assertAll();
    }

    @Test
    public void testRegisterProductDuplicatedName() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithDuplicatedName();

        Response response =produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.DUPLICATED_NAME, productsResponse.getMessage());

        softAssert.assertAll();
    }
}
