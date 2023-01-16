package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class B_StartServerFromCode {
    private static final String HOST = "localhost";
    private static final int PORT =  8080;
    private static WireMockServer server= new WireMockServer(PORT);

    @BeforeClass
    public void initializeServer(){
        System.out.println("Init");
        server.start();
        WireMock.configureFor(HOST,PORT);
        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(201);
        mockResponse.withStatusMessage("Hello guys");
        mockResponse.withHeader("Content-Type", "text/json");
        mockResponse.withHeader("token","11111");
        mockResponse.withHeader("set-cookie", "session_id=91837492837");
        mockResponse.withHeader("set-cookie", "split_test_group=B");
        mockResponse.withBody("text put in the body");
        WireMock.stubFor(WireMock.get("/emps/1").willReturn(mockResponse));
        WireMock.givenThat(WireMock.get("/emps/1").willReturn(mockResponse));
    }


    @Test
    public void testCode(){
        String testApi = "http://localhost: " + PORT + "/emps/1";
        System.out.println("Service to be hit "+ testApi);
        Response response = RestAssured.given()
                .get("http://localhost:8080/emps/1").then()
                .statusCode(201).extract().response();
        System.out.println();
        Assert.assertEquals(response.getHeader("token"),"11111");
        Assert.assertEquals(response.getStatusLine(),"HTTP/1.1 201 Hello guys");
        Assert.assertEquals(response.getCookie("session_id"), "91837492837");
        Assert.assertEquals(response.getCookie("split_test_group"), "B");
        Assert.assertEquals(response.getBody().asString(), "text put in the body");
    }
}
