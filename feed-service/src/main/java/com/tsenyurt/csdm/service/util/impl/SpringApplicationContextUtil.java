package com.tsenyurt.csdm.service.util.impl;

import com.tsenyurt.csdm.service.util.*;
import org.springframework.beans.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

@Component("springApplicationContextUtil")
public class SpringApplicationContextUtil
    implements ApplicationContextAware, ISpringApplicationContextUtil {

  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  @Override
  public Object getBean(String beanName) {
    return context.getBean(beanName);
  }
}
