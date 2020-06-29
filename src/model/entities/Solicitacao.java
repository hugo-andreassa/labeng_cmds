package model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.entities.enums.StatusSolicitacao;

public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date dataAbertura;
	
	private StatusSolicitacao status;
	private List<ItemSolicitacao> itens = new ArrayList<>();
	
	public Solicitacao() {
		
	}

	public Solicitacao(Integer id, Date dataAbertura, StatusSolicitacao status) {
		this.id = id;
		this.dataAbertura = dataAbertura;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public StatusSolicitacao getStatus() {
		return status;
	}

	public void setStatus(StatusSolicitacao status) {
		this.status = status;
	}

	public List<ItemSolicitacao> getItens() {
		return itens;
	}
	
	public void addItens(ItemSolicitacao item) {
		itens.add(item);
	}
	
	public double total() {
		double sum = 0.0;
		for (ItemSolicitacao item : itens) {
			sum += item.subTotal();
		}
		
		return sum;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solicitacao other = (Solicitacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Data de Abertura: " + dataAbertura + ", Status: " + status;
	}
}
