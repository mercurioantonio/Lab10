package it.polito.tdp.rivers.db;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class RiversDAO {

	public List<River> getAllRivers() {
		
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return rivers;
	}
	
    public List<LocalDate> getDate(River river) {
		
		final String sql = "SELECT MIN(DAY) AS MIN, MAX(DAY) AS MAX "
				+ "FROM flow f "
				+ "WHERE f.river=?";

		LocalDate dataI;
		LocalDate dataF;
		List<LocalDate> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				dataI = res.getDate("MIN").toLocalDate();
				dataF = res.getDate("MAX").toLocalDate();
				result.add(dataI);
				result.add(dataF);
				
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
    
public void getAVG(River river) {
		
		final String sql = "SELECT AVG(flow) AS avg "
				+ "FROM flow f "
				+ "WHERE f.river=?";

		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				river.setFlowAvg(res.getDouble("avg"));
				
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		
	}
 
    public List<Flow> getFlow(River river) {
	
	final String sql = "SELECT day, flow "
			+ "FROM flow f "
			+ "WHERE f.river=?";

	List<Flow> flows = new LinkedList<>();

	try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, river.getId());
		ResultSet res = st.executeQuery();

		while (res.next()) {
			flows.add(new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow")*3600*24, river));
			
		}

		river.setFlows(flows);
		conn.close();
		
	} catch (SQLException e) {
		//e.printStackTrace();
		throw new RuntimeException("SQL Error");
	}
	
	return flows;

}

	
	
}
