package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SolicitacaoDao;
import model.entities.Solicitacao;

public class SolicitacaoService {
	
	private SolicitacaoDao dao = DaoFactory.createSolicitacaoDao();
	
	public List<Solicitacao> findAll() {
		return dao.findAll();
	}
	
	public Solicitacao findById(int id) {
		return dao.findById(id);
	}
	
	public void saveOrUpdate(Solicitacao obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}		
	}
	
	public void delete(Integer id) {
		dao.deleteById(id);
	}
}
