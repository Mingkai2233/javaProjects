package org.example.ioc_05;

import org.springframework.beans.factory.FactoryBean;

public class ComponentFactoryBean implements FactoryBean<Component> {
    private String tmp;

    @Override
    public Component getObject() {
        Component component = new Component(tmp);
        return component;
    }

    @Override
    public Class<?> getObjectType() {
        return Component.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
    public String getTmp() {
        return tmp;
    }
}
