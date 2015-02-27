package csvDataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

//-- Read CSV file as datasource
//--Take CSV file for filtering parameters
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public class CSVsource {

	public void test(String FilePath,ServletContext context)throws IOException {
        Properties props = new Properties();
       FileInputStream fis = null;
       fis=new FileInputStream(FilePath);
       System.out.println(FilePath);
       props.load(fis);
       String detail = context.getRealPath(props.getProperty("jdbc.detailjrxml")); 
	//	String detail = props.getProperty("jdbc.detailjrxml");
//		String summary = "reports/Pulse8CSVDetail.jrxml";
		String summary = context.getRealPath(props.getProperty("jdbc.summaryjrxml"));
		System.out.println(detail);
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jasperPrint2;
		try {
			jasperPrint2 = getJasperP(summary,props,context);
		
			jprintlist.add(jasperPrint2);
		JasperPrint jasperPrint1 = getJasperP(detail,props,context);
		jprintlist.add(jasperPrint1);
	
		generatePDF(jprintlist,props,context);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	public void responsetest(String FilePath,ServletContext context,StringBuffer data)throws IOException {
        Properties props = new Properties();
       FileInputStream fis = null;
       fis=new FileInputStream(FilePath);
       System.out.println(FilePath);
       props.load(fis);
       String detail = context.getRealPath(props.getProperty("jdbc.detailjrxml")); 
	//	String detail = props.getProperty("jdbc.detailjrxml");
//		String summary = "reports/Pulse8CSVDetail.jrxml";
		String summary = context.getRealPath(props.getProperty("jdbc.summaryjrxml"));
		System.out.println(detail);
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jasperPrint2;
		try {
			jasperPrint2 = getJasperStringP(summary,props,context,data);
		
			jprintlist.add(jasperPrint2);
		JasperPrint jasperPrint1 = getJasperStringP(detail,props,context,data);
		jprintlist.add(jasperPrint1);
	
		generatePDF(jprintlist,props,context);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	private void generatePDF(List<JasperPrint> jprintlist,Properties props,ServletContext context) throws JRException,
			FileNotFoundException {

		OutputStream stream = new FileOutputStream(new File(
				context.getRealPath(props.getProperty("jdbc.destinationPDF"))));
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		System.out.println("complete");
		// TODO Auto-generated method stub

	}

	private JasperPrint getJasperP(String File,Properties props,ServletContext context) throws JRException, IOException {
		JasperReport jasperReport = JasperCompileManager.compileReport(File);

		long start = System.currentTimeMillis();

		// data source filling

		Map<String, Object> parameters = new HashMap<String, Object>();
	//	parameters.put("ReportTitle", "Address Report");
	//	parameters.put("DataFile", "pulse8Data.csv - CSV data source");
		Set<String> states = new HashSet<String>();
		Parameters params= new Parameters();
		states= params.readCsv(context.getRealPath(props.getProperty("jdbc.paramsource")));
		parameters.put("InterPIDs", states);
		JRCsvDataSource dataSource = new JRCsvDataSource(
				JRLoader.getLocationInputStream(context.getRealPath(props.getProperty("jdbc.datasource"))));
		dataSource.setRecordDelimiter("\r\n");
		 dataSource.setUseFirstRowAsHeader(true);
		//dataSource.setColumnNames(columnNames);
		JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport,
				parameters, dataSource);

		return jasperPrint1;

	}

	private JasperPrint getJasperStringP(String File,Properties props,ServletContext context,StringBuffer data) throws JRException, IOException {
		JasperReport jasperReport = JasperCompileManager.compileReport(File);

		long start = System.currentTimeMillis();

		// data source filling


		
		Map<String, Object> parameters = new HashMap<String, Object>();
		Set<String> states = new HashSet<String>();
		 String a[] = data.toString().split("\n");
		int i=1;
		StringTokenizer st;
		 while(i < a.length){
		states.add(a[i].split(",")[1]);
	System.out.println(a[i].split(",")[1]+"here");	
		 i++;}
		System.out.println(states.toString());
		
	//	parameters.put("ReportTitle", "Address Report");
	//	parameters.put("DataFile", "pulse8Data.csv - CSV data source");
		
		Parameters params= new Parameters();
	//	states= params.readCsv(context.getRealPath(props.getProperty("jdbc.paramsource")));
		parameters.put("InterPIDs", states);
		//parameters.put("InterventionProvID", states);
		
		JRCsvDataSource dataSource = new JRCsvDataSource(
				JRLoader.getLocationInputStream(context.getRealPath(props.getProperty("jdbc.datasource"))));
		dataSource.setRecordDelimiter("\r\n");
		 dataSource.setUseFirstRowAsHeader(true);
		//dataSource.setColumnNames(columnNames);
		JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport,
				parameters, dataSource);

		return jasperPrint1;

	}
	
	
/*	public static void main(String[] args) throws JRException,
			IOException {
		CSVsource csvsource = new CSVsource();
		csvsource.test("data/jdbc.properties");
	}*/
}
