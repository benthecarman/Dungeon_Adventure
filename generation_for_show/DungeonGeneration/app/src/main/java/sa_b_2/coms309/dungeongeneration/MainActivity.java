package sa_b_2.coms309.dungeongeneration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    public class MyView extends View {
        public final int MAXAREA = (int)(getHeight()*getWidth()*0.6);

        public MyView(Context context){
            super (context);
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#29843f"));
            Random rand = new Random(System.currentTimeMillis());
            int count = 0;
            int area = 0;
            while (count <= 10 || area <= MAXAREA) {
                int width = rand.nextInt(150) + 20;
                int height = rand.nextInt(150) + 20;
                int x2 = rand.nextInt(getWidth()-width-70) + width + 50;
                int y2 = rand.nextInt(getHeight()-height-70) + height + 50;
                int x1 = x2 - width;
                int y1 = y2 - height;
                canvas.drawRect(x1, y1, x2, y2, paint);
                count++;
                area = area+width*height;
            }

            paint.setColor(Color.parseColor("#e9f4b7"));
            canvas.drawRect(1, getHeight() / 2 - 15, 31, getHeight() / 2 + 15, paint);
            canvas.drawRect(getWidth()/2-15, getHeight()-31, getWidth()/2+15, getHeight()-1, paint);
            canvas.drawRect(getWidth()-31, getHeight() / 2 - 15, getWidth()-1, getHeight() / 2 + 15, paint);
            canvas.drawRect(getWidth()/2-15, 1, getWidth()/2+15, 31, paint);
        }
    }
}
