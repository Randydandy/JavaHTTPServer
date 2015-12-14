package http;

import main.Parser;

public class Header
{
	enum HeaderType
	{
		REQUEST,
		RESPONSE
	}
	
	public HeaderType m_HeaderType;
	
	// First Line
	public String m_Version;
	
	
	// Fields
	public String m_Connection;
	public String m_CacheControl;
	public int m_ContentLength;
	public String m_ContentMD5;
	public String m_ContentType;
	public String m_Date;
	public String m_Pragma;
	public String m_TransferEncoding;
	public String m_Via;
	public String m_Warning;
	public String m_Agent;
	
	// Data
	public String m_Data;
	
	public Header(HeaderType ht)
	{
		m_HeaderType = ht;
	}
	
	public void ReadLine(String key, String val)
	{
		if(key.equalsIgnoreCase("Connection:"))
			m_Connection = val;
		else if(key.equalsIgnoreCase("Cache-Control:"))
			m_CacheControl = val;
		else if(key.equalsIgnoreCase("Content-Length:"))
		{
			int i = Parser.ParseInt(val);
			if(i >= 0) m_ContentLength = i;
		}
		else if(key.equalsIgnoreCase("Content-MD5:"))
			m_ContentMD5 = val;
		else if(key.equalsIgnoreCase("Content-Type:"))
			m_ContentType = val;
		else if(key.equalsIgnoreCase("Date:"))
			m_Date = val;
		else if(key.equalsIgnoreCase("Pragma:"))
			m_Pragma = val;
		else if(key.equalsIgnoreCase("Transfer-Encoding:"))
			m_TransferEncoding = val;
		else if(key.equalsIgnoreCase("Via:"))
			m_Via = val;
		else if(key.equalsIgnoreCase("Warning:"))
			m_Warning = val;
		else if(key.equalsIgnoreCase("User-Agent:"))
			m_Agent = val;
	}
	
	public void SetData(String data)
	{
		m_Data = data;
		m_ContentLength = data.length();
	}
}
