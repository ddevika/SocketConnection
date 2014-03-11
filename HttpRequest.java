import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpRequest implements Runnable{
	
	//port that it is listening to
	int port = 8000;
	ServerSocket serverSocket = null;

	public void run()
	{	
			// TODO Auto-generated method stub
			System.out.println("Server is listening on port" + port);
			try{

				serverSocket = new ServerSocket(port);
				System.out.println("Listening to port " + port);
				System.out.println("Server Socket is " + serverSocket);
			
				//Socket connectionSocket = new Socket(host , port);
				Socket connectionSocket = null;
				
				while(true)
				{
				serverSocket.getLocalPort();
				
				//try to accept the client connection
				connectionSocket = serverSocket.accept();

				InetAddress client = connectionSocket.getInetAddress();
				System.out.println(client.getHostName() + " "+client  +  " is connected to server");


				BufferedReader input =
		            new BufferedReader(new InputStreamReader(connectionSocket.
		           getInputStream()));


				DataOutputStream output =
		            new DataOutputStream(connectionSocket.getOutputStream());
				
				//process the connection's request
				handleConnection(input, output);
				//connectionSocket.close();
				}
		} 
		catch (Exception e) {
		System.out.println(e);
		}
	}
	
	//ProcessRequest as in the problem example
	private void handleConnection(BufferedReader input, DataOutputStream output)
	{
    String path = new String(); 
    String file = new String(); 
    String request = new String();
    String response = new String();
    Scanner sc = new Scanner(System.in);

    try {
      //type of request:
      //GET /index.html HTTP/1.0
      //output.writeBytes("response message: ");
	
      System.out.println("\n please enter the request (format is GET /index.html HTTP/1.0) :");   
      request = sc.nextLine();
      ArrayList<String> res = new ArrayList<String>();
 

      if (request.startsWith("GET")) 
      {
       
       try
       {
    	   //get the html string and concatenate with the current path
        String pattern1 = "GET /";
        String pattern2 = " HTTP";
        String regexString = Pattern.quote(pattern1)+ "(.*?)" +Pattern.quote(pattern2);
        Pattern p = Pattern.compile(regexString);
   		Matcher matcher1 = p.matcher(request);
   		
   		while (matcher1.find())
   		{
   		file = matcher1.group(1);
   		}
   		path = file;
       }
       catch (Exception e)
       {
    	  System.out.println("Fail to parse the request");
       }
       
       System.out.println("\nClient requested :" + new File(path).getAbsolutePath() + "\n");     
       
       //try to get the content of the file
       try {
      	 File f = new File(path);
      	 if (f.isFile())
      	 {
      		 //System.out.println("file is found, opening file");
      		 res = constructResponseMessage(file);
      		 
      		 //print out header of response message
      		 //output.writeBytes("response message: ");
      		 //output.writeBytes("\r\n");
      		 
      		 for (String s: res)
      		 {
      		  System.out.println(s);
      		  //output.writeBytes(s);
      		  //output.writeBytes("\r\n");
      		 }
      		 //open the file
      		 BufferedReader br = new BufferedReader( new FileReader(path));
      		 String line = null;
      		 while ((line = br.readLine()) != null)
      		 {
      			 //System.out.println(line);
      			 output.writeBytes(line);
      			 output.writeBytes("\r\n"); 
      		 }
      		 br.close();
      	 }

          //if the file can't be opened send a 404
      	else 
      	{
          //System.out.println("404 File is not found");
      		response = "<!DOCTYPE html> " +
      				"<html> " +
      				"<body> " +
      				"<h1>404: File Not Found</h1>"
      		+"</body>"
      		+"</html>" + "\r\n";
            output.writeBytes(response);
            output.writeBytes("\r\n");
      	} //close the stream
          //output.close();
          //System.exit(1);
        }
        catch (Exception e2) {};
   
      }

      //request does not contain "GET"
      else if (!request.startsWith("GET"))
      {
    	 
    	System.out.println("501 Bad Request");
        	response = "<!DOCTYPE html> " +
      				"<html> " +
      				"<body> " +
      				"<h1>501: Bad Request</h1>"
      		+"</body>"
      		+"</html>" + "\r\n";
          output.writeBytes(response);
          output.writeBytes("\r\n");
        }

      


    }
    catch (Exception ie) {
    	System.out.println("I/O Error Request" + ie);
    } //catch block

      
    try 
    {
     //close open handles
      output.close();
      //sc.close();
      //System.exit(1);
    }
    catch (Exception e) {
    }
	}
	
	private ArrayList<String> constructResponseMessage(String file)
	{
	  	//get the HTTP string
		String header = "HTTP/1.0 200 OK";
		String connection = "Connection: Close";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String Date = "Date: " + dateFormat.format(date);
		
		String content = "content-type :"+ file;
		
		ArrayList<String> response = new ArrayList<String>();
		
		response.add(header);
		response.add(connection);
		response.add(Date);
		response.add(content);
		
		return response;
   		
		
	}

}
