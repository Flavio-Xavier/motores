package model.entities;

import java.io.Serializable;

public class Motor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String tensao; 
	private String corrente; 
	//private String potenciaCv; 
	private String rotacao;
	private String carcaca; 
	private String fatorPotencia; 
	private String fatorServico;
	private String fabricante;
	private String codigoSap;
	private String potenciaWatts; 
	private String grauProtecao;
	private String frequencia;
	private String rolamentoDianteiro; 
	private String rolamentoTraseiro;
	private Area area;
	private Equipamento equipamento;
	
	public Motor () {
		
	}
	
	

	public Motor(Integer id, String tensao, String corrente, /*String potenciaCv,*/ String rotacao, String carcaca,
			String fatorPotencia, String fatorServico, String fabricante, String codigoSap, String potenciaWatts,
			String grauProtecao, String frequencia, String rolamentoDianteiro, String rolamentoTraseiro, Area area,
			Equipamento equipamento) {
		this.id = id;
		this.tensao = tensao;
		this.corrente = corrente;
		//this.potenciaCv = potenciaCv;
		this.rotacao = rotacao;
		this.carcaca = carcaca;
		this.fatorPotencia = fatorPotencia;
		this.fatorServico = fatorServico;
		this.fabricante = fabricante;
		this.codigoSap = codigoSap;
		this.potenciaWatts = potenciaWatts;
		this.grauProtecao = grauProtecao;
		this.frequencia = frequencia;
		this.rolamentoDianteiro = rolamentoDianteiro;
		this.rolamentoTraseiro = rolamentoTraseiro;
		this.area = area;
		this.equipamento = equipamento;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTensao() {
		return tensao;
	}

	public void setTensao(String tensao) {
		this.tensao = tensao;
	}

	public String getCorrente() {
		return corrente;
	}

	public void setCorrente(String corrente) {
		this.corrente = corrente;
	}

//	public String getPotenciaCv() {
//		return potenciaCv;
//	}
//
//	public void setPotenciaCv(String potenciaCv) {
//		this.potenciaCv = potenciaCv;
//	}

	public String getRotacao() {
		return rotacao;
	}

	public void setRotacao(String rotacao) {
		this.rotacao = rotacao;
	}

	public String getCarcaca() {
		return carcaca;
	}

	public void setCarcaca(String carcaca) {
		this.carcaca = carcaca;
	}

	public String getFatorPotencia() {
		return fatorPotencia;
	}

	public void setFatorPotencia(String fatorPotencia) {
		this.fatorPotencia = fatorPotencia;
	}

	public String getFatorServico() {
		return fatorServico;
	}

	public void setFatorServico(String fatorServico) {
		this.fatorServico = fatorServico;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getCodigoSap() {
		return codigoSap;
	}

	public void setCodigoSap(String codigoSap) {
		this.codigoSap = codigoSap;
	}

	public String getPotenciaWatts() {
		return potenciaWatts;
	}

	public void setPotenciaWatts(String potenciaWatts) {
		this.potenciaWatts = potenciaWatts;
	}

	public String getGrauProtecao() {
		return grauProtecao;
	}

	public void setGrauProtecao(String grauProtecao) {
		this.grauProtecao = grauProtecao;
	}

	public String getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}

	public String getRolamentoDianteiro() {
		return rolamentoDianteiro;
	}

	public void setRolamentoDianteiro(String rolamentoDianteiro) {
		this.rolamentoDianteiro = rolamentoDianteiro;
	}

	public String getRolamentoTraseiro() {
		return rolamentoTraseiro;
	}

	public void setRolamentoTraseiro(String rolamentoTraseiro) {
		this.rolamentoTraseiro = rolamentoTraseiro;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
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
		Motor other = (Motor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Motor [id=" + id + ", tensao=" + tensao + ", corrente=" + corrente +/* ", potenciaCv=" + potenciaCv
				+*/ ", rotacao=" + rotacao + ", carcaca=" + carcaca + ", fatorPotencia=" + fatorPotencia
				+ ", fatorServico=" + fatorServico + ", fabricante=" + fabricante + ", codigoSap=" + codigoSap
				+ ", potenciaWatts=" + potenciaWatts + ", grauProtecao=" + grauProtecao + ", frequencia=" + frequencia
				+ ", rolamentoDianteiro=" + rolamentoDianteiro + ", rolamentoTraseiro=" + rolamentoTraseiro + ", area="
				+ area + ", equipamento=" + equipamento + "]";
	}
	

}
