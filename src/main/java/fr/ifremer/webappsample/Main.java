package fr.ifremer.webappsample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Assertion;

import sun.net.www.content.text.PlainTextInputStream;

public class Main extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		Assertion assertion = (Assertion) req.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
		
			String sUser = req.getRemoteUser();
			String myproxyticket = assertion.getPrincipal().getProxyTicketFor("http://localhost:8082/sample-webapp/proxy");
			System.out.println(myproxyticket);

			URLConnection u = new URL("http://localhost:8082/sample-webapp/proxy?ticket=" + myproxyticket).openConnection();
			u.connect();
			String ret2 = "";
			HttpURLConnection c = null;
			
			try {
				InputStream ret = (InputStream) u.getInputStream();
				ret2 =  new BufferedReader(new InputStreamReader(ret)).readLine();
				c = (HttpURLConnection) u;
				resp.setContentType("text/plain");
			} catch (Exception e) {
				e.printStackTrace();
			}

//            if(assertion != null && assertion.getPrincipal()!=null) {
//                sUser = assertion.getPrincipal().getName();
//            	
//            } else {
//            	sUser = "not logged in";
//            }

		resp.getWriter().write(((c != null) ? c.getResponseCode() : 500)  +  " Hellow: " + ret2 + "\nsUser = " + sUser);
		
	}
}
