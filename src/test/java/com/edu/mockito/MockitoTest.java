package com.edu.mockito;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MockitoTest {
	
	@InjectMocks
    private OrderCreate orderCreate = mock(OrderCreate.class);   
    @Mock  
    private OrderHelper orderHelper;  
  
    @Before  
    public void initMocks() throws Exception {  
        MockitoAnnotations.initMocks(this);  
        doReturn(11000).when(orderCreate).getAmt();  
        doReturn("success mock").when(orderHelper).resolve();  
        doCallRealMethod().when(orderCreate).create();  
    }  
  
    @Test  
    public void create() {  
        System.out.println("start mock...");  
        orderCreate.create();  
    }  
}
