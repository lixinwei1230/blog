package me.qyh.helper.excutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

@Component
public class ExcutorManagerImpl implements ExcutorManager {

	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void excute(Runnable runnable) throws InterruptedException {
		executorService.submit(runnable);
	}

	@Override
	public void excuteInForeground(Runnable runnable) throws InterruptedException {
		Future<?> ft = executorService.submit(runnable);
		while(!ft.isDone()){
			Thread.sleep(500);
		}
	}

}
