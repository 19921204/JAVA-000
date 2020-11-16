package com.geek.week_05.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class C {
    A a;

    public C(@Autowired A a) {
        this.a = a;
    }
}
