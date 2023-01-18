package com.rajat.security.idp.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IdentityProviderController {
	private static final Log logger =LogFactory.getLog(IdentityProviderController.class);

	@RequestMapping(value = {"/"})
	public String selectProvider() {
		logger.info("Sample IDP Application - Select an SP to log into!");
		return "redirect:/saml/idp/select";
	}


}
