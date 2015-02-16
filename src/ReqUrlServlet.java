
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        final String user = "charan";
        //final String wgserver = "localhost:8000";
        final String wgserver = "localhost:8000";
        final String dst = "views/Book2_uday/Sheet2";
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
            throw new ServletException("Invalid ticket " + ticket);
        ServletContext context=this.getServletConfig().getServletContext();
	Test test = new Test();
	test.getView(context);
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

 