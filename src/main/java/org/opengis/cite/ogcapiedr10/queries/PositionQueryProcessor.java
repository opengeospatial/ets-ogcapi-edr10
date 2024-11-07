package org.opengis.cite.ogcapiedr10.queries;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.util.JsonUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PositionQueryProcessor extends AbstractProcessor{

    public String validatePositionQueryUsingParameters(Set<String> collectionIds, String rootUri, int noOfCollections, RequestSpecification ini){
        StringBuffer sb = new StringBuffer();
        ArrayList<String> collectionsList = new ArrayList<String>();
        collectionsList.addAll(collectionIds);
        
        int numberOfCollectionsWithPositionSupport = 0;

        //fix setting of maximum, see https://github.com/opengeospatial/ets-ogcapi-edr10/issues/133
        int maximum = getMaximum(noOfCollections, collectionsList.size());
                
        for (int c = 0; c < maximum; c++) {

            String collectionId = collectionsList.get(c);
            
            boolean supportsPositionQuery = false;            

            String url = JsonUtils.getCollectionURL(rootUri, collectionId);

            Response response = JsonUtils.getCollectionResponse(rootUri, collectionId, ini);
            JsonPath jsonResponse = JsonPath.from(new java.io.File("D:\\52n\\Projekte\\CITE Consulting\\edr-issue-133-collection-response.json"));
            
            HashMap<?,?> dataQueries = jsonResponse.getJsonObject("data_queries");
            
            if(dataQueries==null) { //Avoids Nullpointer Exception
                sb.append(" The data_queries element is missing from the collection "+collectionId+" .");
            }
            
            supportsPositionQuery = dataQueries != null && dataQueries.containsKey("position");
            
            if(supportsPositionQuery==false) { //Avoids Nullpointer Exception
            	continue;
            }


            if (supportsPositionQuery) {
                
                numberOfCollectionsWithPositionSupport++;
            	
                if(jsonResponse.getJsonObject("parameter_names")==null) { //Avoids Nullpointer Exception
                    continue;
                }            	

                HashMap parameterNames = jsonResponse.getJsonObject("parameter_names");
                Set parameterNamesSet = parameterNames.keySet();
                Iterator<String> parameterNamesIterator = parameterNamesSet.iterator();

                parameterNamesIterator.hasNext();
                String sampleParamaterName = parameterNamesIterator.next();

                if(jsonResponse.getList("crs")==null) { //Avoids Nullpointer Exception
                	sb.append(" The crs list is missing from the collection "+collectionId+" .");
                }                 
                
                List<String> crsList = jsonResponse.getList("crs");

                String supportedCRS = null;
                if(crsList != null) {
                    for (int q = 0; q < crsList.size(); q++) {
                        if (crsList.get(q).equals("CRS:84") || 
                                    crsList.get(q).equals("CRS84") || 
                                    crsList.get(q).equals("EPSG:4326") || 
                                    crsList.get(q).contains("www.opengis.net/def/crs/OGC/1.3/CRS84")) {
                            supportedCRS = crsList.get(q);
                        }
                    }
                }
                if (supportedCRS == null) {
                    sb.append(collectionId + " does not support CRS84 CRS. \n");

                }

                HashMap positionQuery = (HashMap) dataQueries.get("position");
                HashMap link = (HashMap) positionQuery.get("link");
                HashMap variables = (HashMap) link.get("variables");
                String supportedFormat = null;

                if(variables==null) { //Avoids Nullpointer Exception
                        sb.append(" The variables element is missing from the collection " + collectionId + ". ");
                } else {
                    ArrayList<String> outputFormatList = (ArrayList<String>) variables.get("output_formats");
                    supportedFormat = getSupportedFormat(outputFormatList);
                }

                double medianx = 0d;
                double mediany = 0d;

                HashMap extent = jsonResponse.getJsonObject("extent");
                if (extent.containsKey("spatial")) {

                    HashMap spatial = (HashMap) extent.get("spatial");

                    if (!spatial.containsKey("bbox"))
                    {
                        sb.append("spatial extent of collection "+collectionId+" missing bbox. \n");
                        continue;
                    }

                    ArrayList bboxEnv = (ArrayList) spatial.get("bbox"); // for some unknown reason the library returns JSON types as Integers only


                    ArrayList bbox = null;

                    if(bboxEnv.get(0).getClass().toString().contains("java.lang.Integer") ||
                            bboxEnv.get(0).getClass().toString().contains("java.lang.Double")||
                            bboxEnv.get(0).getClass().toString().contains("java.lang.Float")) {	//for EDR API V1.0.0
                        bbox = bboxEnv;

                    }
                    else if(bboxEnv.get(0).getClass().toString().contains("java.util.ArrayList")) {  //for EDR API V1.0.1
                        bbox = (ArrayList) bboxEnv.get(0);
                    }

                    if (bbox.size() > 3) {

                        if (bbox.get(0).getClass().toString().contains("Integer")
                                || bbox.get(0).getClass().toString().contains("Double")
                                || bbox.get(0).getClass().toString().contains("Float")) {
                            double minx = Double.parseDouble(bbox.get(0).toString());
                            double miny = Double.parseDouble(bbox.get(1).toString());
                            double maxx = Double.parseDouble(bbox.get(2).toString());
                            double maxy = Double.parseDouble(bbox.get(3).toString());



                            medianx = minx + ((maxx - minx) / 2d);
                            mediany = miny + ((maxy - miny) / 2d);

                        }

                    } else {
                        sb.append("bbox of spatial extent of collection" + collectionId
                                + " has fewer than four coordinates. \n");
                    }

                }
                else {  //if spatial extent is missing
                	sb.append(" The spatial extent element is missing from the collection "+collectionId+" .");
                }

                String sampleParamaterNameSafe = null;
                try {
                    sampleParamaterNameSafe = URLEncoder.encode(sampleParamaterName,"UTF8");
                }
                catch(Exception ex) {ex.printStackTrace();}

                String sampleDateTime = null;
                if (extent.containsKey("temporal")) {


                    HashMap temporal = (HashMap) extent.get("temporal");

                    if (!temporal.containsKey("interval"))
                    {

                        sb.append("Temporal extent of collection "+collectionId+" missing interval. \n");
                        continue;
                    }


                    ArrayList intervalEnv = (ArrayList) temporal.get("interval");


                    ArrayList interval = null;

                    if(intervalEnv.get(0).getClass().toString().contains("java.lang.String")) {
                        interval = intervalEnv;
                    }
                    else if(intervalEnv.get(0).getClass().toString().contains("java.util.ArrayList")) {

                        interval = (ArrayList) intervalEnv.get(0);
                    }




                    if (interval.size() > 1) {


                        sampleDateTime = interval.get(0)+"/"+interval.get(1);


                    }

                }
                else { //if temporal extent is missing
                	sb.append(" The temporal extent element is missing from the collection "+collectionId+" .");
                }



                String constructedURL = url + "/position?parameter-name="
                        + sampleParamaterNameSafe + "&coords=" + "POINT(" + medianx + "+"
                        + mediany + ")" + "&crs=" + supportedCRS + "&f=" + supportedFormat+"&datetime="+sampleDateTime;




                String pageContent = null;
                try {
                    pageContent = readStringFromURL(constructedURL,10);  //you can use Integer.MAX_VALUE for no limit

                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    sb.append(ex.getMessage() + " \n");
                }


                if(pageContent!=null) {

                    if(pageContent.contains("Coverage") || pageContent.contains("Feature")) {
                        //do nothing
                    }
                    else {
                        sb.append("Response of Position Query on collection " + collectionId
                                + " did not contain a recognised encoding. \n");
                    }

                }
                else {
                    sb.append("Response of Position Query on collection " + collectionId
                            + " was null. \n");
                }

            }

        }

        if(numberOfCollectionsWithPositionSupport==0) {
                sb.append(queryTypeNotSupported+"\n");
        }

      return sb.toString();
    }




}
