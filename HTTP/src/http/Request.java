package http;

import main.Parser;

public class Request extends Header
{
	public enum RequestType
	{
		GET,
		POST,
		HEAD,
		PUT,
		DELETE,
		OPTIONS,
		TRACE,
		CONNECT,
	}
	
	public static String[] aRequestTypes = { "GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE", "CONNECT" };
	public RequestType m_RequestType;	
	
	// First Line
	public String m_RequestTypeStr;
	public String m_Request;
	
	// Fields
	public String m_Host;
	public String m_Accept;
	public String m_AcceptCharset;
	public String m_AcceptEncoding;
	public String m_AcceptLanguage;
	public String m_Authorization;
	public String m_Cookie;
	public String m_Expect;
	public String m_From;
	public String m_IfMatch;
	public String m_IfModifiedSince;
	public String m_IfNoneMatch;
	public String m_IfRange;
	public String m_IfUnmodifiedSince;
	public int m_MaxForwards;
	public String m_ProxyAuthorization;
	public String m_Range;
	public String m_Referer;
	public String m_TE;
	public String m_Upgrade;
	
	public Request(RequestType rt, String request, String version)
	{
		super(HeaderType.REQUEST);
		m_RequestType = rt;
		m_Request = request;
		m_Version = version;
	}
	
	public static RequestType GetRequestType(String s)
	{
		for(int i = 0; i < aRequestTypes.length; i++)
		{
			if(aRequestTypes[i].equalsIgnoreCase(s))
				return RequestType.values()[i];
		}
		
		return null;
	}
	
	public static Request ParseRequest(String s)
	{
		Request r = null;		
		String[] aParams = null;
		String[] aSplit = s.split("\r\n");
		if(aSplit.length < 1) return null;
		
		String FirstLine = aSplit[0];
		aParams = FirstLine.split(" ");
		if(aParams.length < 3) return null;
		
		RequestType type = Request.GetRequestType(aParams[0]);
		if(type == null) return null;
		
		r = new Request(type, aParams[1], aParams[2]);
		r.m_RequestTypeStr = aParams[0];
		
		for(String split : aSplit)
		{
			if(!split.contains(": "))
				continue;
			
			String key = split.substring(0, split.indexOf(':') + 1);
			String val = split.substring(split.indexOf(':') + 2);
			r.ReadLine(key, val);
		}
		
		return r;
	}
	
	public void ReadLine(String key, String val)
	{
		if(key.equalsIgnoreCase("Host:"))
			m_Host = val;
		else if(key.equalsIgnoreCase("Accept:"))
			m_Accept = val;
		else if(key.equalsIgnoreCase("Accept-Charset:"))
			m_AcceptCharset = val;
		else if(key.equalsIgnoreCase("Accept-Encoding:"))
			m_AcceptEncoding = val;
		else if(key.equalsIgnoreCase("Accept-Language:"))
			m_AcceptLanguage = val;
		else if(key.equalsIgnoreCase("Authorization:"))
			m_Authorization = val;
		else if(key.equalsIgnoreCase("Cookie:"))
			m_Cookie = val;
		else if(key.equalsIgnoreCase("Expect:"))
			m_Expect = val;
		else if(key.equalsIgnoreCase("From:"))
			m_From = val;
		else if(key.equalsIgnoreCase("If-Match:"))
			m_IfMatch = val;
		else if(key.equalsIgnoreCase("If-Modified-Since:"))
			m_IfModifiedSince = val;
		else if(key.equalsIgnoreCase("If-None-Match:"))
			m_IfNoneMatch = val;
		else if(key.equalsIgnoreCase("If-Range:"))
			m_IfRange = val;
		else if(key.equalsIgnoreCase("If-Unmodified-Since:"))
			m_IfUnmodifiedSince = val;
		else if(key.equalsIgnoreCase("Max-Forwards:"))
		{
			int i = Parser.ParseInt(val);
			if(i >= 0) m_MaxForwards = i;
		}
		else if(key.equalsIgnoreCase("Proxy-Authorization:"))
			m_ProxyAuthorization = val;
		else if(key.equalsIgnoreCase("Range:"))
			m_Range = val;
		else if(key.equalsIgnoreCase("Referer:"))
			m_Referer = val;
		else if(key.equalsIgnoreCase("TE:"))
			m_TE = val;
		else if(key.equalsIgnoreCase("Upgrade:"))
			m_Upgrade = val;
		else
			super.ReadLine(key, val);
	}
}
