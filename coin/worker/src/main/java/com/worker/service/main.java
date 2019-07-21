package com.worker.service;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread worker = new Thread (new Worker());
		worker.start();
	}

}
