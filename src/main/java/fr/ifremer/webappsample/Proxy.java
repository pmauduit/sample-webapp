package fr.ifremer.webappsample;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

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
		
		Assertion assertion = (Assertion) req.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
		
            if(assertion != null && assertion.getPrincipal()!=null) {
                String sUser = assertion.getPrincipal().getName();
            	resp.getWriter().write("proxy: " + sUser);

            } else {
            	resp.getWriter().write("proxy: no name");
            }
	}
}
