package tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import http.Request;
import http.Request.RequestType;
import http.Response;
import http.Response.StatusCode;

public class Client
{

	private Socket m_Socket;
	private boolean m_Running;
	private Server m_Server;
	private Listener m_Listener;
	private BufferedWriter m_Writer;
	private BufferedReader m_Reader;
	
	
	public Client(Socket s, Server srv)
	{
		m_Socket = s;
		m_Server = srv;
		m_Running = false;
	}
	
	public void Start()
	{
		try
		{
			m_Writer = new BufferedWriter(new OutputStreamWriter(m_Socket.getOutputStream()));
			m_Reader = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
			m_Running = true;
			m_Listener = new Listener();
			m_Listener.start();
		}
		catch(IOException ex)
		{
			Close();
		}
	}
	
	public void Close()
	{
		
	}
	
	public void Send(String s)
	{
		
		try 
		{
			m_Writer.write(s);
			m_Writer.flush();
		} 
		catch (IOException e)
		{
		}
	}
	
	public void SendError(StatusCode sc)
	{
		Response re = new Response(sc);
		re.m_Connection = "Closed";
		re.m_Agent = Server.SERVER_AGENT;
		re.m_ContentType = "text/html";
		re.SetData(m_Server.Config().GetErrorDocument(re.m_StatusCodeNum));
		Send(re.toString());
	}
	
	private void SendDocument(Request r) 
	{
		String file = r.m_Request;
		String request = m_Server.Config().m_RootDirectory + file;
		System.out.println("Request: " + request);
		File f = new File(request);
		Response re = new Response(StatusCode.OK);
		re.m_Connection = "Closed";
		re.m_Agent = Server.SERVER_AGENT;
		re.m_ContentType = "text/html";
		
		if(f.isDirectory())
		{
			// File List or index.html
		}
		else
		{
			String data = m_Server.Config().GetFileContents(request);
			if(!f.exists())
				SendError(StatusCode.NOT_FOUND);
			else if(data == null)
				SendError(StatusCode.BAD_REQUEST);
			else
			{
				// Send File
			}				
		}
	}
	
	class Listener extends Thread
	{
		@Override
		public void run()
		{
			StringBuilder strbuilder = new StringBuilder();
			
			try
			{
				while(m_Running && m_Server.IsRunning())
				{
					char[] aRecv = new char[1024];
					int Bytes = m_Reader.read(aRecv, 0, 1024);					
					if(Bytes > 0) strbuilder.append(aRecv, 0, Bytes);	
					
					String s = new String(strbuilder);
					if(s.endsWith("\r\n"))
					{
						Request r = Request.ParseRequest(s);
						
						if(r == null)
							SendError(StatusCode.BAD_REQUEST);
						else if(!r.m_Version.equalsIgnoreCase(Server.VERSION))
							SendError(StatusCode.HTTP_VERSION_NOT_SUPPORTED);
						else
						{
							System.out.println(r.m_RequestTypeStr + " " + r.m_Host + r.m_Request + " " + r.m_Agent + " " + m_Socket.getRemoteSocketAddress().toString());
							if(r.m_RequestType == RequestType.GET)
								SendDocument(r);
							else						
								SendError(StatusCode.INTERNAL_SERVER_ERROR);
						}
						
						strbuilder = new StringBuilder();
					}
					Thread.sleep(10);
				}
			}
			catch(Exception ex)
			{
				Close();
			}
		}
	}
}
