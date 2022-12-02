package com.inti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.inti.model.Compte;
import com.inti.operation.OperationSimpleController;
import com.inti.service.ICompteRepository;

@Component
public class MessageReceiverService {

	@Autowired
	ICompteRepository icr;

	@Autowired
	OperationSimpleController osc;

	@JmsListener(destination = "PROJETJMS")
	public boolean listener(String infos) {
		String[] infosArray = infos.split("/");
		String verif = infosArray[0];
		Long numEnvoi = Long.parseLong(infosArray[1]);
		Long numDestinataire = Long.parseLong(infosArray[2]);
		Double montant = Double.parseDouble(infosArray[3]);
		if (verif == "true") {
			Compte destinataire = icr.findById(numDestinataire).get();
			boolean verifDestinataire = destinataire.autoriseDepot(montant);
			osc.virement(numEnvoi, numDestinataire, montant);
			return verifDestinataire;
		} else {
			return false;
		}

	}
}

// "true|123456|231456|1000"