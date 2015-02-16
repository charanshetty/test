package csvDataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;


public class DataSourceCSV {

	
	
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();

		// data source filling
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("DataFile", "CsvDataSource.txt - CSV data source");
			Set<String> states = new HashSet<String>();
			states.add("Active");
			states.add("Trial");
			parameters.put("IncludedStates", states);

			String[] columnNames = new String[]{"city", "id", "name", "address", "state"};
			JRCsvDataSource dataSource = new JRCsvDataSource(JRLoader.getLocationInputStream("data/CsvDataSource.txt"));
			dataSource.setRecordDelimiter("\r\n");
//				dataSource.setUseFirstRowAsHeader(true);
			dataSource.setColumnNames(columnNames);
			
			JasperFillManager.fillReportToFile("build/reports/CsvDataSourceReport.jasper", parameters, dataSource);
			System.err.println("Report : CsvDataSourceReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
		}

		
		// query executer filling
		{
			start = System.currentTimeMillis();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("DataFile", "CsvDataSource.txt - CSV query executer");
			Set<String> states = new HashSet<String>();
			states.add("Active");
			states.add("Trial");
			parameters.put("IncludedStates", states);

			JasperFillManager.fillReportToFile("build/reports/CsvQueryExecuterReport.jasper", parameters);
			System.err.println("Report : CsvQueryExecuterReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
		}
	}
	
	protected File[] getFiles(File parentFile, String extension)
	{
		List<File> fileList = new ArrayList<File>();
		String[] files = parentFile.list();
		if (files != null)
		{
			for(int i = 0; i < files.length; i++)
			{
				String reportFile = files[i];
				if (reportFile.endsWith("." + extension))
				{
					fileList.add(new File(parentFile, reportFile)); 
				}
			}
		}
		return fileList.toArray(new File[fileList.size()]);
	}
	
	
	
	public void pdf() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.exportReportToPdfFile(reportFile.getAbsolutePath());
			System.err.println("Report : " + reportFile + ". PDF creation time : " + (System.currentTimeMillis() - start));
		}
	}

	public void test() throws JRException {
		// TODO Auto-generated method stub
		HashMap jasperParameter = new HashMap();
		JasperReport jasperReport;
		JasperPrint jasperPrint;
		// jrxml compiling process
		jasperReport = JasperCompileManager.compileReport("reports/CsvDataSourceReport.jrxml");
//		("C://Users//admin//workspace//csvdatasource//reports");

		// filling report with data from data source
		
		fill();
		pdf();
	}

	
/*	public static void main(String[] args) throws JRException
	{
	//	main(new DataSourceCSV(), args);

		DataSourceCSV dataSourceCSV=new DataSourceCSV();
		dataSourceCSV.test();
	}
	
	*/
	
	
	
	
}
