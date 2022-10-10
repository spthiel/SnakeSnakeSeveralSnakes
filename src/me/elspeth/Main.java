package me.elspeth;

public class Main {
	
	public static void main(String[] args) {
	
		Handler handler = new Handler();
		
		Thread drawLoop = new Thread(() -> limiter(Config.MAXFPS, handler::repaint, "FPS: "));
		Thread updateLoop = new Thread(() -> limiter(Config.MAXUPS, handler::update, "UPS: "));
		
		drawLoop.start();
		updateLoop.start();
	}
	
	private static void limiter(int averageCalls, Runnable callback, String logname) {
		
		float timeperframe = 1000.0f/averageCalls;
		long current;
		long last = System.currentTimeMillis();
		float delta;
		float timer = 0;
		
		long lastLog = System.currentTimeMillis();
		long calls = 0;
		
		while(true) {
			
			current = System.currentTimeMillis();
			delta = current - last;
			last = current;
			timer += delta;
			
			if(timer >= timeperframe) {
				callback.run();
				timer -= timeperframe;
				calls++;
			}
			
			if (System.currentTimeMillis() - lastLog > 1000) {
				calls = 0;
				lastLog = System.currentTimeMillis();
			}
		}
	}
}