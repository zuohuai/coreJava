package com.edu.mockito;
import org.springframework.stereotype.Component;  
  
/**  
 * mock 测试准备类  
 *  
 * @author tiger  
 * @version 1.0.0 createTime: 14/12/27 下午8:59  
 * @since 1.6  
 */  
@Component  
public class OrderHelper {  
  
    public String resolve() {  
        return "resolve order";  
    }  
}  