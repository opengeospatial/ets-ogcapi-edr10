package org.opengis.cite.ogcapiedr10.encodings.edrgeojson;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.EtsAssert;
import org.opengis.cite.ogcapiedr10.encodings.geojson.GeoJSONValidator;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;

public class EDRGeoJSONEncoding extends CommonFixture {


    private String schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/edrgeojson.json";
    protected URI iut;



    /**
     * <pre>
     * Abstract Test 22: Verify support for the EDR GeoJSON Schema
     * Abstract Test 23: Verify the content of an EDR GeoJSON document given an input document and schema.
     * Note that the first positions resource that supports GeoJSON is tested.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 22 (/conf/edr-geojson/definition), Abstract Test 23 (/conf/edr-geojson/content)")
    public void validateResponseForEDRGeoJSON() {

        StringBuffer sb = new StringBuffer();
        boolean atLeastOneCollectionTested = false; //we test the first locations resource we find
        Response response = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET ,"/collections");
        JsonPath jsonResponse = response.jsonPath();
        ArrayList collectionsList = (ArrayList) jsonResponse.getList("collections");

        for(int i=0; (i< collectionsList.size()) && (atLeastOneCollectionTested==false); i++)
        {
            HashMap collectionItem = (HashMap) collectionsList.get(i);

            HashMap dataQueries = (HashMap) collectionItem.get("data_queries");
            boolean supportsLocationsQuery = dataQueries.containsKey("locations");

            if(supportsLocationsQuery) {
                HashMap locationsQuery = (HashMap) dataQueries.get("locations");
                HashMap link = (HashMap) locationsQuery.get("link");
                HashMap variables = (HashMap) link.get("variables");
                ArrayList<String> outputFormatList = (ArrayList<String>) variables.get("output_formats");
                String supportedFormat = null;
                for (int f = 0; f < outputFormatList.size(); f++) {
                    if (outputFormatList.get(f).equals("GeoJSON")) {
                        supportedFormat = outputFormatList.get(f);
                    }
                }

                try {
                    if(supportedFormat!=null) {
                        String locationsURL = link.get("href").toString()+"?f="+supportedFormat;
                        System.out.println("CHK URL "+locationsURL);
                        GeoJSONValidator validator = new GeoJSONValidator();
                        boolean result = validator.isGeoJSONValidPerSchema(locationsURL,GeoJSONValidator.EDRGeoJSON);
                        atLeastOneCollectionTested = true;
                        if(result==false) {
                            sb.append(" None of the collections with locations resources were found to offer GeoJSON encoded responses.\n");
                        }
                    }
                    else {
                        sb.append(" None of the collections with locations resources were found to offer GeoJSON encoded responses.\n");
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    sb.append(" "+ex.getMessage()+"\n");
                }


            }
            else {
                sb.append(" None of the collections were found to offer locations resources.\n");
            }

        }


        String resultMessage = sb.toString();
        EtsAssert.assertTrue(resultMessage.length()==0,
                "Fails Abstract Test 23. "
                        + resultMessage);



    }


}