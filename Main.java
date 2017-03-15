import java.io.*;

public class Main {

	public static void main(String[] args) {
	            
	        	System.out.println(args[0]+"\n"+args[1]);
	        	File f = new File(args[2]);
	        	
	        	//Process p = Runtime.getRuntime().exec("git ls-files",null,f);
	        	//p =Runtime.getRuntime().exec("git diff --stat 4b825dc642cb6eb9a060e54bf8d69288fbee4904",null,f);
	        	//p =Runtime.getRuntime().exec("git branch -ar",null,f);
	        	//p =Runtime.getRuntime().exec("git tag",null,f);
	        	//p =Runtime.getRuntime().exec("git log",null,f);
	        	
	        	System.out.println("Files: " + executeCommand("cmd /C git ls-files | find /c /v \"\"",f));
	        	System.out.println("Lines: "+ executeCommand("git diff --shortstat 4b825dc642cb6eb9a060e54bf8d69288fbee4904 ",f));
	        	System.out.println("Branches : " + executeCommand("cmd /C git branch -ar | find /c /v \"\"",f));
	        	System.out.println("Tags: "+ executeCommand("cmd /C git tag -n | find /c /v \"\"",f));
	        	System.out.println("Commits : "+ executeCommand("cmd /C git log | find /c /v \"\"",f));
	        	System.out.println("Commiters : "+ executeCommand("cmd /C  git log | findstr Author: | sort | uniq ",f));
	            System.exit(0);
	    }
	
	private static String executeCommand(String command,File f) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command,null,f);
			p.waitFor();
			BufferedReader reader =
                           new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	
	

	}

