package model.dao;

import java.util.List;

import model.entities.Solicitacao;

public interface SolicitacaoDao {
	
	void insert(Solicitacao obj);
	void update(Solicitacao obj);
	void deleteById(Integer id);
	Solicitacao findById(Integer id);
	List<Solicitacao> findAll();
}
