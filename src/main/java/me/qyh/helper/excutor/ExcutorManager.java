package me.qyh.helper.excutor;

public interface ExcutorManager {

	public void excute(Runnable runnable) throws InterruptedException;

	public void excuteInForeground(Runnable runnable) throws InterruptedException;

}
