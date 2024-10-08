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

public class DeleteProductsTest {

    private static final ProdutctsClient produtctsClient = new ProdutctsClient();
    private static final LoginClient loginClient = new LoginClient();
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

        ProductsRequest produtoRequest = ProductsDataFactory.createProduct();

        idProduto = produtctsClient.productsPost(token, produtoRequest)
                .then()
                    .extract()
                        .path("_id")
        ;
    }

    @Test
    public void testDeleteProductSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsDelete(token, idProduto)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.EXCLUDED_SUCESS, productsResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testDeleteProductIsPartCart() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsDelete(token, ProductsDataFactory.idProductCart())
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.PRODUCT_PART_CART, productsResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testDeleteProductInvalidId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsDelete(token, ProductsDataFactory.name())
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_OK, productsResponse.getStatusCode());
        softAssert.assertEquals(ProductsMessage.PRODUCT_NOT_EXCLUDED, productsResponse.getMessage());

        softAssert.assertAll();
    }
}
