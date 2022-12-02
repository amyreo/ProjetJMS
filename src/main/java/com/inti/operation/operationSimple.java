package com.inti.operation;

import com.inti.model.Compte;

public class operationSimple {

	public boolean retrait(Compte compte, double montant) {
		if (compte.getDecouvertMax() > montant && compte.getPlafondRetrait() > montant) {
			compte.setSolde(compte.getSolde() - montant);
			System.out.println("le compte " + compte.getNumCompte() + " a ete debité de " + montant);
			return true;
		} else if (compte.getPlafondRetrait() < montant) {
			System.out.println("le montant max du retrait a été depassé");
			return false;
		} else {
			System.out.println("le montant max du decouvert a été depassé");
			return false;
		}
	}
	public boolean depot(Compte compte, double montant) {
		if (compte.getPlafondDepot() > montant) {
			compte.setSolde(compte.getSolde() + montant);
			System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant);
			return true;
		} else {
			System.out.println("le montant max du depot a été depassé");
			return false;
		}
	}
		public boolean virement(Compte compte, double montant) {
			if (compte.getPlafondDepot() > montant) {
				compte.setSolde(compte.getSolde() + montant);
				System.out.println("le compte " + compte.getNumCompte() + " a été augmenté de " + montant);
				return true;
			} else {
				System.out.println("le montant max du depot a été depassé");
				return false;
			}
		
	}
	
}
