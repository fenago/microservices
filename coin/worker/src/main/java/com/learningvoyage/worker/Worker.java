package com.learningvoyage.worker;



import org.springframework.web.client.RestTemplate;


import redis.clients.jedis.Jedis;

public class Worker implements Runnable {
//	private RestTemplate restTemplate;
	Jedis jedis = null; //jedis is redis client library for java 
	private RestTemplate restTemplate; //it will use to make HTTP call and get result 

	public void run() {

		try {
			Thread.sleep(3000);

			jedis = new Jedis("redis");
			this.restTemplate = new RestTemplate();	
			startWorking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startWorking() {

		while (true) {  //forever mining loop
			try {

				work_loop(1000); //interval of 1000 milisec or 1 sec 
			} catch (Exception e) {


				// TODO Auto-generated catch block
				try {
					Thread.sleep(1000); // restart  the loop after 1sec
					System.out.println("trying again ............. "+e.getMessage()+" "+ e.getCause());
				} catch (InterruptedException  ee) {
					
				}
			}}}




	private void work_loop(int interval) {

		int deadline = 0;
		int loops_done = 0;
		while (true)
		{
			if (System.currentTimeMillis() > deadline) { //check if 1 sec is complete or not 
				System.out.println("units of work done, updating hash counter ............."+ loops_done);
				jedis.incrBy("hashes", loops_done); //increment the hashes loop done in one sec  
				loops_done = 0;
				deadline = (int) (System.currentTimeMillis() + interval);

			}
			try {
				work_once(); //coin mining function calling
				loops_done += 1;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("trying again "+e.getMessage()+" "+ e.getCause());
			}
			
		}
		
	}

	private void work_once() throws InterruptedException {
		
        System.out.println("Doing one unit of work");
		Thread.sleep(100);
		
	    String random_bytes = get_random_bytes(); //get random numbers from rng microservice running in docker
	    System.out.println("rng rec:  "+ random_bytes);
	    String  hex_hash = hash_bytes(random_bytes);//get hash numbers from hasher microservice running in docker
	    System.out.println("hashers rec: "+hex_hash);
	    if(!hex_hash.startsWith("0")) //coin must start with zero 
	    {
	    	System.out.println("No coin found");
	        return;
	    }
	    	
	        		System.out.println("Coin found: { "+ hex_hash +" }...");
	     long created = jedis.hset("wallet", hex_hash, random_bytes);
	    if (created>0){ //redis status if aready exist it will not added
	    	
	    	System.out.println("We already had that coin");
	    }
	    	

	}

	String get_random_bytes() {
		String url= "http://rng:8080/rng/{howmany}";
		// if you are running worker class outside docker then use following url
		//  String url= "http://localhost:8080/rng/{howmany}";
		String result = restTemplate.getForObject(url, String.class, "32");
		return result;

	}

	String hash_bytes(String random_bytes) {
		String url= "http://hasher:8080/hasher/{data}";
		// if you are running worker class outside docker then use following url
		//  String url= "http://localhost:8080/hasher/{data}";
		
		String result = restTemplate.getForObject(url, String.class, random_bytes);
		return result;

	}

}
