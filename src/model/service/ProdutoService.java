package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Produto;

public class ProdutoService {
	
	private ProdutoDao dao = DaoFactory.createProdutoDao();
	
	public List<Produto> findAll() {
		return dao.findAll();
	}
	
	public Produto findById(int id) {
		return dao.findById(id);
	}
	
	public void saveOrUpdate(Produto obj) {
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
