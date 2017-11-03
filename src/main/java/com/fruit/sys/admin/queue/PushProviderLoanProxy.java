package com.fruit.sys.admin.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class PushProviderLoanProxy {

	
	/**
	 * 日志类
	 */
	private static final Logger logger = LoggerFactory.getLogger(PushProviderLoanProxy.class);

	private JmsTemplate jmsTemplate;

	private Destination loanDestination;

	public void sendMsgCon(final String message) {

		logger.info("push provider loan create queue , the message is {}", message);

		this.jmsTemplate.send(this.loanDestination, new MessageCreator() {

			@SuppressWarnings("finally")
			@Override
			public Message createMessage(Session session) {
				Message msg = null;
				try {
					msg = session.createTextMessage(message);
				} catch (JMSException e) {
					logger.error("sendMsgCon:fail");
				} finally {
					return msg;
				}
			}
		});
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public Destination getLoanDestination() {
		return loanDestination;
	}

	public void setLoanDestination(Destination loanDestination) {
		this.loanDestination = loanDestination;
	}
}
