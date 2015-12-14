package http;

import tcp.Server;

public class Response extends Header
{
	
	public enum StatusCode
	{
		// 1xx
		CONTINUE,
		SWITCHING_PROTOCOLS,
		
		// 2xx
		OK,
		CREATED,
		ACCEPTED,
		NONAUTHORITATIVE_INFORMATION,
		NO_CONTENT,
		RESET_CONTENT,
		PARTIAL_CONTENT,
		
		// 3xx
		MULTIPLE_CHOICES,
		MOVED_PERMANENTLY,
		FOUND,
		SEE_OTHER,
		NOT_MODIFIED,
		USE_PROXY,
		TEMPORARY_REDIRECT,
		
		// 4xx
		BAD_REQUEST,
		UNAUTHORIZED,
		PAYMENT_REQUIRED,
		FORBIDDEN,
		NOT_FOUND,
		METHOD_NOT_ALLOWED,
		NOT_ACCEPTABLE,
		PROXY_AUTHENTICATION_REQUIRED,
		REQUEST_TIMEOUT,
		CONFLICT,
		GONE,
		LENGTH_REQUIRED,
		PRECONDITION_FAILED,
		REQUEST_ENTITY_TOO_LARGE,
		REQUESTURI_TOO_LARGE,
		UNSUPPORTED_MEDIA_TYPE,
		REQUESTED_RANGE_NOT_SATISFIABLE,
		EXPECTATION_FAILED,
		
		// 5xx
		INTERNAL_SERVER_ERROR,
		NOT_IMPLEMENTED,
		BAD_GATEWAY,
		SERVICE_UNAVAILABLE,
		GATEWAY_TIMEOUT,
		HTTP_VERSION_NOT_SUPPORTED
	}

	private static int aCodes[] = { 100, 101, 
			 200, 201, 202, 203, 204, 205, 206,
			 300, 301, 302, 303, 304, 305, 307,
			 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417,
			 500, 501, 502, 503, 504, 505 };

	private static String aText[] = { "Continue", "Switching Protocols",
			   "OK", "Created", "Accepted", "Non-Authoritative Information", "No Content", "Reset Content", "Partial Content",
			   "Multiple Choices", "Moved Permanently", "Found", "See Other", "Not Modified", "Use Proxy", "Temporary Redirect",
			   "Bad Request", "Unauthorized", "Payment Required", "Forbidden", "Not Found", "Method Not Allowed", "Not Acceptable", "Proxy Authentication Required", "Request Time-out", "Conflict", "Gone", "Length Required", "Precondition Failed", "Request Entity Too Large", "Request-URI Too Large", "Unsupported Media Type", "Requested range not satisfiable", "Expectation Failed",
			   "Internal Server Error", "Not Implemented", "Bad Gateway", "Service Unavailable", "Gateway Time-out", "HTTP Version not supported" };
	
	public StatusCode m_StatusCode;
	
	// First Line
	public int m_StatusCodeNum;
	public String m_StatusLine;
	
	// Fields
	public String m_AcceptRanges;
	public int m_Age;
	public String m_Allow;
	public String m_ContentEncoding;
	public String m_ContentLanguage;
	public String m_ContentLocation;
	public String m_ContentDisposition;
	public String m_ContentRange;
	public String m_ContentSecurityPolicy;
	public String m_ETag;
	public String m_Expires;
	public String m_LastModified;
	public String m_Link;
	public String m_Location;
	public String m_P3P;
	public String m_ProxyAuthenticate;
	public int m_Refresh;
	public int m_RetryAfter;
	public String m_SetCookie;
	public String m_Trailer;
	public String m_Vary;
	public String m_WWWAuthenticate;
	
	public Response(StatusCode c)
	{
		super(HeaderType.RESPONSE);
		SetStatusCode(c);
		
		m_Age = -1;
		m_Refresh = -1;
		m_RetryAfter = -1;
		m_ContentLength = -1;
		m_Version = Server.VERSION;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		
		s += m_Version + " " + m_StatusCodeNum + " " + m_StatusLine + "\r\n";
		if(m_AcceptRanges != null) s+= "Accept-Ranges: " + m_AcceptRanges + "\r\n";
		if(m_Age >= 0) s+= "Age: " + m_Age + "\r\n";
		if(m_Allow != null) s+= "Allow: " + m_Allow + "\r\n";
		if(m_ContentEncoding != null) s+= "Content-Encoding: " + m_ContentEncoding + "\r\n";
		if(m_ContentLanguage != null) s+= "Content-Language: " + m_ContentLanguage + "\r\n";
		if(m_ContentLocation != null) s+= "Content-Location: " + m_ContentLocation + "\r\n";
		if(m_ContentDisposition != null) s+= "Content-Disposition: " + m_ContentDisposition + "\r\n";
		if(m_ContentRange != null) s+= "Content-Range: " + m_ContentRange + "\r\n";
		if(m_ContentSecurityPolicy != null) s+= "Content-Security-Policy: " + m_ContentSecurityPolicy + "\r\n";
		if(m_ETag != null) s+= "ETag: " + m_ETag + "\r\n";
		if(m_Expires != null) s+= "Expires: " + m_Expires + "\r\n";
		if(m_LastModified != null) s+= "Last-Modified: " + m_LastModified + "\r\n";
		if(m_Link != null) s+= "Link: " + m_Link + "\r\n";
		if(m_Location != null) s+= "Location: " + m_Location + "\r\n";
		if(m_P3P != null) s+= "P3P: " + m_P3P + "\r\n";
		if(m_ProxyAuthenticate != null) s+= "ProxyAuthenticate: " + m_ProxyAuthenticate + "\r\n";
		if(m_Refresh >= 0) s+= "Refresh: " + m_Refresh + "\r\n";
		if(m_RetryAfter >= 0) s+= "Retry-After: " + m_RetryAfter + "\r\n";
		if(m_SetCookie != null) s+= "Set-Cookie: " + m_SetCookie + "\r\n";
		if(m_Trailer != null) s+= "Trailer: " + m_Trailer + "\r\n";
		if(m_Vary != null) s+= "Vary: " + m_Vary + "\r\n";
		if(m_WWWAuthenticate != null) s+= "WWW-Authenticate: " + m_WWWAuthenticate + "\r\n";
		if(m_Connection != null) s+= "Connection: " + m_Connection + "\r\n";
		if(m_CacheControl != null) s+= "Cache-Control: " + m_CacheControl + "\r\n";
		if(m_ContentLength >= 0) s+= "Content-Length: " + m_ContentLength + "\r\n";
		if(m_ContentMD5 != null) s+= "Content-MD5: " + m_ContentMD5 + "\r\n";
		if(m_ContentType != null) s+= "Content-Type: " + m_ContentType + "\r\n";
		if(m_Date != null) s+= "Date: " + m_Date + "\r\n";
		if(m_Pragma != null) s+= "Pragma: " + m_Pragma + "\r\n";	
		if(m_TransferEncoding != null) s+= "Transfer-Encoding: " + m_TransferEncoding + "\r\n";
		if(m_Via != null) s+= "Via: " + m_Via + "\r\n";
		if(m_Warning != null) s+= "Warning: " + m_Warning + "\r\n";
		if(m_Agent != null) s+= "Server: " + m_Agent + "\r\n";
		
		s += "\r\n";
		
		if(m_Data != null) s+= m_Data;
		
		return s;
	}
	
	public void SetStatusCode(StatusCode c)
	{
		int num = c.ordinal();
		
		if(num < 0 || num >= aCodes.length)
			return;
		
		m_StatusCodeNum = aCodes[num];
		m_StatusLine = aText[num];
	}
}
