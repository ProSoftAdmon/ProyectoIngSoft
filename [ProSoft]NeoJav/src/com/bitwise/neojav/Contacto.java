package com.bitwise.neojav;

public class Contacto {

	private String apellido;
	private String cargo;
	private String dependencia;
	private int extension;
	private String indice;
	private String nombre;
	private String pbx = new String("0313208320");
	
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getDependencia() {
		return dependencia;
	}
	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}
	public int getExtension() {
		return extension;
	}
	public void setExtension(int extension) {
		this.extension = extension;
	}
	public String getIndice() {
		return indice;
	}
	public void setIndice(String indice) {
		this.indice = indice;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPbx() {
		return pbx;
	}
	public void setPbx(String pbx) {
		this.pbx = pbx;
	}	
}
