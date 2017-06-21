package com.edu.mockito;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;  
  
/**  
 * mock 测试准备类  
 *  
 * @author tiger  
 * @version 1.0.0 createTime: 14/12/27 下午8:57  
 * @see com.practice.mock.OrderHelper  
 * @since 1.6  
 */  
@Component  
public class OrderCreate {  
  
    @Autowired  
    private OrderHelper orderHelper;  
  
    public void create() {  
        System.out.println(getAmt());  
        System.out.println(orderHelper.resolve());  
    }  
  
    public int getAmt() {  
        return 10;  
    }  
}  