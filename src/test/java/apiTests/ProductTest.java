package apiTests;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import responses.product.SuccessCreateProduct;
import responses.product.SuccessDeleteProduct;
import responses.product.SuccessUpdateProduct;
import specification.RequestSpec;
import specification.ResponseSpec;
import steps.data.users.ProductInfoProvider;
import steps.data.users.UserInfoProvider;
import storage.APIV1;
import storage.USER;

import static io.restassured.RestAssured.given;

public class ProductTest {
    Gson gson = new Gson();

    @Test
    @DisplayName("Check create product")
    public void createProductTest() {
        SuccessCreateProduct successCreateProduct = RestAssured.given()
                .header(new Header("Authorization", "Bearer " + UserInfoProvider.getToken()))
                .when()
                .contentType(ContentType.JSON)
                .body(gson.toJson(ProductInfoProvider.getProduct(USER.EMAIL_INFO)))
                .post(APIV1.STAGE.getApi() + APIV1.CREATE_PRODUCT.getApi())
                .then().log().all()
                .extract().as(SuccessCreateProduct.class);

        Assertions.assertEquals("success", successCreateProduct.getResult());
        Assertions.assertEquals(200, successCreateProduct.getCode());
        Assertions.assertFalse(successCreateProduct.getData().isEmpty(), "data  is empty");
    }

    @Test
    @DisplayName("Check delete product")
    public void deleteProductTest() {
        SuccessDeleteProduct deleteProductTest = RestAssured.given()
                .header(new Header("Authorization", "Bearer " + UserInfoProvider.getToken()))
                .delete(APIV1.STAGE.getApi() + APIV1.DELETE_PRODUCT_ID.getApi() + ProductInfoProvider.getProductId())
                .then().log().all()
                .extract().as(SuccessDeleteProduct.class);

        Assertions.assertEquals("success", deleteProductTest.getResult());
        Assertions.assertEquals(200, deleteProductTest.getCode());
        Assertions.assertTrue(deleteProductTest.isData());
    }

    @Test
    @DisplayName("Check success product update")
    public void successUpdateTest() {
        RequestSpec.installSpecification(RequestSpec.requestSpec(APIV1.LOGIN.getApi()), ResponseSpec.responseOK200());
        SuccessUpdateProduct successUpdateProduct = given()
                .header(new Header("Authorization", "Bearer " + UserInfoProvider.getToken()))
                .when()
                .contentType(ContentType.JSON)
                .body(gson.toJson(ProductInfoProvider.updProduct(USER.EMAIL_INFO)))
                .put(APIV1.STAGE.getApi() + APIV1.UPDATE_PRODUCT.getApi() + ProductInfoProvider.getProductId())
                .then().log().all()
                .extract().as(SuccessUpdateProduct.class);

        Assertions.assertEquals("success", successUpdateProduct.getResult());
        Assertions.assertEquals(200, successUpdateProduct.getCode());
        Assertions.assertFalse(successUpdateProduct.getData().isEmpty(), "data  is empty");
    }
}
