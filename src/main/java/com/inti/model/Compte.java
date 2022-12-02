package com.inti.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
	
	@Id
	private long numCompte;
	private String nomProprio;
	private double plafondDepot;
	private double plafondRetrait;
	private double solde;
	private double aggio;
	private double decouvertMax;
	
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn (name = "idBanque")
	private Banque banque;
	
	
	
	

}
