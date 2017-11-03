package com.fruit.sys.admin.service.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.model.order.OrderProcessResponse;

@Service
public class OrderProcessFactory implements InitializingBean, ApplicationContextAware {

	private Map<OrderEventEnum, IOrderProcessor> processors;

    private ApplicationContext applicationContext;
    
    @Autowired
    @Qualifier("defaultOrderProcessor")
    private IOrderProcessor defaultProcessor;
	
    public OrderProcessResponse process(OrderProcessRequest request){
        OrderEventEnum event = request.getEvent();
        IOrderProcessor orderProcessor = this.processors.get(event);
        if(orderProcessor == null){
            orderProcessor = this.defaultProcessor;
        }
        return orderProcessor.process(request);
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, IOrderProcessor> processorMap = this.applicationContext.getBeansOfType(IOrderProcessor.class);
        this.processors = new HashMap<OrderEventEnum, IOrderProcessor>();
        for(IOrderProcessor orderProcessor: processorMap.values()){
            this.processors.put(orderProcessor.getEvent(), orderProcessor);
        }

	}

}
