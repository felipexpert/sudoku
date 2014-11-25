package sound;

public class SFWorldMap1GrassLand {
private static MP3 som;
	
	static {
		som = new MP3("audios/worldMap1GrassLand.mp3");
	}
	
	private SFWorldMap1GrassLand() {}
	
	public static void play() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				som.playContinuo();
			}
		}).start();
	}
	
	public static void stop(){
		som.stop();
	}
}
