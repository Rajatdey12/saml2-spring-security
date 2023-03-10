package com.rajat.security.sp.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SamlAuthentication;
import org.springframework.security.saml.SamlTransformer;
import org.springframework.security.saml.saml2.attribute.Attribute;
import org.springframework.security.saml.saml2.authentication.Assertion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class ServiceProviderController {

	private static final Log logger =LogFactory.getLog(ServiceProviderController.class);
	private SamlTransformer transformer;

	@Autowired
	public ServiceProviderController setTransformer(SamlTransformer transformer) {
		this.transformer = transformer;
		return this;
	}

	@RequestMapping(value = {"/", "/index", "/logged-in"})
	public String home(Model model) {
		logger.info("Sample SP Application - You are logged in!");
		populateModel(model);
//		System.out.println(model);
		return "logged-in";
	}

	private void populateModel(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Attribute> attributes = emptyList();
		String xml = null;
		if (authentication instanceof SamlAuthentication) {
			SamlAuthentication sa = (SamlAuthentication)authentication;
			attributes = sa.getAssertion().getAttributes();
			xml = transformer.toXml(sa.getAssertion());

		}
		model.addAttribute("attributes", attributes);
		model.addAttribute("xml", prettyPrint(xml));
	}


	private String prettyPrint(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (Exception x) {
			return xml;
		}
	}

}
