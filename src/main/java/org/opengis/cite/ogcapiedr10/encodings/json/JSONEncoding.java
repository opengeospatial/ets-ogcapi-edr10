package org.opengis.cite.ogcapiedr10.encodings.json;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URI;

import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.OPEN_API_MIME_TYPE;
import static org.testng.Assert.assertTrue;

public class JSONEncoding extends CommonFixture {
	
	

	protected URI iut;
	

	/**
	 * <pre>
	 * Abstract Test 18: Verify support for JSON
	 * Abstract Test 19: Verify the content of a JSON document given an input document and schema.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 18 (/conf/json/definition), Abstract Test 19 (/conf/json/content)")
	public void validateResponseForJSON() {

		Response response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET);
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for landing page");
		assertTrue(isJSONValidPerSchema(response.asString(), "/"),
				"Fails Abstract Test 19, landing page response not valid JSON");


		response = init().baseUri(apiDefUri.toString()).accept( OPEN_API_MIME_TYPE ).when().request( GET );
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for api description \nCODE"+response.getStatusCode()+"\n"+apiDefUri.toString());

		response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET,
				"/conformance");
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for conformance declaration");
		assertTrue(isJSONValidPerSchema(response.asString(), "/conformance"),
				"Fails Abstract Test 19, conformance declaration response not valid JSON");

		response = getCollectionResponse(null);
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for collections metadata");
		// TODO add JSON Schema Validation for /collections

	}

	private boolean isJSONValidPerSchema(String doc, String path) {

		boolean valid = false;

		if (path.equals("/")) {
			try (InputStream inputStream = getClass()
					.getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/landingPage.json")) {
			
				JSONObject rawSchema = new JSONObject(convertInputStreamToString(inputStream));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
				valid = true;
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} else if (path.equals("/conformance")) {
			try (InputStream inputStream = getClass()
					.getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/confClasses.json")) {
				JSONObject rawSchema = new JSONObject(convertInputStreamToString(inputStream));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
				valid = true;
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} else {
			valid = true;
		}

		return valid;

	}


}
