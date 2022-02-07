package org.opengis.cite.ogcapiedr10.encodings.geojson;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GeoJSONValidator {
    public static final int GeoJSON = -1;
    public static final int EDRGeoJSON = -2;
    public boolean isGeoJSONValidPerSchema(String docURL, int schemaFlag) throws Exception {
        String schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/geojson.json";

        if(schemaFlag == EDRGeoJSON){
            schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/edrgeojson.json";
        }

        boolean valid = false;

        InputStream inputStream = getClass()
                .getResourceAsStream(schemaToApply);
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);
        schema.validate(readJSONObjectFromURL(new URL(docURL))); // throws a ValidationException if this object is invalid
        valid = true;


        return valid;

    }

    public JSONObject readJSONObjectFromURL(URL requestURL) throws IOException {
        try ( Scanner scanner = new Scanner(requestURL.openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");

            return new JSONObject(scanner.hasNext() ? scanner.next() : "");
        }
    }
}
