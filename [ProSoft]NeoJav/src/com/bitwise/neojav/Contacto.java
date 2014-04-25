package com.bitwise.neojav;

public class Contacto {

	private String apellido;
	private String cargo;
	private String dependencia;
	private int extension;
	private String indice;
	private String nombre;
	private String pbx = new String("0313208320");
	
	/**
	 * Metodo analizador de la variable Apellido
	 * @return el apellido del contacto
	 */
	public String getApellido() {
		return apellido;
	}
	
	/**
	 * Metodo modificador de la variable Apellido
	 * @param apellido el apellido a ser asignado
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	/**
	 * Metodo analizador de la variable Cargo
	 * @return el cargo del contacto
	 */
	public String getCargo() {
		return cargo;
	}
	
	/**
	 * Metodo modificador de la variable Cargo
	 * @param cargo cargo a ser asignado
 	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	/**
	 * Metodo analizador de la variable Dependencia
	 * @return la dependencia del contacto
	 */
	public String getDependencia() {
		return dependencia;
	}
	
	/**
	 * Meotodo modificador de la variable Dependencia
	 * @param dependencia variable a ser asignada
	 */
	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}
	
	/**
	 * Metodo analizador de la variable Extension
	 * @return la extension del contacto
	 */
	public int getExtension() {
		return extension;
	}
	
	/**
	 * Metodo modificador de la variable Extension
	 * @param extension variable a ser asignada
	 */
	public void setExtension(int extension) {
		this.extension = extension;
	}
	
	/**
	 * Metodo analizador de la variable Indice
	 * @return indice del contacto
	 */
	public String getIndice() {
		return indice;
	}
	
	/**
	 * Metodo modificador de la variable Indice
	 * @param indice variable a ser asignada
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	
	/**
	 * Metodo analizador de la variable Nombre
	 * @return nombre del contacto
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Metodo modificador de la variable Nombre
	 * @param nombre variable a ser asignada
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Metodo analizador de la variable PBX
	 * @return el pbx del contacto
	 */
	public String getPbx() {
		return pbx;
	}
	
	/**
	 * Metodo modificador de la variable PBX
	 * @param pbx variable a ser asignada
	 */
	public void setPbx(String pbx) {
		this.pbx = pbx;
	}	
}
