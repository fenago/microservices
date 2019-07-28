package com.learningvoyage.worker;


public class App 
{
    public static void main( String[] args )
    {
    	Thread worker = new Thread (new Worker());
		worker.start();
    }
}
