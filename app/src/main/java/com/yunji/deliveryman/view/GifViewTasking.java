package com.yunji.deliveryman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.yunji.deliveryman.R;

import java.io.InputStream;


public class GifViewTasking extends View {

    private Movie mMovie;
    private long movieStart;
    private int width;
    private int height;
    private boolean isStart = false;

    public GifViewTasking(Context context) {
        super(context);
        initializeView();
    }

    public GifViewTasking(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public GifViewTasking(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }

    private void initializeView() {
        //初始化gif图片资源
        InputStream is = getContext().getResources().openRawResource(R.raw.smile);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMovie = Movie.decodeStream(is);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = (int) now;
        }
        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, width / 2 - mMovie.width() / 2, 0);
            if (isStart) {
                this.invalidate();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            //MeasureSpec.EXACTLY表示该view设置的确切的数值
            width = widthSize;
            height = heightSize;
        }
    }

    public void startAnimation() {
        isStart = true;
        postInvalidate();
    }

    public void stopAnimation() {
        isStart = false;
        postInvalidate();
    }

}
