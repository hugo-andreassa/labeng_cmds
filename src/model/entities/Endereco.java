package model.entities;

import java.io.Serializable;

public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String rua;
	private String cidade;
	private String estado;
	private String complemento;
	private String numero;
	private String cep;
	
	public Endereco() {
		
	}

	public Endereco(Integer id, String rua, String cidade, 
			String estado, String complemento, String numero,
			String cep) {
		this.id = id;
		this.rua = rua;
		this.cidade = cidade;
		this.estado = estado;
		this.complemento = complemento;
		this.numero = numero;
		this.cep = cep;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	public String toString() {
		return rua + ", " + cidade + ", " + estado + ", " + complemento + 
				", " + numero + ", " + cep;
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
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
