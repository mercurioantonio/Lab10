package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	RiversDAO rdao;
	
	

	public Model() {
		super();
		rdao = new RiversDAO();
		
	}
	
	public List<River> getRivers(){
		return rdao.getAllRivers();
		
	}
	
	public List<LocalDate> getDate(River river){
		return rdao.getDate(river);
	}
	
	public void getAVG(River river){
		rdao.getAVG(river);
		
	}
	
	public List<Flow> getFlow(River river){
		return rdao.getFlow(river);
	}
	
	
	
	
}
