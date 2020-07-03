package model.dao.impl;

import java.util.List;

import model.dao.SolicitacaoDao;
import model.entities.Solicitacao;

public class SolicitacaoDaoJDBC implements SolicitacaoDao {

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
		// TODO Auto-generated method stub
		return null;
	}
	
	private Solicitacao instantiateSolicitacao() {
		return new Solicitacao();
	}
	
}
