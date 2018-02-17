package sa_b_2.coms309.dungeonadventure.ui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Holds the thread that runs the game
 */
public class MainThread extends Thread {
    private static final short MAX_FPS = 30;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean isRunning;


    public MainThread(SurfaceHolder holder, GamePanel gp) {
        super();
        surfaceHolder = holder;
        gamePanel = gp;
    }

    public void setRunning(boolean r) {
        isRunning = r;
    }

    @Override
    public void run() {
        long startTime, waitTime;
        long timeMillis;
        int frameCount = 0;
        long targetTime = 1000 / MAX_FPS;

        while (isRunning) {
            startTime = System.nanoTime();
            Canvas canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;

            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0)
                    sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (++frameCount == MAX_FPS)
                frameCount = 0;
        }
    }
}
