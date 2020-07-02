package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.FornecedorDao;
import model.entities.Endereco;
import model.entities.Fornecedor;

public class FornecedorDaoJDBC implements FornecedorDao {
	
	private Connection conn;
	
	public FornecedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Fornecedor obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Fornecedor obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Fornecedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT f.id AS idforn, "
					+ "f.nome, f.telefone, e.id as idend, "
					+ "e.rua, e.cidade, e.estado, e.cep, "
					+ "e.complemento, e.numero "
					+ "FROM fornecedor f "
					+ "INNER JOIN endereco e "
					+ "ON f.id = e.idfornecedor "
					+ "where f.id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Fornecedor forn = instantiateFornecedor(rs);
				
				return forn;
			}
			
			return null;			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	}

	@Override
	public List<Fornecedor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT f.id AS idforn, "
					+ "f.nome, f.telefone, e.id as idend, "
					+ "e.rua, e.cidade, e.estado, e.cep, "
					+ "e.complemento, e.numero "
					+ "FROM fornecedor f "
					+ "INNER JOIN endereco e "
					+ "ON f.id = e.idfornecedor");
			
			rs = st.executeQuery();
			
			List<Fornecedor> list = new ArrayList<>();
			while(rs.next()) {
				Fornecedor forn = instantiateFornecedor(rs);
				list.add(forn);
			}			
			
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
		
	}
	
	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
		Endereco end = new Endereco();
		Fornecedor forn = new Fornecedor();
		
		end.setId(rs.getInt("idend"));
		end.setCep(rs.getString("cep"));
		end.setCidade(rs.getString("cidade"));
		end.setComplemento(rs.getString("complemento"));
		end.setEstado(rs.getString("estado"));
		end.setNumero(rs.getInt("numero"));
		end.setRua(rs.getString("rua"));
		
		forn.setId(rs.getInt("idforn"));
		forn.setNome(rs.getString("nome"));
		forn.setTelefone(rs.getString("telefone"));
		forn.setEndereco(end);
		
		return forn;
	}

}
