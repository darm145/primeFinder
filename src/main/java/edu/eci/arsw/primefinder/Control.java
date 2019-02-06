/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;
    private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
    	boolean running=true;
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        
        do {
        	running=false;
        	try {
				Thread.sleep(TMILISECONDS);
				pause();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i=0;i<NTHREADS;i++) {
				System.out.println("el hilo "+i+" ha encontrado hasta el momento "+pft[i].getPrimes().size()+" primos");
				running=running || pft[i].running();
			}
			try {
				br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			continuar();
			
			
        }while(running);
        for(int i=0;i<NTHREADS;i++) {
			System.out.println("en total el hilo "+i+" encontro "+pft[i].getPrimes().size()+" primos");
			running=running || pft[i].running();
		}
    }
    
    
    private void continuar() {
    	for(PrimeFinderThread e:pft) {
    		e.Resume();
    	}
		
	}

	public void play() {
    	pft.notifyAll();
    }
    public void pause() {
    	for(PrimeFinderThread e:pft) {
    		e.pause();
    	}
    }    
}
