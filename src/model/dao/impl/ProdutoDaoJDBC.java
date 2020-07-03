package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.ProdutoDao;
import model.entities.Endereco;
import model.entities.Fornecedor;
import model.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDao{

	private Connection conn;
	
	public ProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("INSERT INTO "
					+ "produto(IdFornecedor, Nome, Preco, Descricao) "
					+ "VALUES(?, ?, ?, ?)");
			st.setInt(1, obj.getFornecedor().getId());
			st.setString(2, obj.getNome());
			st.setDouble(3, obj.getPreco());
			st.setString(4, obj.getDescricao());
			int rows = st.executeUpdate();
			
			if(rows == 0) {
				conn.rollback();
				throw new DBException("Erro inesperado! Nenhuma linha foi afetada!");
			}
			
			conn.commit();
			conn.setAutoCommit(true);			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Produto obj) {
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("UPDATE produto "
					+ "SET IdFornecedor = ?, Nome = ?, "
					+ "Preco = ?, Descricao = ? "
					+ "WHERE Id = ?");
			st.setInt(1, obj.getFornecedor().getId());
			st.setString(2, obj.getNome());
			st.setDouble(3, obj.getPreco());
			st.setString(4, obj.getDescricao());
			st.setInt(5, obj.getId());
			int rows = st.executeUpdate();
			
			if(rows == 0) {
				conn.rollback();
				throw new DBException("Erro inesperado! Nenhuma linha foi afetada!");
			}
			
			conn.commit();
			conn.setAutoCommit(true);			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM produto "
					+ "WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Produto findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from "
					+ "produto "
					+ "inner join fornecedor "
					+ "on produto.idfornecedor = fornecedor.id "
					+ "inner join endereco "
					+ "on fornecedor.id = endereco.idfornecedor "
					+ "WHERE produto.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Produto prod = instantiateProduto(rs);

				return prod;
			}
			
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Produto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from "
					+ "produto "
					+ "inner join fornecedor "
					+ "on produto.idfornecedor = fornecedor.id "
					+ "inner join endereco "
					+ "on fornecedor.id = endereco.idfornecedor");
			rs = st.executeQuery();
			
			List<Produto> list = new ArrayList<>();
			while(rs.next()) {
				Produto prod = instantiateProduto(rs);
				list.add(prod);
			}
			
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Produto instantiateProduto(ResultSet rs) throws SQLException {
		Endereco end = new Endereco();
		Fornecedor forn = new Fornecedor();
		Produto prod = new Produto();
		
		end.setId(rs.getInt("Endereco.Id"));
		end.setCep(rs.getString("Cep"));
		end.setCidade(rs.getString("Cidade"));
		end.setComplemento(rs.getString("Complemento"));
		end.setEstado(rs.getString("Estado"));
		end.setNumero(rs.getString("Numero"));
		end.setRua(rs.getString("Rua"));
		
		forn.setId(rs.getInt("Fornecedor.Id"));
		forn.setNome(rs.getString("Fornecedor.Nome"));
		forn.setTelefone(rs.getString("Telefone"));
		forn.setEndereco(end);
		
		prod.setId(rs.getInt("Produto.Id"));
		prod.setNome(rs.getString("Produto.Nome"));
		prod.setDescricao(rs.getString("Descricao"));
		prod.setPreco(rs.getDouble("Produto.Preco"));
		prod.setFornecedor(forn);	
		
		return prod;
	}
}
