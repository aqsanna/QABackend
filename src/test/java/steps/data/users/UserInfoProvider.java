package steps.data.users;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import requests.AuthInfo;
import responses.userLogin.SuccessLogin;
import storage.ApiV1;
import storage.User;

import static io.restassured.RestAssured.given;
import static storage.User.PUSH_TOKEN;

public class UserInfoProvider {
    public static AuthInfo getUser(User email) {
        return switch (email) {
            case EMAIL_INFO -> new AuthInfo(
                    new AuthInfo.Params(
                            new AuthInfo.Params.App()
                                    .withBundleId(User.BUNDLE_ID.getUserData())
                                    .withVersion(User.APP_VERSION.getUserData())
                            , User.EMAIL_INFO.getUserData()
                            , User.PASSWORD.getUserData(),
                            new AuthInfo.Params.Device()
                                    .withVersion(User.DEVICE_VERSION.getUserData())
                                    .withOs(User.OS.getUserData())
                                    .withPushToken(PUSH_TOKEN.getUserData()),
                            User.APPLICATION_KEY.getUserData()));

            default -> null;
        };
    }

    public static AuthInfo getUserClient(User email) {
        return switch (email) {
            case EMAIL_CLIENT -> new AuthInfo(
                    new AuthInfo.Params(
                            new AuthInfo.Params.App()
                                    .withBundleId(User.BUNDLE_ID.getUserData())
                                    .withVersion(User.APP_VERSION.getUserData())
                            , User.EMAIL_CLIENT.getUserData()
                            , User.PASSWORD_CLIENT.getUserData(),
                            new AuthInfo.Params.Device()
                                    .withVersion(User.DEVICE_VERSION.getUserData())
                                    .withOs(User.OS.getUserData())
                                    .withPushToken(PUSH_TOKEN.getUserData()),
                            User.APPLICATION_CLIENT.getUserData()));

            default -> null;
        };
    }

    public static String getToken() {
        Gson gson = new Gson();
        AuthInfo authInfo = UserInfoProvider.getUser(User.EMAIL_INFO);

        SuccessLogin login = given()
                .when()
                .contentType(ContentType.JSON)
                .body(gson.toJson(authInfo))
                .post(ApiV1.STAGE.getApi() + ApiV1.REGISTER.getApi())
                .then().log().all()
                .extract().as(SuccessLogin.class);
        return login.getData().getToken();
    }

    public static boolean isNumber(String id) {
        return id.matches("[0-9]+") && id.length() > 0;
    }
}
