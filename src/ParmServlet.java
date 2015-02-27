

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.codec.Base64.OutputStream;

/**
 * Servlet implementation class ParmServlet
 */
@WebServlet("/ParmServlet")
public class ParmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String userName=request.getParameter("username");
	String password=request.getParameter("password");
	String customView=request.getParameter("customView");
	System.out.println(userName);
	System.out.println(password);
	System.out.println(customView);
	Properties prop = new Properties();
	FileOutputStream output=null;
	
	
	output = new FileOutputStream("config.properties");
	
	customView="dasd";
	
	prop.setProperty("username", userName);
	prop.setProperty("password",password);
	prop.setProperty("customView", customView);
	
	}

}
