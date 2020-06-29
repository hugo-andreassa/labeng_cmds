package model.entities;

import java.io.Serializable;

import model.entities.enums.StatusItem;

public class ItemSolicitacao implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer quantidade;
	private Double preco;
	private StatusItem status;
	
	private Produto produto;
	
	public ItemSolicitacao() {
		
	}

	public ItemSolicitacao(Integer quantidade, Double preco, StatusItem status, Produto produto) {
		super();
		this.quantidade = quantidade;
		this.preco = preco;
		this.status = status;
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public StatusItem getStatus() {
		return status;
	}

	public void setStatus(StatusItem status) {
		this.status = status;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double subTotal() {
		return quantidade * preco;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((preco == null) ? 0 : preco.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ItemSolicitacao other = (ItemSolicitacao) obj;
		if (preco == null) {
			if (other.preco != null)
				return false;
		} else if (!preco.equals(other.preco))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Quantidade: " + quantidade + ", Preco: " + preco + ", Status: " + status + ", Produto: "
				+ produto;
	}
}
