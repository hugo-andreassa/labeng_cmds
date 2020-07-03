package model.dao;

import db.DB;
import model.dao.impl.FornecedorDaoJDBC;
import model.dao.impl.ProdutoDaoJDBC;
import model.dao.impl.SolicitacaoDaoJDBC;

public class DaoFactory {
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
	
	public static SolicitacaoDao createSolicitacaoDao() {
		return new SolicitacaoDaoJDBC(DB.getConnection());
	}
	
}
