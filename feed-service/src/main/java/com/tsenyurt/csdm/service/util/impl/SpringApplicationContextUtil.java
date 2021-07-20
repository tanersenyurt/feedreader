package com.tsenyurt.csdm.service.util.impl;

import com.tsenyurt.csdm.service.util.ISpringApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component("springApplicationContextUtil")
public class SpringApplicationContextUtil
    implements ApplicationContextAware, ISpringApplicationContextUtil {

  private static ApplicationContext APPLICATION_CONTEXT_STATIC;
  private ApplicationContext APPLICATION_CONTEXT;

  public static Object getBeanByStatic(String beanName) {
    Object bean = APPLICATION_CONTEXT_STATIC.getBean(beanName);
    return bean;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
    APPLICATION_CONTEXT_STATIC = applicationContext;
  }

  @Override
  public Object getBean(String beanName) {
    Object bean = APPLICATION_CONTEXT.getBean(beanName);
    return bean;
  }

  @Override
  public <T> T getBean(Class clazz) {
    return (T) APPLICATION_CONTEXT.getBean(clazz);
  }

  @Override
  public <T> T getBean(String qualifier, Class<T> clazz) {
    return APPLICATION_CONTEXT.getBean(qualifier, clazz);
  }

  @Override
  @SuppressWarnings("unchecked")
  public String getBeanNameFromType(Class clazz) {
    String[] names = APPLICATION_CONTEXT.getBeanNamesForType(clazz);
    if (names != null && names.length > 0) {
      return names[0];
    } else {
      return StringUtils.EMPTY;
    }
  }

  @Override
  public String resolveValue(String key) {
    String _key = "${" + key + "}";
    final ConfigurableListableBeanFactory factory =
        ((ConfigurableApplicationContext) APPLICATION_CONTEXT).getBeanFactory();
    return factory.resolveEmbeddedValue(_key);
  }

  @Override
  public ApplicationContext getAPPLICATION_CONTEXT() {
    return this.APPLICATION_CONTEXT;
  }
}
