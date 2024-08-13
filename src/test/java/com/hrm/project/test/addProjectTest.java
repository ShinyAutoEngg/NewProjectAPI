package com.hrm.project.test;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.Driver;

import io.restassured.http.ContentType;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
public class addProjectTest 
{
	@Test
	public void addSingleProjectWithCreated() throws SQLException
	{
		Random ran = new Random();
		int ranNum=ran.nextInt(100);
		String expectedProjName="Airtel"+ranNum;
		String expMsg="Successfully Added";
		String expStatus="Created";
		String jbody ="{\r\n"
				+ "  \"createdBy\": \"deepak\",\r\n"
				+ "  \"projectName\": \""+expectedProjName+"\",\r\n"
				+ "  \"status\": \"Created\",\r\n"
				+ "  \"teamSize\": 0\r\n"
				+ "}";
		
		
		Response resp = given()
				.contentType(ContentType.JSON).
				body(jbody).log().all()
		.when().post("http://49.249.28.218:8091/addProject");
		String actMsg=resp.jsonPath().get("msg").toString();
		String actProjStatus=resp.jsonPath().get("status").toString();
		String actProjName=resp.jsonPath().getString("projectName");
		
		resp.then().log().all().assertThat().statusCode(201).time(Matchers.lessThan(3000L));
		Assert.assertEquals(actMsg, expMsg);
		Assert.assertEquals(actProjName,expectedProjName);
		Assert.assertEquals(actProjStatus, expStatus);
		
		Driver driverRef = new Driver();
		DriverManager.registerDriver(driverRef);
		Connection conn = DriverManager.getConnection("jdbc:mysql://49.249.28.218:3333/ninza_hrm", "root@%", "root");
		
	}
}
