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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.academia.restapi.tarjeta.enumeradores.TipoPasion;
import com.ibm.academia.restapi.tarjeta.enumeradores.TipoTarjeta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="tarjetas", schema="tarjeta")
public class Tarjeta implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nombre", nullable=false)
	private String nombre;
	
	@Column(name = "tipo_tarjeta", nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoTarjeta tipoTarjeta;
	
	@Column(name = "tipo_pasion", nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoPasion tipoPasion;
	
	@Column(name="salario_mensual_minimo",nullable=false)
	private BigDecimal salarioMinimo;
	
	@Column(name="salario_mensual_maximo")
	private BigDecimal salarioMaximo;
	
	@Column(name="edad_minima",nullable=false)
	private Integer edadMinima;

	@Column(name="edad_maxima",nullable=false)
	private Integer edadMaxima;
	
	@Column(name="usuario_creacion",nullable=false)
	private String usuarioCreacion;
	
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;
	
	@Column(name="fecha_modificacion")
	private Date fechaModificacion;
	
	@ManyToMany(mappedBy = "tarjetas", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"tarjetas"})
	private Set<Persona> personas;

	public Tarjeta(Long id, String nombre, TipoTarjeta tipoTarjeta, TipoPasion tipoPasion, BigDecimal salarioMinimo,
			BigDecimal salarioMaximo, Integer edadMinima, Integer edadMaxima, String usuarioCreacion) {
		this.id = id;
		this.nombre = nombre;
		this.tipoTarjeta = tipoTarjeta;
		this.tipoPasion = tipoPasion;
		this.salarioMinimo = salarioMinimo;
		this.salarioMaximo = salarioMaximo;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
		this.usuarioCreacion = usuarioCreacion;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tarjeta [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", tipoTarjeta=");
		builder.append(tipoTarjeta);
		builder.append(", tipoPasion=");
		builder.append(tipoPasion);
		builder.append(", salarioMinimo=");
		builder.append(salarioMinimo);
		builder.append(", salarioMaximo=");
		builder.append(salarioMaximo);
		builder.append(", edadMinima=");
		builder.append(edadMinima);
		builder.append(", edadMaxima=");
		builder.append(edadMaxima);
		builder.append(", usuarioCreacion=");
		builder.append(usuarioCreacion);
		builder.append(", fechaCreacion=");
		builder.append(fechaCreacion);
		builder.append(", fechaModificacion=");
		builder.append(fechaModificacion);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tarjeta other = (Tarjeta) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
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

	private static final long serialVersionUID = -5711728042674522149L;

}
