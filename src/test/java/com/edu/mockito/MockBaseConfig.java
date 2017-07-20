package com.edu.mockito;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ApplicationObjectSupport;

@Configuration
@ComponentScan({ "com.edu.mockito" })
public class MockBaseConfig extends ApplicationObjectSupport {

}
