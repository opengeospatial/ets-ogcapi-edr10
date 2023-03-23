package org.opengis.cite.ogcapiedr10.encodings.json;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.OPEN_API_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

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

		Response response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/");
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

		response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET,
				"/collections");
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
