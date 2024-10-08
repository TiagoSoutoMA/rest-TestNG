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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetProductsTest {

    private static final ProdutctsClient produtctsClient = new ProdutctsClient();
    private static final LoginClient loginClient = new LoginClient();
    private ProductsResponse productsResponse;
    private static String idProduct, token;

    @BeforeClass
    public void setUp() {

        LoginRequest loginRequest = LoginDataFactory.loginAdmin();

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
    }

    @AfterClass
    public void tearDown() { produtctsClient.productsDelete(token, idProduct); }

    @Test
    public void testListProductsSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsGet()
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(productsResponse.getStatusCode(), HttpStatus.SC_OK, "Status code deve ser 200");
        softAssert.assertFalse(productsResponse.getProdutos().isEmpty(), "A lista de produtos não deve ser vazia");
        softAssert.assertEquals(productsResponse.getProdutos().size(), Integer.parseInt(productsResponse.getQuantidade()), "A quantidade deve ser igual ao tamanho da lista");
        softAssert.assertTrue(
            productsResponse.getProdutos().stream().allMatch(product ->
                    product.getNome() != null && !product.getNome().isEmpty() &&
                    product.getPreco() != null &&
                    product.getDescricao() != null && !product.getDescricao().isEmpty() &&
                    product.getQuantidade() != null &&
                    product.get_id() != null && !product.get_id().isEmpty()
            ), "Todos os produtos devem ter nome, preco, descrição e quantidade preenchidos e um id válido"
        );

        softAssert.assertAll();
    }

    @Test
    public void testListProductsByIdSucessfully() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsIdGet(idProduct)
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(productsResponse.getStatusCode(), HttpStatus.SC_OK, "Status code deve ser 200");
        softAssert.assertTrue(productsResponse.getNome() != null && !productsResponse.getNome().isEmpty());
        softAssert.assertNotNull(productsResponse.getPreco());
        softAssert.assertTrue(productsResponse.getDescricao() != null && !productsResponse.getDescricao().isEmpty());
        softAssert.assertNotNull(productsResponse.getQuantidade());
        softAssert.assertTrue(productsResponse.get_id() != null && !productsResponse.get_id().isEmpty());

        softAssert.assertAll();
    }

    @Test
    public void testListProductsEmptyId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsIdGet(ProductsDataFactory.empty())
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(productsResponse.getStatusCode(), HttpStatus.SC_OK, "Status code deve ser 200");
        softAssert.assertFalse(productsResponse.getProdutos().isEmpty(), "A lista de produtos não deve ser vazia");
        softAssert.assertEquals(productsResponse.getProdutos().size(), Integer.parseInt(productsResponse.getQuantidade()), "A quantidade deve ser igual ao tamanho da lista");
        softAssert.assertTrue(
                productsResponse.getProdutos().stream().allMatch(product ->
                        product.getNome() != null && !product.getNome().isEmpty() &&
                                product.getPreco() != null &&
                                product.getDescricao() != null && !product.getDescricao().isEmpty() &&
                                product.getQuantidade() != null &&
                                product.get_id() != null && !product.get_id().isEmpty()
                ), "Todos os produtos devem ter nome, preco, descrição e quantidade preenchidos e um id válido"
        );

        softAssert.assertAll();
    }

    @Test
    public void testListProductsInvalidId() {

        SoftAssert softAssert = new SoftAssert();

        Response response = produtctsClient.productsIdGet(ProductsDataFactory.name())
                .then()
                    .extract()
                        .response()
        ;

        productsResponse = response.getBody().as(ProductsResponse.class);
        productsResponse.setStatusCode(response.getStatusCode());

        softAssert.assertEquals(HttpStatus.SC_BAD_REQUEST, productsResponse.getStatusCode(), "Status code deve ser 400");
        softAssert.assertEquals(ProductsMessage.PRODUCT_NOT_FOUND, productsResponse.getMessage());

        softAssert.assertAll();
    }

    @Test
    public void testGetProductsContract() {

        produtctsClient.productsGet()
                .then()
                    .body(matchesJsonSchemaInClasspath("schemas/getProductsSchema.json"))
        ;
    }
}
