package com.edu.jdk7;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * 监听文件系统的更改
 * @author jy
 */
public class TestWatcherService {
	private WatchService watcher;

	public TestWatcherService(Path path) throws IOException {
		watcher = FileSystems.getDefault().newWatchService();
		path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	}

	public void handleEvents() throws InterruptedException {
		while (true) {
			WatchKey key = watcher.take();
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				if (kind == StandardWatchEventKinds.OVERFLOW) {// 事件可能lost or discarded
					continue;
				}
				WatchEvent<Path> e = (WatchEvent<Path>) event;
				Path fileName = e.context();

				System.out.printf("Event %s has happened,which fileName is %s%n", kind.name(), fileName);
			}
			if (!key.reset()) {
				break;
			}
		}
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		String path = System.getProperty("user.dir") + File.separator + "file";
		System.out.println(path);
		new TestWatcherService(Paths.get(path)).handleEvents();
	}
}
