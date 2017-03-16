import java.io.*;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
	            
	        	System.out.println(args[0]+"\n"+args[1]);
	        	File f = new File(args[2]);
	        	String s ="";
	        	String re="";
	        	String []temp;
	        	File f1 = new File(args[1] + "/mygit.html");
	        	
	            BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(f1));
					bw.write("<html>");
					bw.write("<head><title>GitReport</title></head>");
					bw.write("</br></br>");
					bw.write("<body style=\"background-color:#F5FAFA\"");
					bw.write("<form><fieldset style=\"background-color:#87cefa\">");
					bw.write("<legend><b>Results</b></legend>");
					bw.write("Files : " + executeCommand("cmd /C git ls-files | find /c /v \"\"",f) +"<br>");
					s= executeCommand("git diff --shortstat 4b825dc642cb6eb9a060e54bf8d69288fbee4904 ",f);	
			        re=findLines(s);
			        bw.write("Lines : "+re+"<br>");
			        bw.write("Branches : "+executeCommand("cmd /C git branch -ar | find /c /v \"\"",f)+"<br>");
			        bw.write("Tags : "+executeCommand("cmd /C git tag -n | find /c /v \"\"",f)+"<br>");
			        bw.write("Commits : "+executeCommand("cmd /C git log | find /c /v \"\"",f)+"<br>");
			        bw.write("Commiters : "+executeCommand("cmd /C git log --pretty=\"%an %ae%n%cn %ce\" | sort | uniq | wc -l",f)+"<br>");
			        bw.write("Commits per Contributor : "+executeCommand("git shortlog -s -n --all",f));
					bw.write("</fieldset>");
					bw.write("</form>");		 
					 
					 bw.write("</body>");
					 bw.write("</html>");
					 bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	
	        	s=executeCommand("git for-each-ref --sort=-committerdate refs/heads/",f);
	        
	        	String[] parts =s.split("\n");
	        	for(String part : parts)
	        	{
	        		temp=part.split(" ");
	        		System.out.println(temp[0]);
	        	}
	            System.exit(0);	
	    }
	
	private static String executeCommand(String command,File f) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command,null,f);
			p.waitFor();
			BufferedReader reader =new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	private static String findLines(String result) {
		
		String[] bits = result.split(",");
		String lastword = bits[1];
		bits = lastword.split(" ");
		return bits[1];
	}
	
	}

