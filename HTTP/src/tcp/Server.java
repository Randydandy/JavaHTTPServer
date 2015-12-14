package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import IO.Configuration;

public class Server 
{
	public static String VERSION = "HTTP/1.1";
	
	private ServerSocket m_Socket;
	private Listener m_Listener;
	private boolean m_Running;
	private List<Client> m_aClients;
	private Configuration m_Config;
	
	public Server()
	{
		m_Running = false;
		m_aClients = new ArrayList<Client>();
		m_Config = new Configuration();
	}
	
	public void Start()
	{
		try 
		{
			System.out.println("Loading config..");
			m_Config.Load();
			
			System.out.println("Starting server..");
			m_Socket = new ServerSocket(80);
			m_Running = true;
			m_Listener = new Listener();
			m_Listener.start();
			System.out.println("Now listening on Port " + m_Config.m_BindPort);
		} 
		catch (IOException e) 
		{

			e.printStackTrace();
		}
	}
	
	public boolean IsRunning() { return m_Running; }
	
	class Listener extends Thread
	{
		@Override
		public void run()
		{
			while(m_Running)
			{
				try 
				{
					Socket s = m_Socket.accept();
					Client c = new Client(s, Server.this);
					c.Start();
					m_aClients.add(c);
					Thread.sleep(10);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void Close() 
	{
		try 
		{
			System.out.println("Saving config..");
			m_Config.Save();	
			
			System.out.println("Stopping server..");			
			for(Client c : m_aClients) 
				c.Close();
			
			m_Socket.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
