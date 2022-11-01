package apiTests;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import responses.userLogin.SuccessLogin;
import specification.RequestSpec;
import specification.ResponseSpec;
import steps.data.users.UserInfoProvider;
import storage.APIV1;
import storage.USER;

import static io.restassured.RestAssured.given;

public class AuthTest {
    Gson gson = new Gson();

    @Test
    @DisplayName("Check success user login")
    public void successLoginTest() {
        RequestSpec.installSpecification(RequestSpec.requestSpec(APIV1.LOGIN.getApi()), ResponseSpec.responseOK200());

        SuccessLogin successLogin = given()
                .when()
                .contentType(ContentType.JSON)
                .body(gson.toJson(UserInfoProvider.getUser(USER.EMAIL_INFO)))
                .post(APIV1.STAGE.getApi() + APIV1.REGISTER.getApi())
                .then().log().all()
                .extract().as(SuccessLogin.class);

        Assertions.assertEquals("success", successLogin.getResult());
        Assertions.assertEquals("info@local.express", successLogin.getData().getUserEmail());
        Assertions.assertEquals("13546", successLogin.getData().getUserId());
        Assertions.assertTrue(successLogin.getError().isEmpty());
        Assertions.assertNotNull(successLogin.getData().getToken());
    }
}
