package apiTests;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import responses.userLogin.SuccessLogin;
import spec.Specifications;
import steps.data.users.UserInfoProvider;
import storage.APIV2;
import storage.USER;

import static io.restassured.RestAssured.given;

public class AuthTest {
    Gson gson = new Gson();

    @Test
    @DisplayName("Check success user login")
    public void successLoginTest() {
        Specifications.installSpecification(Specifications.requestSpec(APIV2.LOGIN.getApi()), Specifications.responseOK200());

        SuccessLogin successLogin = given()
                .when()
                .contentType(ContentType.JSON)
                .body(gson.toJson(UserInfoProvider.getUser(USER.EMAIL_INFO)))
                .post(APIV2.STAGE.getApi() + APIV2.REGISTER.getApi())
                .then().log().all()
                .extract().as(SuccessLogin.class);

        Assertions.assertEquals("success", successLogin.getResult());
        Assertions.assertEquals("info@local.express", successLogin.getData().getUserEmail());
        Assertions.assertEquals("13546", successLogin.getData().getUserId());
        Assertions.assertTrue(successLogin.getError().isEmpty());
        Assertions.assertNotNull(successLogin.getData().getToken());
    }
}
