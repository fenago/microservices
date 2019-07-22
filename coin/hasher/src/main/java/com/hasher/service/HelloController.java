package com.hasher.service;


import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {
    
	// hasher........................//////////////////////////////////////////////////////////////
				
	            @RequestMapping("/hasher")
				@ResponseBody
				String hellohasher() {
					return "hasher running on hasher";
				}
				@RequestMapping("/hasher/{data}")
				@ResponseBody
				
				public String gethash(
						  @PathVariable("data") String data) {
					MessageDigest digest;
					try {
						digest = MessageDigest.getInstance("SHA-256");
						byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
						  return  Base64.getEncoder().encodeToString(hash);
						
					  
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "error............";
					
					
						}

	////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    
}
