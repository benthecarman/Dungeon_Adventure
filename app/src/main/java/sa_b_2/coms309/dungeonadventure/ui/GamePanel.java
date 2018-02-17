package sa_b_2.coms309.dungeonadventure.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneManager;

/**
 * Runs the MainThread, also handles the SceneManager which dictates what is drawn and also handles touch events
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mainThread;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(), this);
        Constants.sceneManager = new SceneManager();
        setFocusable(true);
        update();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mainThread = new MainThread(getHolder(), this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constants.sceneManager.receiveTouch(event);
        return true;
    }

    public void update() {
        Constants.sceneManager.update();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Constants.sceneManager.draw(canvas);
    }
}
