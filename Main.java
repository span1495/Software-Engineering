import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws ParseException {
	            if(args.length<2)
	            {
	            	System.out.println("Wrong number of arguments given!");
	            	System.exit(-1);
	            }
	        	File f = new File(args[0]);
	        	String s ="";
	        	String re="";
	        	String re2="";
	        	String []temp;
	        	String []temp2;
	        	String []temp3;
	        	String []temp4;
	        	String []temp5;
	        	int count=0;
	        	List<String> branches = new ArrayList<String>();
	        	List<String> branch_count = new ArrayList<String>();
	        	List<String> users = new ArrayList<String>();
	        	List<String> dates = new ArrayList<String>();
	        	List<String> authors = new ArrayList<String>();
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
					bw.write("</br></br><link rel=\"stylesheet\" href=\"my.css\">");
					bw.write("<body>");
					bw.write("<form><fieldset style=\"background-color:#87cefa\">");
					bw.write("<legend><b>Results</b></legend><link rel=\"stylesheet\" href=\"../my.css\">");
					count=countResults("git ls-files",f);
					bw.write("Files : " + count +"<br>");
					s= executeCommand("git diff --shortstat 4b825dc642cb6eb9a060e54bf8d69288fbee4904",f);
			        re=findLines(s);
			        bw.write("Lines : "+re+"<br>");
			        count=countResults("git branch -ar",f);
			        bw.write("Branches : "+(count-1)+"<br>");
			        count=countResults("git tag -n",f);
			        bw.write("Tags : "+count+"<br>");
			        String commits =executeCommand("git rev-list --all --count",f);
			        commits=commits.replace("\n","");
			        int com=Integer.valueOf(commits);
			        bw.write("Commits : "+commits+"<br>");
			        count=countResults("git shortlog -sn --all",f);
			        bw.write("Commiters : "+count+"<br>");
					bw.write("</fieldset>");
					bw.write("</form></br>");
					re=executeCommand("git branch",f);
					re=re.replaceAll(" ","");
					temp=re.split("\n");
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Branch</th><th>Date of creation</th><th>Last Modifaction</th><th>Percentage %</th></tr>");
					String previous="";
					for(String t : temp )
					{	
						t=t.replace("*","");
						t=t.replace("\n","");
						if(previous.compareTo("")==0)
							re=executeCommand("git log "+ t,f);
						else
							re=executeCommand("git log "+ t+"..."+previous,f);
						previous=t;
						temp2=re.split("\n");
						int counter=0;
						for(int i=0;i<temp2.length;i++)
						{
							if(temp2[i].contains("Date"))
								counter++;
						}
						branches.add(t);
						bw.write("<td><b><a target=\"_blank\" href=\"userReports\\"+t+".html\">"+ t+ "</a></b></td>");
						temp2[2]=temp2[2].replace("Date:","");
						temp2[2]=temp2[2].substring(0,(temp2[2].length())-5);
						int place=0;
						for(int j=temp2.length-1;j>0;j--)
						{
							if(temp2[j].contains("Date"))
							{
								place=j;
								break;
							}
						}
						temp2[place]=temp2[place].replace("Date:","");
						temp2[place]=temp2[place].substring(0,(temp2[place].length())-5);
						bw.write("<td>"+temp2[place]+"</td>");
						
						bw.write("<td>"+temp2[2]+"</td>");
						String myf = String.format("%.02f", Float.valueOf(counter)*100/com);
						bw.write("<td>"+myf+" %</td>");
						bw.write("</tr>");
						
						//-----------------------BRANCHES---------------------------//
						File f2 = new File(args[1]+"/userReports/"+t+".html");
						bw2 = new BufferedWriter(new FileWriter(f2));
						bw2.write("<html>");
						bw2.write("<head><title>"+t+"</title></head><link rel=\"stylesheet\" href=\"../my.css\">");
						bw2.write("<body>");
						bw2.write("<div class=\"flex-container\"><header>Branch : "+ t+"</header></div>");
						bw2.write("<table border=\"1\"");
						bw2.write("<tr><th>Id</th><th>Message</th><th>Date</th><th>Commiter</th><th>Tags</th></tr>");
						re=executeCommand("git log "+t +" --oneline",f);
						re2=executeCommand("git log "+t +" --date=format:%Y-%m-%d",f);
						temp2=re2.split("\n");
						temp4=re.split("\n");
						dates=new ArrayList<String>();
						authors=new ArrayList<String>();
						for(int j=0;j<temp2.length;j++)
						{
							if(temp2[j].contains("Date:"))
								dates.add(temp2[j]);
							if(temp2[j].contains("Author:"))
								authors.add(temp2[j]);	
						}
						
						for(int i=0;i<authors.size();i++)
						{
							temp5=temp4[i].split(" ");
							bw2.write("<tr><td>"+temp5[0]+"</td>");
							re=temp5[0];
							bw2.write("<td>"+temp5[1]+"</td>");
							temp5=dates.get(i).split(":");
							bw2.write("<td>"+temp5[1]+"</td>");
							temp5=authors.get(i).split(":");
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
					bw.write("</table></br></br>");
					
					for(int i=0;i<branches.size();i++)
					{
						count=countResults("git log "+branches.get(i)+" --oneline",f);
						branch_count.add(String.valueOf(count));
					}
					
					re=executeCommand("git shortlog -s -n --all",f);
					re=re.replaceAll(" ","");					
					temp=re.split("\n");
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Contributor : Name</th><th>Total Percentage</th>");
					for(int i=0;i<branches.size();i++)
					{
						bw.write("<th>"+branches.get(i)+"</th>");
					}
					bw.write("</tr>");
					for(int i=0;i<temp.length;i++)
					{
						temp2=temp[i].split("\t");
						bw.write("<tr><td>"+temp2[1]+"</td>");
						users.add(temp2[1]);
						bw.write("<td>");
						String myf = String.format("%.02f", Float.valueOf(temp2[0])*100/com);
						bw.write(myf);
					    bw.write("%</td>");
						for(int j=0;j<branches.size();j++)
						{	
							re2=executeCommand("git log "+branches.get(j),f);
							temp3=re2.split("\n");
							int count2=0;
							int current=0;
							for(int w=0;w<temp3.length;w++)
							{
								if(temp3[w].contains("Author:"))
								{
									count2++;
									if(temp3[w].contains(temp2[1]))	
										current++;
								}
							}
							bw.write("<td>");
							myf = String.format("%.02f", Float.valueOf(current)*100/count2);
							bw.write(myf);
						    bw.write("%</td>");
						}
						bw.write("</tr>");
					}
					bw.write("</table></br>");
					
					String last_day="";
					String first_day="";
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Contributor : Name</th><th>Commit Per Day</th><th>Commit Per Week</th><th>Commit Per Month</th></tr>");
					for(int i=0;i<users.size();i++)	
					{
						re=executeCommand("git log --author="+users.get(i),f);
						dates=new ArrayList<String>();
						temp=re.split("\n");
						int sinolo=0;
						for(int j=0;j<temp.length;j++)
						{
							if(temp[j].contains("Date:"))
							{
								sinolo++;
								if(j==2)
								{
									temp2=temp[j].split(" ");
									last_day=temp2[4]+" "+temp2[5]+", "+temp2[7];
								}
								if(j==temp.length-3 ||j==temp.length-4 || j==temp.length-5 )
								{
									
									temp2=temp[j].split(" ");
									first_day=temp2[4]+" "+temp2[5]+", "+temp2[7];
								}
							}
						}
						
						DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
						Date date = format.parse(last_day);
						Date date2 = format.parse(first_day);	
						long diff = date.getTime() - date2.getTime();
						int days=(int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						if(days==0)
							days=1;
						int weeks=(days/7)+1;
						int months=(days/30)+1;
						float percent=Float.valueOf(sinolo)/days;
						bw.write("<tr><td>"+users.get(i)+"</td>");
						bw.write("<td>"+percent+"</td>");
						percent=Float.valueOf(sinolo)/weeks;
						bw.write("<td>"+percent+"</td>");
						percent=Float.valueOf(sinolo)/months;
						bw.write("<td>"+percent+"</td></tr>");
					}
					
					bw.write("</table></br>");
					int sinolo_add=0;
					int sinolo_rem=0;
					int sinolo_upd=0;
					for(int i=0;i<users.size();i++)
					{
						re=executeCommand("git log --author=\""+users.get(i) +"\" --pretty=tformat: --numstat",f);
						if(re.compareTo("")==0)
							continue;
						re=re.replaceAll("-","0");
						temp=re.split("\n");
						for(int j=0;j<temp.length;j++)
						{
							temp2=temp[j].split("\t");
							
							int add=Integer.valueOf(temp2[0]);
							int remove=Integer.valueOf(temp2[1]);
							sinolo_add+=add;
							sinolo_rem+=remove;
							sinolo_upd+=Math.abs(add-remove);		
						}
					}
					bw.write("<table border=\"1\">");
					bw.write("<tr><th>Added Lines Per Contributor</th><th>Removed Lined  Per Contributor</th><th>Updated Lines  Per Contributor</th></tr>");
					int siz=users.size();
					bw.write("<td>"+sinolo_add/siz+"</td>");
					bw.write("<td>"+sinolo_rem/siz+"</td>");
					bw.write("<td>"+sinolo_upd/siz+"</td>");
					bw.write("</tr>");
					bw.write("</table></br>");
					bw.write("</body>");
					bw.write("</html>");
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
	            System.exit(0);	
	    }
	
	private static Integer countResults(String command,File F)
	{
		String re=executeCommand(command,F);
		String [] mytemp ;
		mytemp=re.split("\n");
		return mytemp.length;
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

