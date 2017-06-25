package it.polito.tdp.radio.db;

import it.polito.tdp.radio.bean.Citta;
import it.polito.tdp.radio.bean.CityPair;
import it.polito.tdp.radio.bean.Ponte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe DAO per l'accesso al database {@code pontiradio}
 * 
 * @author Fulvio
 * 
 */

public class RadioDAO {

	/**
	 * Interroga il database e restituisce la {@link Citta} che ha il campo
	 * {@code idCitta} pari a quello specificato
	 * 
	 * @param idCitta il valore dell'ID Città da ricercare
	 * @return la {@link Citta} cercata, oppure {@code null} se non esiste
	 */
	public Citta getCittaById(int idCitta) {
		
		throw new UnsupportedOperationException("Method not implemented - sorry") ;
		
		//return null;

	}

	/**
	 * Interroga il database e restituisce tutti i dati nella tabella
	 * {@code citta} sotto forma di un {@link ArrayList} di {@link Citta}.
	 * 
	 * @return la {@link ArrayList} di {@link Citta}
	 */

	public List<Citta> getAllCitta() {
		
		final String sql = "SELECT distinct * FROM citta ";

		List<Citta> citta = new ArrayList<Citta>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Citta c = new Citta(
						rs.getInt("idCitta"),
						rs.getString("call"),
						rs.getString("city")
						);
			
				citta.add(c);
			}
			conn.close();
			return citta;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Interroga il database e restituisce tutti i dati nella tabella
	 * {@code ponte} sotto forma di un {@link ArrayList} di {@link Ponte}.
	 * 
	 * @return la {@link ArrayList} di {@link Ponte}
	 */

	public List<Ponte> getAllPonte() {
		
		final String sql = "SELECT * FROM ponte";

		List<Ponte> ponti = new ArrayList<Ponte>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Ponte p = new Ponte(
						rs.getInt("idPonte"),
						rs.getString("output"),
						rs.getString("call"),
						rs.getDouble("pl"),
						rs.getString("comments")
						);
				ponti.add(p);
			}
			conn.close();
			return ponti;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	public static void main(String[] args) {
		
		RadioDAO dao = new RadioDAO() ;
		
		List<Citta> citta = dao.getAllCitta() ;
		
		for( Citta c: citta ) {
			System.out.format("%4d %s %s\n", c.getIdCitta(), c.getCall(), c.getCity()) ;
		}
		
		List<Ponte> ponti = dao.getAllPonte() ;

		for( Ponte p : ponti ) {
			System.out.format("%4d %s %s\n", p.getIdPonte(), p.getCall(), p.getComments()) ;
		}
		
	}

	public List<Ponte> getPontiCheCoprono(Citta c1, Citta c2, Map<Integer, Ponte> map) {
		final String sql= "select p.* "+
							"from copertura as cop,copertura as cop2, citta as c1, citta as c2, ponte as p "+
							"where c1.idCitta=? and c2.idCitta=? and "+
							"cop.idPonte=cop2.idPonte and cop.idCitta=c1.idCitta and cop2.idCitta=c2.idCitta "+
							"and p.idPonte=cop.idPonte";
		
		List<Ponte> ponti = new ArrayList<Ponte>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, c1.getIdCitta());
			st.setInt(2, c2.getIdCitta());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Ponte p = map.get(rs.getInt("idPonte"));

				if(p==null){
					 p = new Ponte(
							rs.getInt("idPonte"),
							rs.getString("output"),
							rs.getString("call"),
							rs.getDouble("pl"),
							rs.getString("comments")
							);
					map.put(p.getIdPonte(), p);
				}
				
				ponti.add(p);
			}
			
			conn.close();
			return ponti;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Ponte getPonteByCitta(Citta c3, Map<Integer, Ponte> map) {
		final String sql = "select p.idPonte "+
							"from citta as c1, copertura as c, ponte as p "+
							"where c1.idCitta=c.idCitta and c.idPonte= p.idPonte and c.idCitta=? ";
		
		

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, c3.getIdCitta());
			
			
			ResultSet rs = st.executeQuery();

			Ponte p = null;
			if (rs.next()) {
				
				p = map.get(rs.getInt("idPonte"));

				if(p==null){
					 p = new Ponte(
							rs.getInt("idPonte"),
							rs.getString("output"),
							rs.getString("call"),
							rs.getDouble("pl"),
							rs.getString("comments")
							);
					map.put(p.getIdPonte(), p);
				}
			}	
			
			conn.close();
				return p;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<CityPair> getCityPair() {
		String sql ="select c.idCitta as cit1, c1.idCitta as cit2, c.idPonte as p "+
					"from copertura as c, copertura as c1, citta as cit "+
					"where c.idPonte=c1.idPonte and c.idCitta<>c1.idCitta ";
		
		List<CityPair> cittacollegate= new ArrayList<>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			Ponte p = null;
			while (rs.next()) {
				
				cittacollegate.add(new CityPair(rs.getInt("cit1"), rs.getInt("cit2"), rs.getInt("p")));
			}	
			
			conn.close();
				return cittacollegate;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Ponte> getPontiCheCoprono(Citta c1, Map<Integer, Ponte> map) {
		final String sql= "select p.* "+
				"from copertura as cop, citta as c1, ponte as p "+
				"where c1.idCitta=? and "+
				"cop.idCitta=c1.idCitta "+
				"and p.idPonte=cop.idPonte";

List<Ponte> ponti = new ArrayList<Ponte>();

try {
Connection conn = DBConnect.getInstance().getConnection();
PreparedStatement st = conn.prepareStatement(sql);
st.setInt(1, c1.getIdCitta());

ResultSet rs = st.executeQuery();

while (rs.next()) {
	
	Ponte p = map.get(rs.getInt("idPonte"));

	if(p==null){
		 p = new Ponte(
				rs.getInt("idPonte"),
				rs.getString("output"),
				rs.getString("call"),
				rs.getDouble("pl"),
				rs.getString("comments")
				);
		map.put(p.getIdPonte(), p);
	}
	
	ponti.add(p);
	c1.addPonti(ponti);
}

conn.close();
return ponti;
} catch (SQLException e) {
e.printStackTrace();
throw new RuntimeException(e);
}
}	
		
		}
