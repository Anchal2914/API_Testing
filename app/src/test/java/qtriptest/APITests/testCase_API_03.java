package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import java.util.UUID;

public class testCase_API_03 {
 
    @Test(groups = "API_Test")
    public void TestCase03() {
        
        //  Register    
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
       
        String token = res.body().jsonPath().getString("data.token");
        String id = res.body().jsonPath().getString("data.id");
        //String header_token = "Bearer " + token ;
        //System.out.println(header_token + id);
 

//  Reservation 
        RestAssured.basePath = "api/v1/reservations/new";   
        RequestSpecification reservation_http = RestAssured.given().log().all();

        JSONObject resevationObj = new JSONObject();
        String userId = id;
        String name = "AnchalAPItest";
        String date = "2024-09-09";
        String person = "2";
        String adventure = "2447910730";
        resevationObj.put("userId", userId);
        resevationObj.put("name", name);
        resevationObj.put("date", date);
        resevationObj.put("person", person);
        resevationObj.put("adventure", adventure);

        
        res = reservation_http.header("Authorization", "Bearer " + token).
        header("Content-Type","application/json").body(resevationObj.toString()).
        when().post().then().log().all().extract().response();
        System.out.println(res);
        JsonPath jp = new JsonPath(res.body().asString());

        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertEquals(jp.getBoolean("success"), true);

//  Check Reservation
        RestAssured.basePath = "api/v1/reservations";  
        RequestSpecification reservationCheck_http = RestAssured.given().log().all(); 
        
        res = reservationCheck_http.header("Authorization", "Bearer " + token).queryParam("id", userId).when().get();
        jp = new JsonPath(res.body().asString());
        

        // List<String> list_date = jp.getList("date");
        // System.out.println(list_date);
        // int index = 0;
        // for(int i=0; i<list_date.size(); i++){
        //     if(list_date.get(i).toString().equals(date)){
        //         index = i;
        //         break;
        //     }
        // }

        List<String> list_id = jp.getList("userId");
        System.out.println(list_id);
        int index = 0;
        for(int i=0; i<list_id.size(); i++){
            if(list_id.get(i).toString().equals(userId)){
                index = i;
                break;
            }
        }
        
        Assert.assertEquals(res.getStatusCode(), 200);
        String path = "[" + index + "]." + "isCancelled";
        Assert.assertFalse(res.jsonPath().getBoolean(path));
        String uid = "[" + index + "]." + "userId";
        Assert.assertEquals(res.jsonPath().getString(uid),userId);
    } 
}
