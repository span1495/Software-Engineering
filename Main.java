import java.io.*;

public class Main {

	public static void main(String[] args) {
		 String s = null;

	        try {
	            
	        	System.out.println(args[0]+"\n"+args[1]);
	        	File f = new File(args[2]);
	        	Process p = Runtime.getRuntime().exec("git ls-files",null,f);
	        	p =Runtime.getRuntime().exec("git diff --stat 4b825dc642cb6eb9a060e54bf8d69288fbee4904",null,f);
	            
	            BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

	            BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));

	            // read the output from the command
	            System.out.println("Here is the standard output of the command:\n");
	            int counter=0;
	            while ((s = stdInput.readLine()) != null) {
	            	counter++;
	                System.out.println(s);
	            }
	            System.out.println("Number of files :"+counter);
	            // read any errors from the attempted command
	            System.out.println("Here is the standard error of the command (if any):\n");
	            while ((s = stdError.readLine()) != null) {
	                System.out.println(s);
	            }
	            
	            System.exit(0);
	        }
	        catch (IOException e) {
	            System.out.println("exception happened - here's what I know: ");
	            e.printStackTrace();
	            System.exit(-1);
	        }
	    }

	}

