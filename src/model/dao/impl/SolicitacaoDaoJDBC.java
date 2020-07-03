package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.SolicitacaoDao;
import model.entities.Endereco;
import model.entities.Fornecedor;
import model.entities.ItemSolicitacao;
import model.entities.Produto;
import model.entities.Solicitacao;
import model.entities.enums.StatusItem;
import model.entities.enums.StatusSolicitacao;

public class SolicitacaoDaoJDBC implements SolicitacaoDao {

	private Connection conn;
	
	public SolicitacaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Solicitacao obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Solicitacao obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Solicitacao findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Solicitacao> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from solicitacao");
			rs = st.executeQuery();
			
			List<Solicitacao> list = new ArrayList<>();
			while(rs.next()) {
				Solicitacao s = instantiateSolicitacao(rs);
				list.add(s);
			}
			
			for (Solicitacao s : list) {
				s = addItemsSolicitacao(s);
			}
			
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Solicitacao addItemsSolicitacao(Solicitacao s) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM itemsolicitacao "
					+ "inner join produto "
					+ "on produto.id = idproduto "
					+ "inner join fornecedor "
					+ "on produto.idfornecedor = fornecedor.id "
					+ "inner join endereco "
					+ "on fornecedor.id = endereco.idfornecedor "
					+ "WHERE idsolicitacao = ?");
			st.setInt(1, s.getId());
			rs = st.executeQuery();
			
			while(rs.next()) {
				s.addItens(instantiateItemSolicitacao(rs));
			}
			
			return s;
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	}
	
	private Solicitacao instantiateSolicitacao(ResultSet rs) throws SQLException {
		Solicitacao s = new Solicitacao();
		
		s.setId(rs.getInt("Solicitacao.Id"));
		s.setStatus(StatusSolicitacao.valueOf(rs.getString("Solicitacao.Status")));
		s.setDataAbertura(rs.getDate("Instante"));
		
		return s;
	}
	
	private ItemSolicitacao instantiateItemSolicitacao(ResultSet rs) throws SQLException {
		Endereco end = new Endereco();
		Fornecedor forn = new Fornecedor();
		Produto prod = new Produto();
		ItemSolicitacao is = new ItemSolicitacao();
		
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
		
		is.setQuantidade(rs.getInt("Quantidade"));
		is.setStatus(StatusItem.valueOf(rs.getString("ItemSolicitacao.Status")));
		is.setPreco(rs.getDouble("ItemSolicitacao.Preco"));
		is.setProduto(prod);
		
		return is;
	}
}
