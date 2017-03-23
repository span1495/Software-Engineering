import java.io.*;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
	            
	        	File f = new File(args[0]);
	        	String s ="";
	        	String re="";
	        	String re2="";
	        	String re3="";
	        	String []temp;
	        	String []temp2;
	        	String []temp3;
	        	String []temp4;
	        	String []temp5;
	        	File f1 = new File(args[1] + "/mygit.html");
	            BufferedWriter bw,bw2;
	            File myDir = new File(args[1], "userReports");
	            if (!myDir.exists()) {
	                System.out.println("creating directory: " + myDir.getName());
	                boolean result = false;
	                myDir.mkdir();
	                result = true;          
	                if(result) {    
	                    System.out.println("DIR created");  
	                }
	            }
	     
				try {
					bw = new BufferedWriter(new FileWriter(f1));
					bw.write("<html>");
					bw.write("<head><title>GitReport</title></head>");
					bw.write("</br></br><link rel=\"stylesheet\" href=\""+args[0]+"/my.css\">");
					bw.write("<body>");
					bw.write("<form><fieldset style=\"background-color:#87cefa\">");
					bw.write("<legend><b>Results</b></legend>");
					bw.write("Files : " + executeCommand("cmd /C git ls-files | find /c /v \"\"",f) +"<br>");
					s= executeCommand("git diff --shortstat 4b825dc642cb6eb9a060e54bf8d69288fbee4904 ",f);	
			        re=findLines(s);
			        //git log --shortstat --author span1495 --since "1 weeks ago" | grep "files changed"
			        bw.write("Lines : "+re+"<br>");
			        bw.write("Branches : "+executeCommand("cmd /C git branch -ar | wc -l",f)+"<br>");
			        bw.write("Tags : "+executeCommand("cmd /C git tag -n | find /c /v \"\"",f)+"<br>");
			        String commits =executeCommand("git rev-list --all --count",f);
			        commits=commits.replace("\n","");
			        int com=Integer.valueOf(commits);
			        bw.write("Commits : "+commits+"<br>");
			        bw.write("Commiters : "+executeCommand("cmd /C git log --pretty=\"%an %ae%n%cn %ce\" | sort | uniq | wc -l",f)+"<br>");
					bw.write("</fieldset>");
					bw.write("</form></br>");
					re=executeCommand("git shortlog -s -n --all",f);
					re=re.replaceAll(" ","");					
					temp=re.split("\n");
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Name</th><th>Percentage</th></tr>");
					for(int i=0;i<temp.length;i++)
					{
						temp2=temp[i].split("\t");
						bw.write("<tr><td>"+temp2[1]+"</td>");
						bw.write("<td>");
						String myf = String.format("%.02f", Float.valueOf(temp2[0])*100/com);
					    bw.write(myf);
					    bw.write("%</td></tr>");
						
					}
					bw.write("</table></br>");
					re=executeCommand("git branch",f);
					re=re.replaceAll(" ","");
					temp=re.split("\n");
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Branch</th><th>Date of creation</th><th>Last Modifaction</th><th>Percentage %</th></tr>");
					for(String t : temp )
					{
						t=t.replace("*","");
						t=t.replace("\n","");				
						re=executeCommand("cmd /C git log "+ t + "|grep Date ",f);
						bw.write("<td><b><a target=\"_blank\" href="+args[1]+"\\userReports\\"+t+".html>" + t+ "</a></b></td>");
						temp2=re.split("\n");
						temp2[0]=temp2[0].replace("Date:","");
						temp2[0]=temp2[0].substring(0,(temp2[0].length())-5);
						temp2[temp2.length-1]=temp2[temp2.length-1].replace("Date:","");
						temp2[temp2.length-1]=temp2[temp2.length-1].substring(0,(temp2[temp2.length-1].length())-5);
						bw.write("<td>"+temp2[temp2.length-1]+"</td>");
						bw.write("<td>"+temp2[0]+"</td>");
						re=executeCommand("cmd /C git log "+ t + "|grep Date: |wc -l ",f);
						String myf = String.format("%.02f", Float.valueOf(re)*100/com);
						bw.write("<td>"+myf+" %</td>");
						bw.write("</tr>");
						
						//-----------------------BRANCHES---------------------------//
						File f2 = new File(args[1]+"/userReports/"+t+".html");
						bw2 = new BufferedWriter(new FileWriter(f2));
						bw2.write("<html>");
						bw2.write("<head><title>"+t+"</title></head><link rel=\"stylesheet\" href=\""+args[0]+"/my.css\">");
						bw2.write("<body>");
						bw2.write("<div class=\"flex-container\"><header>Branch : "+ t+"</header></div>");
						bw2.write("<table border=\"1\"");
						bw2.write("<tr><th>Id</th><th>Message</th><th>Date</th><th>Commiter</th><th>Tags</th></tr>");
						re=executeCommand("git log "+t +" --oneline",f);
						re2=executeCommand("cmd /C git log "+t +" --date=format:%Y-%m-%d | grep Date:",f);
						re3=executeCommand("cmd /C git log "+t +" | grep Author:",f);
						temp2=re2.split("\n");
						temp3=re3.split("\n");
						temp4=re.split("\n");
						for(int i=0;i<temp3.length;i++)
						{
							temp5=temp4[i].split(" ");
							bw2.write("<tr><td>"+temp5[0]+"</td>");
							re=temp5[0];
							bw2.write("<td>"+temp5[1]+"</td>");
							temp5=temp2[i].split(":");
							bw2.write("<td>"+temp5[1]+"</td>");
							temp5=temp3[i].split(":");
							bw2.write("<td>"+temp5[1]+"</td>");
							
							re=executeCommand("git tag --contains "+re,f);
							bw2.write("<td>"+re+"</td>");
							bw2.write("</tr>");
						}
						bw2.write("</br></br>");
						bw2.write("</body");
						bw2.write("</html>");
						bw2.close();
				
					}
					
					 bw.write("</table>");
					 bw.write("</body>");
					 bw.write("</html>");
					 bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				

	            System.exit(0);	
	    }
	
	private static String executeCommand(String command,File f) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command,null,f);

			BufferedReader reader =new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			p.waitFor();
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

