package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import http.Response;
import main.Parser;
import tcp.Server;

public class Configuration 
{
	public static String STANDARD_ERROR_DOCUMENT =
		"<html>" +
		"<body bgcolor=\"white\">" +
		"<center><h1>%d %s</h1></center><hr><center>" + Server.SERVER_AGENT + "</center>" +
		"</body>" +
		"</html>";
	
	
	public String m_ConfigFile;
	public String m_RootDirectory;
	public int m_BindPort;
	public HashMap<Integer, String> m_aErrorDocuments;
		
	public Configuration()
	{
		m_aErrorDocuments = new HashMap<Integer, String>();
		LoadDefaults();
	}
	
	public Configuration(String conf)
	{
		m_ConfigFile = conf;
	}
	
	private void LoadDefaults()
	{
		m_ConfigFile = "http.conf";
		m_RootDirectory = System.getProperty("user.dir");
		m_BindPort = 80;
	}
	
	public boolean Save()
	{
		try 
		{
			File f = new File(m_ConfigFile);
			PrintWriter pw = new PrintWriter(new FileWriter(f));		
			pw.println("rootdir " + m_RootDirectory);	
			pw.println("bindport " + m_BindPort);	
			
			for(int key : m_aErrorDocuments.keySet())
				pw.println("ErrorDocument " + key + m_aErrorDocuments.get(key));
			
			pw.flush();
			pw.close();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean Load()
	{
		File f = new File(m_ConfigFile);
		if(!f.exists() || !f.canRead())
			return false;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			System.out.println("File Success");
			
			String line;
			while((line = br.readLine()) != null)
				ParseLine(line);
			
			br.close();
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public String GetFileContents(String path)
	{
		File f = new File(path);
		if(!f.exists() || !f.canRead())
			return null;
			
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String text = "";
			String line;
			while((line = br.readLine()) != null)
				text += line + "\n";
			
			br.close();
			return text;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void ParseLine(String line)
	{
		line = line.trim();
		
		if(!line.contains(" "))
			return;
		
		String key = line.substring(0, line.indexOf(' '));
		String val = line.substring(line.indexOf(' ') + 1);
		
		if(key.equalsIgnoreCase("rootdir"))
			m_RootDirectory = val;
		else if(key.equalsIgnoreCase("bindport"))
		{
			int i = Parser.ParseInt(val);
			if(i > 0) m_BindPort = i;
		}
		else if(key.equalsIgnoreCase("ErrorDocument"))
		{
			System.out.println(key +"=" + val);
			
			String[] aSplit = val.split(" ");
			if(aSplit.length >= 2)
			{
				int i = Parser.ParseInt(aSplit[0]);
				if(i < 0) return;
				String path = val.substring(aSplit[0].length() + 1);
				m_aErrorDocuments.put(i, path);
			}
		}
	}

	public String GetErrorDocument(int i)
	{
		String s = STANDARD_ERROR_DOCUMENT;
		String Error = "Unknown Error";
		
		if(m_aErrorDocuments.containsKey(i))
		{
			String temp = GetFileContents(m_aErrorDocuments.get(i));
			if (temp != null) s = temp;
		}
		
		for(int d = 0; d < Response.aCodes.length; d++)
		{
			if(Response.aCodes[d] == i)
			{
				Error = Response.aText[d]; 
				break;
			}
		}
		
		s = s.replaceAll("%d", "" + i);
		s = s.replaceAll("%s", Error);	
					
		return s;
	}
}
