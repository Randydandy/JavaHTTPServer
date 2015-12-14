package tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import http.Parser;

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
			e.printStackTrace();
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
						Parser.ParseRequest(s);
				}
			}
			catch(Exception ex)
			{
				Close();
			}
		}
	}
}
