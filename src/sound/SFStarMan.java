package sound;

public class SFStarMan {
private static MP3 som;
	
	static {
		som = new MP3("audios/starman.mp3");
	}
	
	private SFStarMan() {}
	
	public static void play() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				som.play();
			}
		}).start();
	}
	
	public static void stop(){
		som.stop();
	}
}
