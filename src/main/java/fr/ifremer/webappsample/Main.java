package fr.ifremer.webappsample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

public class Main extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8359715423257610348L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		StringBuilder response = new StringBuilder();
		response.append("request remote user: ").append(req.getRemoteUser()).append("\n");

		Assertion assertion = AssertionHolder.getAssertion();
		response.append("assertion principal name: ").append(assertion.getPrincipal().getName()).append("\n");

		String baseURL = "http://localhost:8080/sample-webapp/proxy";

		printAtts("Assertion Attributes", assertion.getAttributes(),response);
		printAtts("Principal Attributes", assertion.getPrincipal().getAttributes(),response);
		String myproxyticket = assertion.getPrincipal().getProxyTicketFor(baseURL);
		response.append("proxyticket: ").append(myproxyticket).append("\n");
		
		try {
			InputStream in = new URL(baseURL+"?ticket="+ myproxyticket).openStream();
			String proxyResponse = new BufferedReader(new InputStreamReader(in)).readLine();
			response.append("proxy response: ").append(proxyResponse).append("\n");
		} catch (Exception e) {
			response.append("proxy response: ").append("failure").append("\n");
			response.append("exception: ").append(e.getMessage()).append("\n");
		}
		resp.getWriter().write(response.toString());
	}

	private void printAtts(String string, Map attributes, StringBuilder response) {
		response.append(string).append("\n");
		Set<Map.Entry<Object, Object>> entries = attributes.entrySet();
		for (Map.Entry<Object, Object> entry : entries) {
			response.append("\t").append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
		}
	}
}
