package com.worker.service;



import org.springframework.web.client.RestTemplate;


import redis.clients.jedis.Jedis;

public class Worker implements Runnable {
//	private RestTemplate restTemplate;
	Jedis jedis = null;
	private RestTemplate restTemplate;

	public void run() {

		try {
			Thread.sleep(10000);

			jedis = new Jedis("localhost");
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

				work_loop(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				try {
					Thread.sleep(1000);
					System.out.println("trying again ............. "+e.getMessage());
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
		if (System.currentTimeMillis() / 1000 > deadline) {
			System.out.println("updating hash counter .............");
			jedis.incrBy("hashes", loops_done);
			loops_done = 0;
			deadline = (int) (System.currentTimeMillis() / 1000 + interval);

		}
		try {
			work_once();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("trying again ");
		}
		loops_done += 1;
	}

	private void work_once() throws InterruptedException {
		System.out.println("Doing one unit of work");
		Thread.sleep(100);
		
	    String random_bytes = get_random_bytes();
	    String  hex_hash = hash_bytes(random_bytes);
	    System.out.println(hex_hash);
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
		String result = restTemplate.getForObject("http://localhost:8001/rng/{howmany}", String.class, "32");
		return result;

	}

	String hash_bytes(String random_bytes) {
		String result = restTemplate.getForObject("http://localhost:8002/hasher/{data}", String.class, random_bytes);
		return result;

	}

}
