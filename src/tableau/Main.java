package tableau;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.HttpClient;
import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WebServiceClient client = new WebServiceClient();
		
		Properties property = new Properties();

			try {
				property.load(new FileInputStream("config.properties"));
				
				String server = property.getProperty("uri");
				String username = property.getProperty("username");
				String password = property.getProperty("password");
				String view = property.getProperty("view");
				
				try {
				client.authenticate(server,username,password);
				} catch (InvalidKeyException | NoSuchAlgorithmException
						| InvalidKeySpecException | NoSuchPaddingException
						| IllegalBlockSizeException | BadPaddingException
						| ParserConfigurationException | SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	

}
