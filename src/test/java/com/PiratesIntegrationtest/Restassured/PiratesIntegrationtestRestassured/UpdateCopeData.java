package com.PiratesIntegrationtest.Restassured.PiratesIntegrationtestRestassured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.IOException;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class UpdateCopeData {

//public class Oauth2value {
	String grantType = "Client Credentials";
	String accessTokenURL = "https://test-lmidp.libertymutual.com/as/token.oauth2";
	String clientID = "ci_niplcmsclient_4";
	String clientSecret = "955be1ac-d3af-4c86-b8cb-e4f69bbafc5d";
	String scope = "CI_ATWORK_COM";
	String clientAuthentication = "Send as Basic Auth header";
	String accessToken = "";
	String tokenType = "";
	String expiresin = "";

	public void getaccesstokenvalues() throws IOException, JSONException {

		RestAssured.baseURI = "https://test-lmidp.libertymutual.com/as/token.oauth2";
		RequestSpecification httpRequest = RestAssured.given().formParam("grant_type", "client_credentials")
				.formParam("client_id", "ci_niplcmsclient_4")
				.formParam("client_secret", "955be1ac-d3af-4c86-b8cb-e4f69bbafc5d").formParam("scope", "CI_ATWORK_COM")
				.formParam("client authentication", "send as basic auth header");
		System.out.println(httpRequest.log().toString());
		Response response = httpRequest.post();
		ResponseBody body = response.getBody();
		System.out.println("OAuth Response Body is:" + body.asString());

		// To convert response body to json object
		JSONObject jsonobj = new JSONObject(body.asString());
		accessToken = jsonobj.get("access_token").toString();
		tokenType = jsonobj.get("token_type").toString();
		expiresin = jsonobj.get("expires_in").toString();
		System.out.println(accessToken + tokenType + expiresin);		
	}

	@Test
	public void test_UpdateCopeDataService() throws IOException, JSONException {
		getaccesstokenvalues();
		Response response = given().auth().oauth2(accessToken).contentType("application/json")
				.body("{\"agreementId\":0,\r\n" + "\"subLocId\":14036975,\r\n" + "\"copyPolicyIndicator\":\"N\",\r\n"
						+ "\"userId\":2280}  \r\n" + "")
				.when()
				.post("https://nip-pw-location-code-service-stage.us-east-1.np.paas.lmig.com/services/niplcs/cope/update-cope-data");
		System.out.println("POST Response\n" + response.asString());

		Assert.assertEquals(response.path("subLocId"), 14036975);
		Assert.assertEquals(response.path("agreementId"), 0);
		Assert.assertEquals(response.path("copyPolicyIndicator"), "N");
		Assert.assertEquals(response.path("userId"), 2280);

	}

}
