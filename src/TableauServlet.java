
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: TableauServlet
 *
 */
@WebServlet("/TableauServlet/*")
 public class TableauServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

	public TableauServlet() {
		super();
	}   	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		//String wgserver = property.getProperty("wgserver");
		String URLuser = property.getProperty("URLuser");
		String dst = property.getProperty("dst");
		String params = property.getProperty("params");
		  final String wgserver = "localhost:8000";
        Properties prop = new  Properties();
        System.out.println(wgserver);
        String ticket = getTrustedTicket(wgserver, URLuser, request.getRemoteAddr());
        
        
        
        
        System.out.println(ticket);
        if ( !ticket.equals("-1") ) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "http://" + wgserver + "/trusted/" + ticket + "/" + dst + "?" + params);
            System.out.println(ticket);
    		response.getWriter().write(ticket);
    		response.sendRedirect(request.getParameter("url"));
        }
        else
            // handle error
            throw new ServletException("Invalid ticket " + ticket);
	}  	
	
    // the client_ip parameter isn't necessary to send in the POST unless you have
    // wgserver.extended_trusted_ip_checking enabled (it's disabled by default)
	private String getTrustedTicket(String wgserver, String user, String remoteAddr) 
        throws ServletException 
    {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        try {
            // Encode the parameters
            StringBuffer data = new StringBuffer();
            data.append(URLEncoder.encode("username", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(user, "UTF-8"));
            data.append("&");
            data.append(URLEncoder.encode("client_ip", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(remoteAddr, "UTF-8"));
            
            // Send the request
            URL url = new URL("http://" + wgserver + "/trusted");
            System.out.println(url);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data.toString());
            out.flush();
            
            // Read the response
            StringBuffer rsp = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ( (line = in.readLine()) != null) {
                rsp.append(line);
            }
            
            return rsp.toString();
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
        finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            }
            catch (IOException e) {}
        }
    }
}

 