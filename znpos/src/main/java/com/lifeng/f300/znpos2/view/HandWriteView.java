package com.lifeng.f300.znpos2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lifeng.f300.znpos2.R;

/**
 * Created by happen on 2017/9/5.
 */

public class HandWriteView extends RelativeLayout {

    private WriteView writeView;
    public HandWriteView(Context context) {
        this(context, null);
    }

    public HandWriteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandWriteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.hand_writing_layout, this, false);

        writeView = new WriteView(getContext());

        FrameLayout writeLayout = (FrameLayout) rootView.findViewById(R.id.hand_writing_layout);
        writeLayout.addView(writeView);

        addView(rootView);
    }
    /**
     * 设置签名笔迹宽度
     *
     * @param width
     */
    public void setStrokeWidth(int width) {
        writeView.setStrokeWidth(width);
    }

    /**
     * 清除签名
     */
    public void clear() {
        if (null != writeView){
            writeView.clear();
            writeView.touchX=0;
            writeView.touchY=0;
        }
    }

    /**
     * 获取手写的电子签名
     *
     * @return
     */
    public Bitmap getWriteBitmap() {
        return writeView.touchX==0 && writeView.touchY==0 ? null:writeView.getCacheBitmap();
    }

    /**
     * Describe : 手写绘制视图<br/>
     * Date : 2015年9月7日下午3:42:18 <br/>
     * Version : 1.0 <br/>
     *
     * @author XiongWei
     */
    class WriteView extends View {
        static final int BACKGROUND_COLOR = Color.WHITE;
        static final int BRUSH_COLOR = Color.BLACK;
        static final int DEFAULT_STROKE_WIDTH = 5;

        private int strokeWidth = DEFAULT_STROKE_WIDTH;
        private Paint paint;
        private Canvas cacheCanvas;
        private Bitmap cachebBitmap;
        private Path path;

        private int bitmapWidth;
        private int bitmapHeight;

        public WriteView(Context context) {
            this(context, null);
        }

        public WriteView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public WriteView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            path = new Path();
        }

        public void initCache() {
            cachebBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            cacheCanvas = new Canvas(cachebBitmap);
            cacheCanvas.drawColor(Color.WHITE);
//			cacheCanvas.drawColor(0xFFF9F9F9); //背景颜色灰色
        }

        public void setStrokeWidth(int width) {
            paint.setStrokeWidth(width);
        }

        public Bitmap getCacheBitmap() {
            return cachebBitmap;
        }

        public void clear() {
            if (null != cacheCanvas) {
                paint.setColor(BACKGROUND_COLOR);
                cacheCanvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                cacheCanvas.drawColor(Color.WHITE);
                path.reset();
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            bitmapHeight = h;
            bitmapWidth = w;
            initCache();
        }

        private float touchX;
        private float touchY;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    touchX = x;
                    touchY = y;
                    path.moveTo(x, y);
                    break;

                case MotionEvent.ACTION_MOVE:
                    path.quadTo(touchX, touchY, x, y);
                    touchX = x;
                    touchY = y;
                    break;

                case MotionEvent.ACTION_UP:
                    cacheCanvas.drawPath(path, paint);
                    break;

                default:
                    break;
            }

            invalidate();
            return true;
        }
    }
}
