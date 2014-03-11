import java.io.*;



public class WebServerExample{

	public static void main(String [] args) throws IOException
	{
		
		HttpRequest hr = new HttpRequest();
		Thread thread = new Thread(hr);
		thread.start();
		
	}
  }


