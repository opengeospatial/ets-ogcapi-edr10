package org.opengis.cite.ogcapiedr10.encodings.geojson;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GeoJSONValidator {
    public static final int GeoJSON = -1;
    public static final int EDRGeoJSON = -2;
    protected final int DEFAULT_BUFFER_SIZE = 8192;
    public boolean isGeoJSONValidPerSchema(String docURL, int schemaFlag) throws Exception {
        String schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/geojson.json";

        if(schemaFlag == EDRGeoJSON){
            schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/edrgeojson.json";
        }

        boolean valid = false;

        InputStream inputStream = getClass()
                .getResourceAsStream(schemaToApply);
        JSONObject rawSchema = new JSONObject(otherConvertInputStreamToString(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);
        schema.validate(readJSONObjectFromURL(new URL(docURL))); // throws a ValidationException if this object is invalid
        valid = true;


        return valid;

    }

    public JSONObject readJSONObjectFromURL(URL requestURL) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) requestURL.openConnection();
        urlConnection.setRequestProperty("Accept","application/json");
        InputStream is = urlConnection.getInputStream();
        try ( Scanner scanner = new Scanner(is,
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");

            return new JSONObject(scanner.hasNext() ? scanner.next() : "");
        }

    }
    
    // from https://mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
    public String otherConvertInputStreamToString(InputStream is) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }



        return result.toString("UTF-8");


    }	    
}
