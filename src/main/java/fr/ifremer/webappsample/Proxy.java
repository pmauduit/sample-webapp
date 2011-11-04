package fr.ifremer.webappsample;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;




public class Proxy extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		

		String ticket = req.getParameter("ticket");
		System.out.println("[PROXY] proxy ticket received: " + ticket);
		
		System.out.println("[PROXY] Trying to validate it against CAS server ...");
		
		// following code is a pity: this should be handled by the CAS client API ...
		// not homebrew piece of code like this
		
		// Anyway, it is the way I already implemented it onto GeoNetwork
		
		String baseUrl = "https://localhost:8443/cas/proxyValidate";
		String serviceUrl = "http://localhost:8080/sample-webapp/proxy";
		
		StringBuilder response = new StringBuilder();
		
		try {
			URL casProxyValidateUrl = new URL(baseUrl + "?service="+serviceUrl+"&ticket="+ ticket);

			Document doc= new SAXBuilder().build(casProxyValidateUrl);
			String proxyResponse = new XMLOutputter().outputString(doc);
			
			System.out.println("[PROXY] got answer from CAS server: " + proxyResponse);
			
			String casUser = "nonamed";
			
			Namespace casNs = Namespace.getNamespace("cas", "http://www.yale.edu/tp/cas");
			
			XPath xpSuccess = XPath.newInstance("//cas:user");
			xpSuccess.addNamespace(casNs);
			casUser = (String) ((Element) xpSuccess.selectSingleNode(doc)).getText();

			System.out.println("[PROXY] parsed user: " + casUser);
			
			
			response.append("proxy acting as user: " + casUser).append("\n");
			
		} catch (Exception e) {
			response.append("exception: ").append(e.getMessage()).append("\n");
			System.out.println("[PROXY] Exception caught: " + e.getStackTrace());
		}
		resp.getWriter().write(response.toString());
	}
}
