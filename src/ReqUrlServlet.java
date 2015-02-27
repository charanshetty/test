import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.catalina.startup.WebappServiceLoader;
import org.xml.sax.SAXException;

import tableau.WebServiceClient;
import tabcmd.Test;

/**
 * Servlet implementation class for Servlet: TableauServlet
 *
 */
@WebServlet("/ReqUrlServlet/*")
 public class ReqUrlServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

	public ReqUrlServlet() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//	String userName=request.getParameter("username");
	//	String password=request.getParameter("password");
	//	String customView=request.getParameter("customView");
		
		String json = request.getParameter("paramString");
		JSONObject obj = new JSONObject();
		
		String customView="";
		String userName="";
		String password="";
		try {
			customView = obj.getString("customView");
			 userName=obj.getString("username");
			 password=obj.getString("password");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(json);
		System.out.println(userName);
		System.out.println(password);
		System.out.println(customView);
		System.out.println("ppr");
		
	/*	final String user = "charan";
        //final String wgserver = "localhost:8000";
        final String wgserver = "localhost:8000";
        final String dst = "views/Pulse8Mod/A";
        //final String params = ":embed=yes&:toolbar=top&:customViews=yes";
       final String params="/charan/abc.csv";
        String ticket = getTrustedTicket(wgserver, user, request.getRemoteAddr());
        if ( !ticket.equals("-1") ) {
        //    response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
         //   response.setHeader("Location", "http://" + wgserver + "/trusted/" + ticket + "/" + dst  + params);
            System.out.println(ticket+"get");
    		//response.getWriter().write(ticket);
            System.out.println(response.getHeader(getServletInfo()));
          //  response.sendRedirect(request.getParameter'("url"));
            System.out.println(request.getParameter("url"));
            
        }
        else
            // handle error
            throw new ServletException("Invalid ticket " + ticket);*/
        ServletContext context=this.getServletConfig().getServletContext();
        WebServiceClient serviceClient = new WebServiceClient();
        String url="/views/Pulse8Mod/A/"+userName+"/"+customView+".csv";
        System.out.println(url);
		try {
			serviceClient.authenticate("http://localhost:8000", userName, password,url,context);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| InvalidKeySpecException | NoSuchPaddingException
					| IllegalBlockSizeException | BadPaddingException
					| ParserConfigurationException | SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        //Test test = new Test();
	//test.getView(context,userName,password,customView);
	
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
                System.out.println(line+"1");
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

 