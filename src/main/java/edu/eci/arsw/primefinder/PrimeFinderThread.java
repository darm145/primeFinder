package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private  List<Integer> primes;
	private boolean running,paused=false;
	public PrimeFinderThread(int a, int b) {
		super();
                this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		running=true;
		
	}

        @Override
	public void run(){
        	while (a<b) {
        		if (paused) {
        			try {
        				synchronized(this) {
        					wait();
        				}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        		if(isPrime(a)) {
        			primes.add(a);
        			
        		}
        		a++;
        	}
            running=false;
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	public boolean running() {
		return running;
	}
	public void pause() {
		paused=true;
	}
	public void Resume() {
		paused=false;
		synchronized(this) {
			this.notify();
		}
		
	}
	
}
