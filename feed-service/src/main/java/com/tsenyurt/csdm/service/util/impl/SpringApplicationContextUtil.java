package com.tsenyurt.csdm.service.util.impl;

import com.tsenyurt.csdm.service.util.*;
import org.springframework.beans.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

@Component("springApplicationContextUtil")
public class SpringApplicationContextUtil
    implements ApplicationContextAware, ISpringApplicationContextUtil {

  private ApplicationContext APPLICATION_CONTEXT;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
  }

  @Override
  public Object getBean(String beanName) {
    Object bean = APPLICATION_CONTEXT.getBean(beanName);
    return bean;
  }
}
