package com.inti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.inti.model.Compte;
import com.inti.operation.OperationSimpleController;
import com.inti.service.ICompteRepository;

@Component
public class MessageReceiverService {

	private static final String MESSAGE_QUEUE = "PROJETJMS";
	
	@Autowired
	ICompteRepository icr;

	@Autowired
	OperationSimpleController osc;

	@JmsListener(destination = MESSAGE_QUEUE)
	public void listener(String verif) {
		System.out.println(verif);
		String[] infosArray = verif.split("/");
		String verif1 = infosArray[0];
		Long numEnvoi = Long.parseLong(infosArray[1]);
		Long numDestinataire = Long.parseLong(infosArray[2]);
		Double montant = Double.parseDouble(infosArray[3]);
		if (verif1.equals("true")) {
			Compte destinataire = icr.findById(numDestinataire).get();
			boolean verifDestinataire = destinataire.autoriseDepot(montant);
			osc.virement(numEnvoi, numDestinataire, montant);
			System.out.println("CA MARCHE");
		} else {
			System.out.println("CA MARCHE PO");
		}

	}
}

// "true|123456|231456|1000"