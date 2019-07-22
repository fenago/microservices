package com.worker.service;



import org.springframework.web.client.RestTemplate;


import redis.clients.jedis.Jedis;

public class Worker implements Runnable {
//	private RestTemplate restTemplate;
	Jedis jedis = null;
	private RestTemplate restTemplate;

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

		while (true) {
			try {

				work_loop(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				try {
					Thread.sleep(1000);
					System.out.println("trying again ............. "+e.getMessage()+" "+ e.getCause());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	private void work_loop(int interval) {

		int deadline = 0;
		int loops_done = 0;
		while (true)
		{
			if (System.currentTimeMillis() > deadline) {
				System.out.println("units of work done, updating hash counter ............."+ loops_done);
				jedis.incrBy("hashes", loops_done);
				loops_done = 0;
				deadline = (int) (System.currentTimeMillis() + interval);

			}
			try {
				work_once();
				loops_done += 1;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("trying again "+e.getMessage()+" "+ e.getCause());
			}
			
		}
		
	}

	private void work_once() throws InterruptedException {
		
//		def work_once():
//		    log.debug("Doing one unit of work")
//		    time.sleep(0.1)
//		    random_bytes = get_random_bytes()
//		    hex_hash = hash_bytes(random_bytes)
//		    if not hex_hash.startswith('0'):
//		        log.debug("No coin found")
//		        return
//		    log.info("Coin found: {}...".format(hex_hash[:8]))
//		    created = redis.hset("wallet", hex_hash, random_bytes)
//		    if not created:
//		        log.info("We already had that coin")
//		
		
		
		
		System.out.println("Doing one unit of work");
		Thread.sleep(100);
		
	    String random_bytes = get_random_bytes();
	    System.out.println("rng rec:  "+ random_bytes);
	    String  hex_hash = hash_bytes(random_bytes);
	    System.out.println("hashers rec: "+hex_hash);
	    if(!hex_hash.startsWith("0"))
	    {
	    	
	    	System.out.println("No coin found");
	        return;
	    }
	    	
	        		System.out.println("Coin found: { "+ hex_hash +" }...");
	     long created = jedis.hset("wallet", hex_hash, random_bytes);
	    if (created>0){
	    	
	    	System.out.println("We already had that coin");
	    }
	    	

	}

	String get_random_bytes() {
		String result = restTemplate.getForObject("http://rng:8080/rng/{howmany}", String.class, "32");
		return result;

	}

	String hash_bytes(String random_bytes) {
//	String result=restTemplate.postForObject(
//				  "http://hasher:8080/hasher/{data}",
//				  random_bytes,
//				  String.class);
//		
//		return result;
		String result = restTemplate.getForObject("http://hasher:8080/hasher/{data}", String.class, random_bytes);
		return result;

	}

}
