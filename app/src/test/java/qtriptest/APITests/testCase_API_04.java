package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class testCase_API_04 {

    
    @Test(groups = "API_Test")
    public void TestCase04() {
        
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

//  Re_register
        RestAssured.basePath = "api/v1/register";
        RequestSpecification reRegister_http = RestAssured.given().log().all();

        JSONObject reRegisterObj = new JSONObject();
        reRegisterObj.put("email", email);
        reRegisterObj.put("password", password);
        reRegisterObj.put("password", confirmpassword);

        reRegister_http.header("Content-Type","application/json");
        reRegister_http.body(reRegisterObj.toString());
        res = reRegister_http.post().then().log().all().statusCode(400).extract().response();

        Assert.assertEquals(res.getStatusCode(), 400);
        Assert.assertFalse(res.jsonPath().getBoolean("success"));
        Assert.assertEquals(res.jsonPath().getString("message"), "Email already exists");

        
    } 
}

  

