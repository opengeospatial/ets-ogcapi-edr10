package org.opengis.cite.ogcapiedr10.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Process {
	
	public void process() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader b = new BufferedReader(new FileReader("/Users/ogccite/Documents/GitHub/ets-ogcapi-edr10/src/main/java/org/opengis/cite/ogcapiedr10/collections/AreaCollections.java"));
		String line = b.readLine();
		while(line!=null)
		{
			sb.append(line+"\n");
			
			line = b.readLine();
		}
		b.close();
		
		
		String[] resources = { "Locations", "Position", "Radius", "Trajectory", "Cube", "Corridor"};
		
		for(String res:resources)
		{
			PrintWriter fw = new PrintWriter(new FileWriter("/Users/ogccite/Documents/GitHub/ets-ogcapi-edr10/src/main/java/org/opengis/cite/ogcapiedr10/collections/"+res+"Collections.java"));
			fw.println(sb.toString().replace("Area",res).replace("area",res.toLowerCase()));
			fw.close();
			
		}
		
		
	}

	public static void main(String[] args) {
		Process p = new Process();
		try {
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
