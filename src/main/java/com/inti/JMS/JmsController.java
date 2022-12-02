package com.inti.JMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class JmsController {


	
	public void sendNameFE(String name ,String banque) {
		jmsTemplate.convertAndSend(banque, name);
			}
}