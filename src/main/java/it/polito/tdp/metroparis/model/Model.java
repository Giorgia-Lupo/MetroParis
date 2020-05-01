package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> graph ;
	private List<Fermata> fermate ;
	private Map<Integer, Fermata> fermateIdMap ;//utile per metodo 2)
	
	public Model() {
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class) ;
		
		MetroDAO dao = new MetroDAO() ;
		
		// CREAZIONE DEI VERTICI
		this.fermate = dao.getAllFermate() ;
		
		this.fermateIdMap = new HashMap<>() ;
		for(Fermata f: this.fermate) {
			fermateIdMap.put(f.getIdFermata(), f) ; //popolo la mappa
		}
		
		// CREAZIONE DEI VERTICI
		Graphs.addAllVertices(this.graph, this.fermate) ;
		
		
		//CREAZIONE DEGLI ARCHI
				
		// CREAZIONE DEGLI ARCHI -- metodo 1 (coppie di vertici)
		//Grafo ha dei vertici, e tra ogni coppia l'arco c'è o no? Query per ogni coppia di query
		//VA BENE CON GRAFO PICCOLO, complessita' pari a n^2
		/*
		for(Fermata fp : this.fermate) {
			for(Fermata fa : this.fermate) {
				if( dao.fermateConnesse(fp, fa) ) { //esiste connessione da fa a fp?
					this.graph.addEdge(fp, fa) ; //aggiungo arco
				}
			}
		}
		*/
		
		// CREAZIONE DEGLI ARCHI -- metodo 2 (da un vertice, trova tutti i connessi)
		//Avrei meno query. Dati uscenti dalla stazione
		//PIU' VELOCE DEL PRIMO, complessita' pari a n
		/*
		for(Fermata fp: this.fermate) {
			List<Fermata> connesse = dao.fermateSuccessive(fp, fermateIdMap); //fermate adiacenti alla fp
			
			for(Fermata fa: connesse) {
				this.graph.addEdge(fp, fa) ;
			}
		}
		*/
		
		// CREAZIONE DEGLI ARCHI -- metodo 3 (chiedo al DB l'elenco degli archi)
		List<CoppiaFermate> coppie = dao.coppieFermate(fermateIdMap) ;
		for(CoppiaFermate c : coppie) {
			this.graph.addEdge(c.getFp(), c.getFa()) ;
		}
				
		
//		System.out.println(this.graph) ;
		System.out.format("Grafo caricato con %d vertici %d archi",
				this.graph.vertexSet().size(),
				this.graph.edgeSet().size());

	}
	
	public static void main(String args[]) {
		new Model() ;
	}

}
