package com.rnd.serviceD;


import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {
    
   
    @RequestMapping("/rng")
	@ResponseBody
	String home() {
		return "RNG running on rng";
	}
	@RequestMapping("/rng/{howMany}")
	@ResponseBody
	public String getFRandomNumber(
			  @PathVariable("howMany") int howMany) {
		
		SecureRandom rnd = new SecureRandom();
		byte[] token = new byte[howMany];
		rnd.nextBytes(token);
			    return  Base64.getEncoder().encodeToString(token);
			}
	////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    
}
