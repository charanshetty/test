package csvDataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
public class Parameters {

		
	public Set<String> readCsv(String parmFile) throws IOException{
		Set<String> states = new HashSet<String>();
		
		 CSVReader reader = new CSVReader(new FileReader(parmFile) ,',');
	      String [] nextLine;
	      int lineNumber = 0;
	      while ((nextLine = reader.readNext()) != null) {
	        lineNumber++;
	        System.out.println("Line # " + lineNumber);
	        //System.out.println(nextLine);
	        // nextLine[] is an array of values from the line
	        System.out.println(nextLine[1]);
	        states.add(nextLine[1]);	
	}
	      return states;}
}
