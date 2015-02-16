package tabcmd;




import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Tabcmd {
	
	
	private ProcessBuilder pb;

	public Tabcmd(){
		pb = new ProcessBuilder();
	}
	
	private int execute(ProcessBuilder pb) throws IOException, InterruptedException{
		Process process = pb.start();
		new Thread(new OutputHandler(process.getInputStream())).start();
		new Thread(new OutputHandler(process.getErrorStream())).start();
		int status = process.waitFor();
		return status;
	}
	
	public int login(String server,String username,String password) throws IOException, InterruptedException{
		List<String> cmd = Arrays.asList("java", "-jar", "C:\\Users\\admin\\workspace\\Pulse8_1\\lib\\tabcmd.jar","login","-s",server,"-u",username,"-p",password);
		pb.command(cmd);
		int status = execute(pb);
		System.out.println("Exited with status: " + status);
		return status;
	}
	
	public int export(String view,String dest) throws IOException, InterruptedException{
	System.out.println(dest);
		List<String> cmd = Arrays.asList("java", "-jar", "C:\\Users\\admin\\workspace\\Pulse8_1\\lib\\tabcmd.jar","export",view,"--csv","-f",dest);
		System.out.println(cmd);
		pb.command(cmd);
		int status = execute(pb);
		System.out.println("Exited with status: " + status);
		return status;
	}
	
	public int disconnect() throws IOException, InterruptedException{
		List<String> cmd = Arrays.asList("java", "-jar", "C:\\Users\\admin\\workspace\\Pulse8_1\\lib\\tabcmd.jar","logout");
		pb.command(cmd);
		int status = execute(pb);
		System.out.println("Exited with status: " + status);
		return status;
	}
}
