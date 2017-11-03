package com.fruit.sys.admin.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class PushDebtContractProxy {

	/**
	 * 日志类
	 */
	private static final Logger logger = LoggerFactory.getLogger(PushDebtContractProxy.class);

	private JmsTemplate jmsTemplate;

	private Destination debtContractDestination;

	public void sendMsgCon(final String message) {

		logger.info("push debt contract create queue , the message is {}", message);

		this.jmsTemplate.send(this.debtContractDestination, new MessageCreator() {

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

	public Destination getDebtContractDestination() {
		return debtContractDestination;
	}

	public void setDebtContractDestination(Destination debtContractDestination) {
		this.debtContractDestination = debtContractDestination;
	}
}
