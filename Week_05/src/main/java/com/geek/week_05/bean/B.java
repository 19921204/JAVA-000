package com.geek.week_05.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class B {
    C c;

    @Autowired
    public void setC(C c) {
        this.c = c;
    }
}
