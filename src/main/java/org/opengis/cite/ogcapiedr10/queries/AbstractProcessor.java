package org.opengis.cite.ogcapiedr10.queries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public abstract class AbstractProcessor {
    
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
	   
	   
	   int getMaximum(int noOfCollections, int collectionsListSize) {	        
	        //if noOfCollections is -1 (meaning check box 'Test all collections' was checked)
	        //use all collections. Otherwise use the specified noOfCollections
	        int maximum = noOfCollections == -1 ? collectionsListSize : noOfCollections;
	        maximum = noOfCollections > collectionsListSize ? collectionsListSize : noOfCollections;
	        return maximum;
	   }
	   
	   String getSupportedFormat(List<String> outputFormatList) {
	       String supportedFormat = "";
               for (int f = 0; f < outputFormatList.size(); f++) {
                   if (outputFormatList.get(f).equalsIgnoreCase("CoverageJSON") || outputFormatList.get(f).toLowerCase().contains("CoverageJSON".toLowerCase())) {  //preference for CoverageJSON if supported
                       supportedFormat = outputFormatList.get(f);
                   }
                   else if (outputFormatList.get(f).equalsIgnoreCase("GeoJSON")) {
                       supportedFormat = outputFormatList.get(f);
                   }
               }
               return supportedFormat;
	   }

}
