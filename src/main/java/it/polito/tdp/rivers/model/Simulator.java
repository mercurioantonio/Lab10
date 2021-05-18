package it.polito.tdp.rivers.model;

import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.model.Event.EventType;

public class Simulator {
	
	private Model model;
	
	public Simulator() {
		this.model = new Model();
	}
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//parametri di input
	private float k;
	private float fMed;
	private River r;
	
	//modello del mondo
	private float Q;
	private float C;
	private List<Flow> fIn;
	private float fOut;
	private float fOutMin;
	
	//misurazioni in output
	private int nGiorniDisservizio;
	private float Cmed;
	
	//Impostazione parametri iniziali
	public void setK(float k) {
		this.k = k;
	}
	public void setfMed(float f) {
		this.fMed = f*3600*24; //conversione di fMed da m^3/s a m^3/gg
	}
	public void setR(River r) {
		this.r = r;
	}

	//Simulazione
	public void run() {
		this.queue = new PriorityQueue<Event>();
		
		//Stato iniziale
		this.Q = this.k*this.fMed*30;
		this.C = this.Q/2;
		this.fOutMin = (float) (0.8*this.fMed);
		this.nGiorniDisservizio = 0;
		this.Cmed = 0;
		
		//Eventi iniziali
		this.fIn = this.model.getFlow(r);
		for(Flow f : fIn) {
			this.queue.add(new Event(f.getDay(), f, EventType.INGRESSO));
		}
		
		//Ciclo di simulazione
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		case INGRESSO: 
			this.C += e.getFlow().getFlow(); //capienza corrente con flusso del giorno
			
			if(this.C > this.Q) //tracimazione
				this.queue.add(new Event(e.getDate(), e.getFlow(), EventType.TRACIMAZIONE));
			
			int p = (int) (Math.random()*100); //probabilità di irrigazione ogni giorno del 5%
			if(p < 5) //irrigazione
				this.queue.add(new Event(e.getDate(), e.getFlow(), EventType.IRRIGAZIONE));
			else //uscita classica
				this.queue.add(new Event(e.getDate(), e.getFlow(), EventType.USCITA));
			
			break;
			
		case USCITA:
			if(this.C < this.fOutMin) { //flusso minimo di uscita non disponibile
				this.nGiorniDisservizio++;
				this.C = 0;
				this.Cmed += this.C;
			}
			else { //flusso minimo disponibile
				this.C -= this.fOutMin;
				this.Cmed += this.C;
			}
			break;
			
		case TRACIMAZIONE:
			float diff = this.C - this.Q;
			this.C -= diff;
			
			break;
			
		case IRRIGAZIONE:
			this.fOut = 10*this.fOutMin;
			
			if(this.fOut > C) { 
				//irrigazione completa non possibile
				this.nGiorniDisservizio++;
				this.fOut = this.C;
				this.C = 0;
				this.Cmed += this.C;
			}
			else {
				//irrigazione possibile
				this.C -= this.fOut;
				this.Cmed += this.C;
			}
			break;
		}
	}
	
	//Ricavare parametri finali
	public int getnGiorniDisservizio() {
		return nGiorniDisservizio;
	}

	public float getCmed() {
		return Cmed/this.fIn.size();
	}
}