package it.polito.tdp.radio.bean;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import it.polito.tdp.radio.db.RadioDAO;

public class Model {
	private List<Citta> citta;
	private Map<Integer, Citta> map;
	private Map<Integer, Ponte> mapPonti;
	private UndirectedGraph<Citta, Ponte>graph;

	Citta c1;
	Citta c2;
	Citta c3;
	private Set<Ponte> ottimo;
	private Set<Ponte> daConsiderare;
	
	public List<Citta> getCities() {
		if(citta==null){
			RadioDAO dao = new RadioDAO();
			citta= dao.getAllCitta();
			map = new HashMap<>();
			for(Citta c: citta)
				map.put(c.getIdCitta(), c);
		}
		Collections.sort(citta);
		return citta;
	}

	public Map<Integer, Ponte> getMappaPonti(){
		if(mapPonti==null){
			RadioDAO dao = new RadioDAO();
			List<Ponte> ponti = dao.getAllPonte();
			mapPonti = new HashMap<>();
			
			for(Ponte p: ponti){
				mapPonti.put(p.getIdPonte(), p);
			}
		}
		return mapPonti;
	}
	
	public List<Ponte> getPontiCheCoprono(Citta c1, Citta c2) {
		RadioDAO dao = new RadioDAO();
		List<Ponte> collegati = dao.getPontiCheCoprono(c1, c2, this.getMappaPonti());
		
		return  collegati;
	}
/*
	public Set<Ponte> createPath(Citta c1, Citta c2, Citta c3) {
		RadioDAO dao = new RadioDAO();
		List<Ponte> l1 =dao.getPontiCheCoprono(c1, c2, this.getMappaPonti());
		List<Ponte> l2 = dao.getPontiCheCoprono(c2, c3, mapPonti);
		List<Ponte> l3 = dao.getPontiCheCoprono(c3, c1, mapPonti);
		
		Set<Ponte> ponti = new HashSet<Ponte>();
		
		int max=Integer.MAX_VALUE;

		if(l1.size()!=0){
			max =2;
			
			for(Ponte p1: l1){
				if(l2.size()!=0){
					for(Ponte p2:l2){
						if(p1.equals(p2)){
							max =1;
							ponti.add(l1.get(0));
							break;
						} else{
							if(l3.size()!=0){
								if(l3.contains(p2)){
									max =2;
								}
							}
							
							
							
							max=2;
							ponti.add(l1.get(0));
							ponti.add(l2.get(0));
							break;
						}
					}
				} else {
					max =2;
					ponti.add(l1.get(0));
					ponti.add(dao.getPonteByCitta(c3, mapPonti));
				}
			}
		}
		if(l1.size()==0 && l2.size()!=0){
			max=2;
			ponti.removeAll(ponti);
			
			ponti.add(dao.getPonteByCitta(c1, mapPonti));
			ponti.add(l2.get(0));
		}
		
		if(l1.size()==0 && l2.size()==0){
			ponti.removeAll(ponti);
			
			ponti.add(dao.getPonteByCitta(c1, mapPonti));
			ponti.add(dao.getPonteByCitta(c2, mapPonti));
			ponti.add(dao.getPonteByCitta(c3, mapPonti));
			max=3;
		}
		
		return ponti;
	}
	*/
	//LIMITARE IL GRAFO SOLO A QUELLO CHE MI SERVE, FARE GLI ARCHI E POI OTTENGO CIò CHE VOGLIO
	public void creaGrafo(Citta c1, Citta c2, Citta c3){
		graph = new Multigraph<>(Ponte.class);
	
		RadioDAO dao = new RadioDAO();
		graph.addVertex(c1);
		graph.addVertex(c2);
		graph.addVertex(c3);
		
		this.c1=c1;
		this.c2= c2;
		this.c3=c3;
		
		for(Ponte p : dao.getPontiCheCoprono(c1, c2, mapPonti)){
			graph.addEdge(c1, c2, p);
		}
		
		for(Ponte p : dao.getPontiCheCoprono(c2, c3, mapPonti)){
			graph.addEdge(c2, c3, p);
		}
		for(Ponte p : dao.getPontiCheCoprono(c3, c1, mapPonti)){
			graph.addEdge(c3, c1, p);
		}
		
		
		System.out.println(graph.toString());
		}
		
	public Set<Ponte> getPath(){
		Set<Ponte> ponti = new HashSet<>();
		RadioDAO dao = new RadioDAO();
		
		
		if(graph.edgeSet().size()==0){
			ponti.add(dao.getPonteByCitta(c1, mapPonti));
			ponti.add(dao.getPonteByCitta(c2, mapPonti));
			ponti.add(dao.getPonteByCitta(c3, mapPonti));
		}
		else {
			DijkstraShortestPath<Citta, Ponte> dj = new DijkstraShortestPath<Citta, Ponte>(graph, c1, c2);
			if(dj.getPathEdgeList()!=null)
				ponti.addAll(dj.getPathEdgeList());
			else
				ponti.add(dao.getPonteByCitta(c1, mapPonti));
			
			DijkstraShortestPath<Citta, Ponte> dj2 = new DijkstraShortestPath<Citta, Ponte>(graph, c2, c3);
			if(dj2.getPathEdgeList()!=null)
				ponti.addAll(dj2.getPathEdgeList());
			else
				ponti.add(dao.getPonteByCitta(c2, mapPonti));
			
			DijkstraShortestPath<Citta, Ponte> dj3 = new DijkstraShortestPath<Citta, Ponte>(graph, c3, c1);
			if(dj3.getPathEdgeList()!=null)
				ponti.addAll(dj3.getPathEdgeList());	
			else
				ponti.add(dao.getPonteByCitta(c3, mapPonti));
		}
		return ponti;
	}
	
	public Set<Ponte> interfacciaRicorsione(Citta cc1,Citta cc2,Citta cc3){
		c1=cc1;
		c2=cc2;
		c3=cc3;
		
		RadioDAO dao = new RadioDAO();
		List<Ponte> l1 =dao.getPontiCheCoprono(c1,this.getMappaPonti());
		List<Ponte> l2 = dao.getPontiCheCoprono(c2, mapPonti);
		List<Ponte> l3 = dao.getPontiCheCoprono(c3,mapPonti);
		
		if(c1.getPonti()==null){
			c1.addPonti(l1);
		}
		if(c2.getPonti()==null){
			c2.addPonti(l2);
		}
		if(c3.getPonti()==null){
			c3.addPonti(l3);
		}
		
		HashSet<Ponte> soluzioneParziale = new HashSet<Ponte>();
		ottimo=new HashSet<Ponte>();
		
		ottimo.addAll(c1.getPonti());
		ottimo.addAll(c2.getPonti());
		ottimo.addAll(c3.getPonti());
		
		daConsiderare=new HashSet<Ponte>();
		
		daConsiderare.addAll(c1.getPonti());
		daConsiderare.addAll(c2.getPonti());
		daConsiderare.addAll(c3.getPonti());
		
		this.ricorsivo(soluzioneParziale,null);
		
		return ottimo;
	}

	private void ricorsivo(HashSet<Ponte> soluzioneParziale,Ponte ultimoAggiunto) {
		if(soluzioneParziale.size()>3||soluzioneParziale.size()>=ottimo.size())
			return;

		boolean tib1=false;
		boolean tib2=false;
		boolean tib3=false;
		
		for(Ponte p1:soluzioneParziale){
			if(c1.getPonti().contains(p1))
				tib1=true;
		}
		for(Ponte p2:soluzioneParziale){
			if(c2.getPonti().contains(p2))
				tib2=true;
		}
		for(Ponte p3:soluzioneParziale){
			if(c3.getPonti().contains(p3)){
				tib3=true;
			}
				
		}
		
		if(tib1&&tib2&&tib3){
			ottimo.clear();
			ottimo.addAll(soluzioneParziale);
		}
		
		for(Ponte p:daConsiderare){
			if(soluzioneParziale.size()==0||ultimoAggiunto.compareTo(p)<0){
				soluzioneParziale.add(p);
				ricorsivo(soluzioneParziale,p);
				soluzioneParziale.remove(p);
			}
		}
	}

}
