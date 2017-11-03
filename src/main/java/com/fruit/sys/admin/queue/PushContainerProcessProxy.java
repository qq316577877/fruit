package com.fruit.sys.admin.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class PushContainerProcessProxy {

	/**
	 * 日志类
	 */
	private static final Logger logger = LoggerFactory.getLogger(PushContainerProcessProxy.class);

	private JmsTemplate jmsTemplate;

	private Destination containerProcessDestination;

	public void sendMsgCon(final String message) {

		logger.info("push container process create queue , the message is {}", message);

		this.jmsTemplate.send(this.containerProcessDestination, new MessageCreator() {

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

	public Destination getContainerProcessDestination() {
		return containerProcessDestination;
	}

	public void setContainerProcessDestination(Destination containerProcessDestination) {
		this.containerProcessDestination = containerProcessDestination;
	}
}
