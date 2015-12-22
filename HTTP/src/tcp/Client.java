package tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
			String data = "<html></body>";
			data += "<h1>Index of " + file + "</h1>";
			data += "<table><tbody>";
			data += "<tr><th valign=\"top\"><img src=\"/icons/blank.gif\" alt=\"[ICO]\"></th><th><a href=\"?C=N;O=D\">Name</a></th><th><a href=\"?C=M;O=A\">Last modified</a></th><th><a href=\"?C=S;O=A\">Size</a></th><th><a href=\"?C=D;O=A\">Description</a></th></tr>";
			data += "<tr><th colspan=\"5\"><hr></th></tr>";
			
			if(!(new File(m_Server.Config().m_RootDirectory)).equals(f))
			{
				String url = file;
				if(!url.endsWith("\\") && !url.endsWith("/")) url += "/";
				url += "..";
				data += "<tr><td valign=\"top\"><img src=\"/icons/back.gif\" alt=\"[PARENTDIR]\"></td><td><a href=\"" + url + "\">Parent Directory</a>       </td><td>&nbsp;</td><td align=\"right\">  - </td><td>&nbsp;</td></tr>";	
			}
						
			List<File> aDirs = new ArrayList<File>();
			List<File> aFiles = new ArrayList<File>();
			
			for (File a : f.listFiles()) 
			{
				if (a.isDirectory()) aDirs.add(a);
				else aFiles.add(a);
			}
			
			for(File a : aDirs)
			{
				String url = file;
				if(!url.endsWith("\\") && !url.endsWith("/")) url += "/";
				url += a.getName();
				data += "<tr><td valign=\"top\"><img src=\"/icons/folder.gif\" alt=\"[DIR]\"></td><td><a href=\"" + url + "\">" + a.getName() + "</a>                  </td><td align=\"right\"></td><td align=\"right\">  - </td><td>&nbsp;</td></tr>";
			}
			
			for(File a : aFiles)
			{
				String url = file;
				if(!url.endsWith("\\") && !url.endsWith("/")) url += "/";
				url += a.getName();
				data += "<tr><td valign=\"top\"><img src=\"/icons/binary.gif\" alt=\"[   ]\"></td><td><a href=\"" + url + "\">" + a.getName() + "</a>             </td><td align=\"right\"></td><td align=\"right\"></td><td>&nbsp;</td></tr>";
			}
			
			data += "<tr><th colspan=\"5\"><hr></th></tr>";
			data += "</tbody></table>";
			data += "<address>" + Server.SERVER_AGENT + "</address>";
			data += "</body></html>";
			re.SetData(data);
			Send(re.toString());
		}
		else
		{
			String data = m_Server.Config().GetFileContents(request);
			if(!f.exists())
				SendError(StatusCode.NOT_FOUND);
			else if(data == null)
				SendError(StatusCode.INTERNAL_SERVER_ERROR);
			else
			{
				re.m_ContentDisposition = "attachment; filename=\"" + f.getName() + "\"";
				re.SetData(m_Server.Config().GetFileContents(request));
				Send(re.toString());
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
