/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Control extends Thread implements KeyListener{
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

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
			for(int i=0;i<NTHREADS;i++) {
				System.out.println("el hilo "+i+" ha encontrado hasta el momento "+pft[i].getPrimes().size()+" primos");
				synchronized (pft[i]) {
					try {
						pft[i].wait();
						running=running || pft[i].running();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
        }while(running);
    }
    
    
    public void play() {
    	pft.notifyAll();
    }

	@Override
	public void keyPressed(KeyEvent e) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.equals(KeyEvent.VK_ENTER)) {
			this.play();	
		}
		
	}
    
}
