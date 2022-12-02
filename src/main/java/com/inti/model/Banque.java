package com.inti.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "BanqueProjet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banque {
	
	@Id
	public long idBanque;
	public String nomBanque;
	public String adresse;

	@OneToMany (cascade = CascadeType.ALL, mappedBy = "banque")
	private List<Compte> listeCompte;
}
