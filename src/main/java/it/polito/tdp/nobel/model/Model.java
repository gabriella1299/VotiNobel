package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	//e' un problema di ottimizzazione: struttura dati per salvare soluzione migliore
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao=new EsameDAO();
		this.partenza=dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale=new HashSet<Esame>();
		soluzioneMigliore=new HashSet<Esame>();//ogni volta che calcolo insieme esami la lista si pulisce
		mediaSoluzioneMigliore=0;
		
		//cerca1(parziale,0,numeroCrediti);
		cerca2(parziale,0,numeroCrediti);
		return soluzioneMigliore;//se e' vuota non ho trovato la soluzione migliore	
	}

	/*COMPLESSITA: N!*/
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		
		int crediti=sommaCrediti(parziale);//somma corrente dei crediti di parziale
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale);//la soluzione migliore diventa la parziale che sto analizzando
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		//sicuramente ora: crediti<m
		//raggiungiamo L=N--> non ci sono piu' esami da aggiungere		
		if(L==partenza.size()) {
			return;
		}
		
		//Se arrivo qui non rientro nei casi terminali quindi vado avanti ad esplorare il ramo
		//generare i sotto-problemi
		for(Esame e:partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale,L+1,m);
				parziale.remove(e);
			}
		}
	
	}
	
	//Complessita: 2^N
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		int crediti=sommaCrediti(parziale);
		if(crediti>m) {
				return;
			}
			if(crediti==m) {
				double media=calcolaMedia(parziale);
				if(media>mediaSoluzioneMigliore) {
					soluzioneMigliore=new HashSet<>(parziale);//la soluzione migliore diventa la parziale che sto analizzando
					mediaSoluzioneMigliore=media;
				}
				return;
			}
			if(L==partenza.size()) {
				return;
			}
			
			//generazione sottoproblemi
			//partenza L e' da aggiungere o no? Provo entrambe le cose
			parziale.add(partenza.get(L));
			cerca2(parziale,L+1,m);
			
			parziale.remove(partenza.get(L));
			cerca2(parziale,L+1,m);
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
