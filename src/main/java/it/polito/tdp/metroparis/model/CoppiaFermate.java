package it.polito.tdp.metroparis.model;

public class CoppiaFermate {
	 //CREATA PER IL METODO 3)
	private Fermata fp ;
	private Fermata fa ;
	/**
	 * @param fp
	 * @param fa
	 */
	public CoppiaFermate(Fermata fp, Fermata fa) {
		super();
		this.fp = fp;
		this.fa = fa;
	}
	public Fermata getFp() {
		return fp;
	}
	public void setFp(Fermata fp) {
		this.fp = fp;
	}
	public Fermata getFa() {
		return fa;
	}
	public void setFa(Fermata fa) {
		this.fa = fa;
	}
	
	
}
