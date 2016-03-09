package com.edu.disruptor;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {  
    private byte[] packet;  
  
    public byte[] getValue()  
    {  
        return packet;  
    }  
  
    public void setValue(final byte[] packet)  
    {  
        this.packet = packet;  
    }  
  
    public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>()  
    {  
        public ValueEvent newInstance()  
        {  
            return new ValueEvent();  
        }  
    };  
}  