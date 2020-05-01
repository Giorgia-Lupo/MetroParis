package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroparis.model.CoppiaFermate;
import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	public List<Linea> getAllLinee() {
		final String sql = "SELECT id_linea, nome, velocita, intervallo FROM linea ORDER BY nome ASC";

		List<Linea> linee = new ArrayList<Linea>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Linea f = new Linea(rs.getInt("id_linea"), rs.getString("nome"), rs.getDouble("velocita"),
						rs.getDouble("intervallo"));
				linee.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee;
	}
 
	// PER IL METODO 1)
	public boolean fermateConnesse(Fermata fp, Fermata fa) {
		String sql = "SELECT COUNT(*) AS C " + "FROM connessione " + "WHERE id_stazP=? " + "AND id_stazA=?";

		//se COUNT(*) = 1 --> esiste connessione
		//se COUNT(*) = 0 --> non esiste connessione
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, fp.getIdFermata());
			st.setInt(2, fa.getIdFermata());

			ResultSet res = st.executeQuery();

			res.first(); // mi metto sulla prima riga
			int linee = res.getInt("C"); // numero di linee

			conn.close();

			return linee >= 1; //in questo caso ritorna vero

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// PER IL METODO 2)                                 //per "conversione" da ID a Fermata
	public List<Fermata> fermateSuccessive(Fermata fp, Map<Integer, Fermata> fermateIdMap) {
		String sql = "SELECT DISTINCT id_stazA " + "FROM connessione " + "WHERE id_stazP=?";
		//con DINSTINCT evito i duplicati --> grafo semplice
		List<Fermata> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, fp.getIdFermata());

			ResultSet res = st.executeQuery();

			while (res.next()) {
				int id_fa = res.getInt("id_stazA"); // ID fermata arrivo
				result.add(fermateIdMap.get(id_fa)); //Prendo Fermata dato ID
			}

			conn.close();

			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// PER IL METODO 3) --> anche in questo caso ho bisogno della conversione: ID-->Fermata 
	public List<CoppiaFermate> coppieFermate(Map<Integer, Fermata> fermateIdMap) {
		String sql = "SELECT DISTINCT id_stazP, id_stazA FROM connessione" ;
		
		List<CoppiaFermate> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				CoppiaFermate c = new CoppiaFermate(
						fermateIdMap.get(res.getInt("id_stazP")), 
						fermateIdMap.get(res.getInt("id_stazA"))) ;//prendo oggetto Fermata dato ID
				result.add(c);
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result ;
	}

}
