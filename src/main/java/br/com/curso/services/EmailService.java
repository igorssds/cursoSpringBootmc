package br.com.curso.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.curso.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
