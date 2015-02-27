package tableau;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import csvDataSource.CSVsource;

public class WebServiceClient {
	
	private String getSession(HttpResponse response){
		String cookie = response.getLastHeader("Set-Cookie").toString();
		String session = "";
		String sessionEntry = Arrays.asList(cookie.split(";")).get(0);
		sessionEntry.replaceAll("\\s+","");
		session = Arrays.asList(sessionEntry.split("=")).get(1);
		return session;
	}

	private void printHeaders(HttpResponse response){
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {

			System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
		}
	}
	private void printHeaders(HttpGet request){
		Header[] headers = request.getAllHeaders();
		for (Header header : headers) {

			System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
		}
	}

	private static StringBuffer readResponse(HttpResponse httpresp) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpresp.getEntity().getContent()));

		StringBuffer strbuffer = new StringBuffer();
		String currentline = "";
		while ((currentline = bufferedReader.readLine()) != null) {
			strbuffer.append(currentline);
		}
		return strbuffer;
	}
	
	private static StringBuffer readCSV(HttpResponse httpresp) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpresp.getEntity().getContent()));

		StringBuffer strbuffer = new StringBuffer();
		String currentline = "";
		while ((currentline = bufferedReader.readLine()) != null) {
			strbuffer.append(currentline + "\n");
		}
		return strbuffer;
	}

	private  List<NameValuePair> getAuthPostData(HttpResponse response,String username,String password) throws IOException, ParserConfigurationException, SAXException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		/*String cookie = response.getFirstHeader("Set-Cookie").getValue();
		String session = getSession(cookie);
		//System.out.println(session); */

		StringBuffer result = readResponse(response);

		// Parse XML FROM the result
		StringReader reader = new StringReader(result.toString());

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(reader);

		Document doc = db.parse(is);

		// Get Required data for creating the authentication request, such as
		// modulus and exponent of the RSA public key and the authencity_token
		String modulusstr = null;
		String exponentstr = null;
		String authenticity_token = null;
		//String version = null;

		//Node element = doc.getElementsByTagName("authinfo").item(0);
		/*for (int i = 0; i < elements.getLength(); i++) {
			NodeList moduluses = ((Element) elements.item(i))
					.getElementsByTagName("modulus");
			for (int k = 0; k < moduluses.getLength(); k++) {
				modulusstr = moduluses.item(k).getTextContent();
			}
			NodeList exponents = ((Element) elements.item(i))
					.getElementsByTagName("exponent");
			for (int k = 0; k < exponents.getLength(); k++) {
				exponentstr = exponents.item(k).getTextContent();
			}
			NodeList authenticity_tokens = ((Element) elements.item(i))
					.getElementsByTagName("authenticity_token");
			for (int k = 0; k < exponents.getLength(); k++) {
				authenticity_token = authenticity_tokens.item(k).getTextContent();
				System.out.println(authenticity_tokens);
			}
		}*/

		NodeList elements = doc.getElementsByTagName("authinfo");
		modulusstr = ((Element)elements.item(0)).getElementsByTagName("modulus").item(0).getTextContent();
		exponentstr = ((Element)elements.item(0)).getElementsByTagName("exponent").item(0).getTextContent();
		authenticity_token = ((Element)elements.item(0)).getElementsByTagName("authenticity_token").item(0).getTextContent();
		//version = ((Element)elements.item(0)).getElementsByTagName("authenticity_token").item(0).getTextContent();


		// Parse the modulus and exponent into a BigInteger and create an RSA
		// public key from it
		BigInteger modulus = new BigInteger(modulusstr, 16);
		BigInteger exponent = new BigInteger(exponentstr, 16);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub = new RSAPublicKeySpec(modulus, exponent);
		PublicKey pubkey = keyFactory.generatePublic(pub);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);

		// Encrypt the password with the created public key
		byte[] cipherData = cipher.doFinal(password.getBytes());
		String cryptedpass = Hex.encodeHexString(cipherData);

		// Create a post request for the authentication

		//System.out.println(authenticity_token + " "+ cryptedpass +" "+ username);
		// Fill in parameters

		nvps.add(new BasicNameValuePair("authenticity_token", authenticity_token));
		nvps.add(new BasicNameValuePair("crypted", cryptedpass));
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("format", "xml"));	

		return nvps;
	}

	public  void authenticate(String serveraddress, String username,String password,String csvUrl,ServletContext context) throws ClientProtocolException,IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ParserConfigurationException, SAXException {
		// Initialize apache HttpClient
		CloseableHttpClient client = HttpClientBuilder.create().build();

		// Create Http Get request for authentication informations
		String url = serveraddress + "/auth?format=xml";
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		request.releaseConnection();
		//Get data for response
		List<NameValuePair> nvps = getAuthPostData(response,username,password);
		HttpPost postrequest = new HttpPost(serveraddress + "/auth/login");
		// bind parameters to the request
		postrequest.setEntity(new UrlEncodedFormEntity(nvps));

		HttpResponse postResponse = client.execute(postrequest);
		
		//=>>> Check status code
		
		StringBuffer result = readResponse(postResponse);
		StringReader reader = new StringReader(result.toString());

		//printHeaders(postResponse);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(reader);



		Document doc = db.parse(is);

		if(doc.getElementsByTagName("error/sites").equals(null)){
			if(!doc.getElementsByTagName("successful_login/user/site_prefix").item(0).getTextContent().isEmpty()){
				// @site_prefix = prefix.gsub(%r<^/>,'') + "/"
			}		
		};
		
		NodeList success = doc.getElementsByTagName("successful_login");
		String authenticity_token = ((Element)success.item(0)).getElementsByTagName("authenticity_token").item(0).getTextContent();
		String workgroup_session_id = getSession(postResponse);
		//System.out.println(workgroup_session_id);
		String csvurl =serveraddress+csvUrl;
	//	String csvurl = serveraddress + "/views/Book2_uday/Dashboard1?format=csv";
		HttpGet requestNew = new HttpGet(csvurl);
		//System.out.println(csvurl);
		request.setHeader("Cookie",workgroup_session_id);
		
		//System.out.println(request.getFirstHeader("Cookie").getValue());
		try{
			HttpResponse responseNew = client.execute(requestNew);
			StringBuffer data= new StringBuffer();
			data=readCSV(responseNew);
			System.out.println(data.toString());
			CSVsource tmp= new CSVsource();
			tmp.responsetest("C:\\Users\\admin\\workspace\\test\\data\\jdbc.properties", context, data);
		}
		catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      // Release the connection.
		      request.releaseConnection();
		    }  
		client.close();




	}
}
