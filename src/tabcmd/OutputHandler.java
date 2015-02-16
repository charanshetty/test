package tabcmd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputHandler implements Runnable {
	
	
	private BufferedReader reader;
	
	public OutputHandler(InputStream is){
		this.reader = new BufferedReader(new InputStreamReader(is));
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String line = "";
		try {
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
