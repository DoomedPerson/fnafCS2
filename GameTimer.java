import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private Timer timer;
    private int secondsRemaining;

    public GameTimer(int initialSeconds) {
        this.secondsRemaining = initialSeconds;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    System.out.println("Time remaining: " + secondsRemaining);
                } else {
                    timer.cancel();
                    System.out.println("Game over!");
                }
            }
        }, 0, 1000); // 0 delay, 1000ms (1 second) interval
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            System.out.println("Timer stopped.");
        }
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }


    public static void main(String[] args) {
        GameTimer gameTimer = new GameTimer(10); // 90 seconds
        gameTimer.start();
    }
}
