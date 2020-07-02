package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.FornecedorDao;
import model.entities.Fornecedor;

public class FornecedorService {
	
	private FornecedorDao dao = DaoFactory.createFornecedorDao();
	
	public List<Fornecedor> findAll() {
		return dao.findAll();
	}
	
	public Fornecedor findById(int id) {
		return dao.findById(id);
	}
	
	public void saveOrUpdate(Fornecedor obj) {
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
