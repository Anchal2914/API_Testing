package qtriptest.APITests;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import java.io.File;
import java.util.List;

public class testCase_API_02 {
        
        @Test(groups = "API_Test")
        public void TestCase02() {
            
            
            RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/";
            RestAssured.basePath = "api/v1/cities";
        //Response res = RestAssured.given().log().all().when().get().then().log().all().extract().response();
        Response res = RestAssured.given().log().all().queryParam("q","beng").when().get().then().log().all().extract().response();

        JSONArray arr = new JSONArray(res.body().asString());
        JsonPath jp = new JsonPath(res.body().asString());
        System.out.println(jp.getString("[0].description"));

        List<String> list = jp.getList("id");
        int index = 0;
        for(int i=0; i<list.size(); i++){
            if(list.get(i).equals("bengaluru")){
                index = 1;
                break;
            }
            System.out.println(jp.getString("[1].image"));

            list = jp.getList("image");

            System.out.println(list.get(index));
            System.out.println(arr.length());
            System.out.println(list.size());

            Assert.assertEquals(res.getStatusCode(), 200);
            Assert.assertEquals(list.size(), 1);
            Assert.assertEquals(jp.getString("[0].description"),"100+ Places");

            File SchemaFile = new File("/home/crio-user/workspace/anchalsingh2914-ME_API_TESTING_PROJECT/app/src/test/java/qtriptest/APITests/testCase_API_02.java");
            JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchema(SchemaFile);
            res.then().assertThat().body(validator);


        }

    } 



}
