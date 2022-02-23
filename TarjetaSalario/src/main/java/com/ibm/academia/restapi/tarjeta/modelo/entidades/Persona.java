package com.ibm.academia.restapi.tarjeta.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.academia.restapi.tarjeta.enumeradores.TipoPasion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="personas", schema="tarjeta")
public class Persona implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="apellido_paterno")
	private String apellidoPaterno;
	
	@Column(name="apellido_materno")
	private String apellidoMaterno;
	
	@Column(name="edad",nullable=false)
	private Integer edad;
	
	@Column(name="salario_mensual",nullable=false)
	private BigDecimal salario;
	
	@Column(name="dni",nullable=false)
	private String dni;
	
	@Column(name = "tipo_pasion")
	@Enumerated(EnumType.STRING)
	private TipoPasion tipoPasion;

	@Column(name="usuario_creacion",nullable=false)
	private String usuarioCreacion;
	
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;
	
	@Column(name="fecha_modificacion")
	private Date fechaModificacion;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "persona_tarjeta", schema = "tarjeta",
			joinColumns = @JoinColumn(name = "persona_id"),
			inverseJoinColumns = @JoinColumn(name = "tarjeta_id")
			)
	@JsonIgnoreProperties({"personas"})
	private Set<Tarjeta> tarjetas;

	public Persona(Long id, String nombre, String apellidoPaterno, String apellidoMaterno, Integer edad,
			BigDecimal salario, String dni, TipoPasion tipoPasion, String usuarioCreacion) {
		this.id = id;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.edad = edad;
		this.salario = salario;
		this.dni = dni;
		this.tipoPasion = tipoPasion;
		this.usuarioCreacion = usuarioCreacion;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Persona [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", apellidoPaterno=");
		builder.append(apellidoPaterno);
		builder.append(", apellidoMaterno=");
		builder.append(apellidoMaterno);
		builder.append(", edad=");
		builder.append(edad);
		builder.append(", salario=");
		builder.append(salario);
		builder.append(", dni=");
		builder.append(dni);
		builder.append(", tipoPasion=");
		builder.append(tipoPasion);
		builder.append(", usuarioCreacion=");
		builder.append(usuarioCreacion);
		builder.append(", fechaCreacion=");
		builder.append(fechaCreacion);
		builder.append(", fechaModificacion=");
		builder.append(fechaModificacion);
		builder.append(", tarjetas=");
		builder.append(tarjetas);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(dni, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return Objects.equals(dni, other.dni) && Objects.equals(id, other.id);
	}

	@PrePersist
	private void antesPersistir()
	{
		this.fechaCreacion = new Date();
	}
	
	@PreUpdate
	private void antesActualizar()
	{
		this.fechaModificacion = new Date();
	}

	private static final long serialVersionUID = 5175066011614995965L;

}
