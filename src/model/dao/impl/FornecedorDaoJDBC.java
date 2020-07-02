package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement stFornecedor = null;
		PreparedStatement stEndereco = null;
		ResultSet rs = null;
		
		try {
			conn.setAutoCommit(false);
			
			stFornecedor = conn.prepareStatement("INSERT INTO "
					+ "fornecedor(nome, telefone) "
					+ "VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			stFornecedor.setString(1, obj.getNome());
			stFornecedor.setString(2, obj.getTelefone());
			stFornecedor.executeUpdate();
			
			// Pego o ID gerado pra o fornecedor
			rs = stFornecedor.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				obj.setId(id);
			}
			
			stEndereco = conn.prepareStatement("INSERT INTO "
					+ "endereco(idfornecedor, rua, cidade, estado, cep, "
					+ "complemento, numero) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			stEndereco.setInt(1, obj.getId());
			stEndereco.setString(2, obj.getEndereco().getRua());
			stEndereco.setString(3, obj.getEndereco().getCidade());
			stEndereco.setString(4, obj.getEndereco().getEstado());
			stEndereco.setString(5, obj.getEndereco().getCep());
			stEndereco.setString(6, obj.getEndereco().getComplemento());
			stEndereco.setString(7, obj.getEndereco().getNumero());
			stEndereco.executeUpdate();
			
			conn.commit();			
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(stFornecedor);
			DB.closeStatement(stEndereco);
		}
	}

	@Override
	public void update(Fornecedor obj) {
		PreparedStatement stFornecedor = null;
		PreparedStatement stEndereco = null;
		
		try {
			conn.setAutoCommit(false);
			
			stFornecedor = conn.prepareStatement("UPDATE fornecedor "
					+ "SET nome = ?, telefone = ? "
					+ "WHERE id = ?");
			stFornecedor.setString(1, obj.getNome());
			stFornecedor.setString(2, obj.getTelefone());
			stFornecedor.setInt(3, obj.getId());
			stFornecedor.executeUpdate();
			
			stEndereco = conn.prepareStatement("UPDATE endereco "
					+ "SET rua = ?, cidade = ?, estado = ?, cep = ?, "
					+ "complemento = ?, numero = ? "
					+ "WHERE id = ?");
			stEndereco.setString(1, obj.getEndereco().getRua());
			stEndereco.setString(2, obj.getEndereco().getCidade());
			stEndereco.setString(3, obj.getEndereco().getEstado());
			stEndereco.setString(4, obj.getEndereco().getCep());
			stEndereco.setString(5, obj.getEndereco().getComplemento());
			stEndereco.setString(6, obj.getEndereco().getNumero());
			stEndereco.setInt(7, obj.getEndereco().getId());
			stEndereco.executeUpdate();
			
			conn.commit();			
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(stFornecedor);
			DB.closeStatement(stEndereco);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM fornecedor "
					+ "WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}		
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
		end.setNumero(rs.getString("numero"));
		end.setRua(rs.getString("rua"));
		
		forn.setId(rs.getInt("idforn"));
		forn.setNome(rs.getString("nome"));
		forn.setTelefone(rs.getString("telefone"));
		forn.setEndereco(end);
		
		return forn;
	}

}
