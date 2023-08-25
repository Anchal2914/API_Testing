package qtriptest.APITests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import java.util.UUID;



public class testCase_API_01 {
    
    @Test(groups = "API_Test")
    public void TestCase01() {
        
        // Register    
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/";
        RestAssured.basePath = "api/v1/register";
        RequestSpecification register_http = RestAssured.given().log().all();

        JSONObject registerObj = new JSONObject();
        String email = String.format("anchal%scrio.do", UUID.randomUUID());
        String password = String.format("test%s", UUID.randomUUID());
        String confirmpassword = password;
        registerObj.put("email", email);
        registerObj.put("password", password);
        registerObj.put("confirmpassword", confirmpassword);

         
        register_http.header("Content-Type","application/json");
        register_http.body(registerObj.toString());
        Response res = register_http.post();

        Assert.assertEquals(res.getStatusCode(), 201);
        Boolean register_success = res.jsonPath().getBoolean("success");
        Assert.assertTrue(register_success);

//  Login
        RestAssured.basePath = "api/v1/login";
        RequestSpecification login_http = RestAssured.given().log().all();

        JSONObject loginObj = new JSONObject();
        loginObj.put("email", email);
        loginObj.put("password", password);

        login_http.header("Content-Type","application/json");
        login_http.body(loginObj.toString());
        res = login_http.post().then().log().all().statusCode(201).extract().response();

        Assert.assertEquals(res.getStatusCode(), 201);
        Boolean login_success =res.body().jsonPath().getBoolean("success");
        Assert.assertTrue(login_success);
        String token = res.body().jsonPath().getString("data.token");
        String id = res.body().jsonPath().getString("data.id");

        Assert.assertNotNull(token);
        Assert.assertNotNull(id);
    } 
     
}
