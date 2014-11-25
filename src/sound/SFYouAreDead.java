package sound;

public class SFYouAreDead {
	private static MP3 som;
	
	static {
		som = new MP3("audios/youAreDead.mp3");
	}
	
	private SFYouAreDead() {}
	
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
