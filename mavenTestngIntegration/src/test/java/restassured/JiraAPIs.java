package restassured;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class JiraAPIs {
	
	
	
	public String getSeesionID() {
		
		String sessionID = null;
		
		Response resp= RestAssured.given()
				.body("{ \"username\": \"admin\", \"password\": \"admin\" }")
				.headers("content-type","application/json")
				.when()
				.post("http://localhost:8080/rest/auth/1/session");
		JsonPath jpath = new JsonPath(resp.body().asPrettyString());
		sessionID = jpath.get("session.value");
		return sessionID;
	}
	
	@Test
	public void getIssue() {
		
		Response resp = 
				//then
				RestAssured.given()
				.headers("Cookie","JSESSIONID="+getSeesionID())
				//when
				.when()
				.get("http://localhost:8080/rest/api/2/issue/AP-3");
		JsonPath jpath = new JsonPath(resp.body().asPrettyString());
		String summary = jpath.getString("fields.summary");
		String emailAddr  = jpath.getString("fields.creator.emailAddress");
		//given
		Assert.assertEquals(summary, "Testing thru postman - RestAssued F/W..");
		Assert.assertEquals(emailAddr, "yogeshaanerao90@gmail.com");
		Assert.assertEquals(resp.statusCode(), 200);
	}
	
	@Test
	public void addComment() {
		Response resp = 
				//then
				RestAssured.given()
				.body("{\r\n"
						+ "    \"body\": \"This comment is added from eclipse\",\r\n"
						+ "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n"
						+ "    }\r\n"
						+ "}")
				.headers("Cookie","JSESSIONID="+getSeesionID())
				.headers("content-type","application/json")
				//when
				.when()
				.post("http://localhost:8080/rest/api/2/issue/AP-3/comment");
		JsonPath jpath = new JsonPath(resp.body().asPrettyString());
		String body = jpath.getString("body");
		//given
		Assert.assertEquals(body, "This comment is added from eclipse");
		Assert.assertEquals(resp.statusCode(), 201);
	}
	
	@Test
	public void getComment() {
		Response resp = 
				//then
				RestAssured.given()
				.headers("Cookie","JSESSIONID="+getSeesionID())
				.headers("content-type","application/json")
				//when
				.when()
				.get("http://localhost:8080/rest/api/2/issue/AP-3/comment");
		JsonPath jpath = new JsonPath(resp.body().asPrettyString());
		String body = jpath.getString("comments[0].body");
		//given
		Assert.assertEquals(body, "This comment is added from postman");
		Assert.assertEquals(resp.statusCode(), 200);
	}
	
	@Test
	public void putComment() {
		Response resp = 
				//then
				RestAssured.given()
				.headers("Cookie","JSESSIONID="+getSeesionID())
				.headers("content-type","application/json")
				.body("{\r\n"
						+ "    \"body\": \"This comment is added from eclipse - updated\",\r\n"
						+ "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n"
						+ "    }\r\n"
						+ "}")
				//when
				.when()
				.put("http://localhost:8080/rest/api/2/issue/AP-3/comment/10105");
		JsonPath jpath = new JsonPath(resp.body().asPrettyString());
		String body = jpath.getString("body");
		//given
		Assert.assertEquals(body, "This comment is added from eclipse - updated");
		Assert.assertEquals(resp.statusCode(), 200);
	}
	
	@Test
	public void deleteComment() {
		Response resp = 
				//then
				RestAssured.given()
				.headers("Cookie","JSESSIONID="+getSeesionID())
				.headers("content-type","application/json")
				
				//when
				.when()
				.delete("http://localhost:8080/rest/api/2/issue/AP-3/comment/10107");
			Assert.assertEquals(resp.statusCode(), 204);
	}

}
