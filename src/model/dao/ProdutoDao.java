package model.dao;

import java.util.List;

import model.entities.Produto;

public interface ProdutoDao {
	
	void insert(Produto obj);
	void update(Produto obj);
	void deleteById(Integer id);
	Produto findById(Integer id);
	List<Produto> findAll();
}
