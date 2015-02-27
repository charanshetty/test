package tabcmd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class Test {

	// public static void main(String[] args)
	public void getView(ServletContext context,String username,String password,String customView) throws IOException {
		String filePath = "C:\\Users\\admin\\workspace\\test\\tabconfig.properties";
		Properties property = new Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(filePath);

			property.load(fis);
			// property.load(new
			// FileInputStream("C:\\Users\\admin\\workspace\\Pulse8_1\\config.properties"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("here1");
		String server = property.getProperty("uri");
		//String username = property.getProperty("username");
		//String password = property.getProperty("password");
		String view = property.getProperty("view");
		customView="abc";
		view=view+"/"+username+"/"+customView;
		//String view = property.getProperty("view");
		System.out.println(view+"asff");
		
		String dest=context.getRealPath((property.getProperty("dest")));
		/*
		 * String server = "http://localhost:8000"; String username = "charan";
		 * String password = "a1s2d3"; String view =
		 * "Book2_uday/Sheet2/charan/abc";
		 */

		Tabcmd tc = new Tabcmd();
		try {
			tc.login(server, username, password);
			tc.export(view,dest);
			tc.disconnect();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
