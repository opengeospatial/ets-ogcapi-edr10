package org.opengis.cite.ogcapiedr10.queries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import io.restassured.response.Response;

public abstract class AbstractProcessor {
	
//    protected Response response;
    
//    protected AbstractProcessor() {
//    }
//    
//    protected AbstractProcessor(Response response) {
//        this.response = response;
//    }
    
	   public String readStringFromURL(String urlString,int limit) throws Exception
	    {
	        URL requestURL = new URL(urlString);

	        BufferedReader in = new BufferedReader(new InputStreamReader(requestURL.openConnection().getInputStream()));

	        StringBuilder response = new StringBuilder();
	        String inputLine;

	        int i = 0;


	        while (((inputLine = in.readLine()) != null) && (i < limit))
	        {
	            response.append(inputLine+"\n");
	            i++;
	        }


	        in.close();

	        return response.toString();
	    }

}
