package com.edu.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

@Component
public class ZkWatcher implements Watcher{

	@Override
	public void process(WatchedEvent event) {
		System.out.println("触发了事件type:"+ event.getType()+",state:"+event.getState());
	}

}
