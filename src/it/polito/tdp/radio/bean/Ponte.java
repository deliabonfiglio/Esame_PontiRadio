/**
 * 
 */
package it.polito.tdp.radio.bean;

/**
 * 
 * Bean per rappresentare la tabella "ponte" del database "pontiradio"
 * 
 * @author Fulvio
 * 
 */
public class Ponte {
	
	private int idPonte ;
	private String output ;
	private String call ;
	private Double pl ;
	private String comments ;
	
	
	public Ponte(int idPonte, String output, String call, Double pl,
			String comments) {
		super();
		this.idPonte = idPonte;
		this.output = output;
		this.call = call;
		this.pl = pl;
		this.comments = comments;
	}


	public int getIdPonte() {
		return idPonte;
	}


	public void setIdPonte(int idPonte) {
		this.idPonte = idPonte;
	}


	public String getOutput() {
		return output;
	}


	public void setOutput(String output) {
		this.output = output;
	}


	public String getCall() {
		return call;
	}


	public void setCall(String call) {
		this.call = call;
	}


	public Double getPl() {
		return pl;
	}


	public void setPl(Double pl) {
		this.pl = pl;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPonte;
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
		Ponte other = (Ponte) obj;
		if (idPonte != other.idPonte)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return idPonte + ", output=" + output + ", call=" + call;
	}


	public int compareTo(Ponte p) {
		// TODO Auto-generated method stub
		return this.idPonte-p.idPonte;
	}
	
	

}
