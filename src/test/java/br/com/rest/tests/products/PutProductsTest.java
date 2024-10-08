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

public class PutProductsTest {

    private static final ProdutctsClient produtctsClient = new ProdutctsClient();
    private static final LoginClient loginClient = new LoginClient();
    private static ProductsRequest productsRequest;
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

        productsRequest = ProductsDataFactory.createProduct();

        idProduto = produtctsClient.productsPost(token, productsRequest)
                .then()
                    .extract()
                        .path("_id")
        ;
    }

    @AfterClass
    public void tearDown() { produtctsClient.productsDelete(token, idProduto); }

    @Test
    public void testUpdateProductSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProduct();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.UPDATE_SUCESS, productsResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testUpdateProductIncorrectId() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProduct();

        Response response = produtctsClient.productsPut(token, ProductsDataFactory.name(), productsRequest)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_CREATED, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.CREATED_SUCESS, productsResponse.getMessage());
        softAssert.assertNotNull(productsResponse.get_id());

        softAssert.assertAll();
    }

    @Test
    public void testUpdateProductInvalidData() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidData();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
    public void testUpdateProductEmptyName() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithEmptyName();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
    public void testUpdateProductInvalidPrice() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidPrice();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
    public void testUpdateProductEmptyDescription() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithEmptyDescription();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
    public void testUpdateProductInvalidQuantity() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithInvalidQuantity();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
    public void testUpdateProductDuplicatedName() {

        SoftAssert softAssert = new SoftAssert();

        productsRequest = ProductsDataFactory.createProductWithDuplicatedName();

        Response response = produtctsClient.productsPut(token, idProduto, productsRequest)
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
