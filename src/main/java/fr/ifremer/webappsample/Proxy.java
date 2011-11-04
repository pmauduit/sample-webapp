package fr.ifremer.webappsample;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Assertion;

public class Proxy extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		
		//Assertion assertion = (Assertion) req.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
		
		
//            if(assertion != null && assertion.getPrincipal()!=null) {
//                String sUser = assertion.getPrincipal().getName();
//            	resp.getWriter().write("proxy: " + sUser);
//
//            } else {
//            	resp.getWriter().write("proxy: no name");
//            }
		
		String ticket = req.getParameter("ticket");
		System.out.println("[PROXY] proxy ticket received: " + ticket);
		
		System.out.println("[PROXY] Trying to validate it against CAS server ...");
		
		// It is a pity: this should be handled by the CAS client API ...
		
		String baseUrl = "https://localhost:8443/cas/proxyValidate";
		String serviceUrl = "http://localhost:8080/sample-webapp/proxy";
		
		StringBuilder response = new StringBuilder();
		try {
			InputStream in = new URL(baseUrl + "?service="+serviceUrl+"&ticket="+ ticket).openStream();
			byte[] cbuf = new byte[1024];
              int len; // number of bytes read from the stream
              OutputStream out = new ByteArrayOutputStream();
              
              while ((len = in.read(cbuf)) > 0) {
                  out.write(cbuf, 0, len);
              }

			String proxyResponse = out.toString();
			System.out.println("[PROXY] got answer from CAS server: " + proxyResponse);
			
			response.append("proxy response: ").append(proxyResponse).append("\n");
		} catch (Exception e) {
			response.append("proxy response: ").append("failure").append("\n");
			response.append("exception: ").append(e.getMessage()).append("\n");
		}
		resp.getWriter().write(response.toString());
	}
}
