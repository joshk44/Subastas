package com.subastas.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 
 * @author Jose Ferreyra (jose.ferreyra@tallertechnologies.com)
 * 
 */

public class BarChart extends View {


    private int selectedBar = 2;
    private int pressedBar = -1;
    private Bar[] bars;
    private int barSelectedColor;
    private boolean barsRectProcessed;
    private int barIdleColor;
    private float pointX = 0;
    private float pointY = 0;

    private int fontSize = 25;
    private int barWidth = 50;

    private int barFadeIn = 0;
    ValueAnimator fadeInAnimation;

    float maxValue;

    // porcentual chart height
    private float porcentualChartHeight = (float) 0.70;

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BarChart(Context context, int barIdle, int barSelected, float[] values) {
        super(context);
        this.barIdleColor = barIdle;
        this.barSelectedColor = barSelected;
        bars = new Bar[values.length];

        for (int i = 0; i < values.length; i++) {

            bars[i] = new Bar();
            bars[i].value = values[i];
        }
        maxValue = getMaxValue(values);
        barsRectProcessed = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        calculateBarsRect(canvas);
        // draw the bars.
        drawBottomControls(canvas);
        for (int i = 0; i < bars.length; i++) {
            drawBar(bars[i], canvas, ((pressedBar != -1) && (pressedBar == i)) ? BarStyle.PRESSED : ((i == selectedBar) ? BarStyle.SELECTED
                    : BarStyle.IDLE));
        }
        //drawBottomControls(canvas);
    }

    public void calculateBarsRect(Canvas canvas) {
        if (!barsRectProcessed) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            int chartHeight = (int) ((float) canvasHeight * porcentualChartHeight);

            for (int i = 0; i < bars.length; i++) {
                float left = this.getPaddingLeft() + ((canvasWidth / bars.length) / 4)
                        + ((canvasWidth - (this.getPaddingLeft() + this.getPaddingRight())) / bars.length) * i;
                float right = left + barWidth;
                float top = chartHeight - ((bars[i].value * chartHeight) / maxValue) + this.getPaddingTop();
                float bottom = top + ((bars[i].value * chartHeight) / maxValue);
                bars[i].rect = new RectF(left, top, right, bottom);
            }
            barsRectProcessed = true;
            fadeInAnimation = ObjectAnimator.ofInt(this, "barFadeIn", 100, 0);
            fadeInAnimation.setInterpolator(new AccelerateInterpolator());
            fadeInAnimation.setDuration(1000).start();
        }
    }

    public float getMaxValue(float[] values) {
        float maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxValue)
                maxValue = values[i];
        }
        return maxValue;
    }

    public void drawBar(Bar bar, Canvas canvas, BarStyle style)
    {
        RectF rect = new RectF(bar.rect);
        canvas.clipRect(new Rect( this.getPaddingLeft(), this.getPaddingTop(), canvas.getWidth() + this.getPaddingRight(),
                (int) bars[0].rect.bottom + this.getPaddingTop()), Region.Op.REPLACE);
        int maxHeight = (int) (((int) canvas.getHeight() * porcentualChartHeight) + this.getPaddingTop());
        int topBar = (int) (rect.bottom - (maxHeight * barFadeIn) / 100);

        if (topBar <= (rect.bottom - rect.top))
            rect.top += (rect.bottom - rect.top) - topBar;

        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setColor(Color.BLACK);
        fontPaint.setTextSize(fontSize);
        fontPaint.setTextAlign(Paint.Align.LEFT);
        String barValueText = "$" + (String.valueOf(bar.value * (100 - barFadeIn) / 100));
        float textSize = fontPaint.measureText(barValueText);
        canvas.drawText(barValueText, rect.left - ((textSize - barWidth) / 2), rect.top + this.getPaddingTop() + fontSize / 2, fontPaint);
        rect.top += fontSize + this.getPaddingTop();

        switch (style) {
        case IDLE:
            barPaint.setStyle(Paint.Style.FILL);
            barPaint.setColor(barIdleColor);
            canvas.drawRoundRect(rect, 10, 10, barPaint);
            break;
        case SELECTED:
            barPaint.setStyle(Paint.Style.FILL);
            barPaint.setColor(barSelectedColor);
            canvas.drawRoundRect(rect, 4, 4, barPaint);
            barPaint.setStyle(Paint.Style.STROKE);
            barPaint.setColor(barIdleColor);
            barPaint.setStrokeWidth(4);
            canvas.drawRoundRect(rect, 10, 10, barPaint);
            break;
        case PRESSED:
            barPaint.setStyle(Paint.Style.FILL);
            barPaint.setColor(Color.RED);
            rect.left -= 3;
            rect.top -= 3;
            rect.right += 3;
            canvas.drawRoundRect(rect, 10, 10, barPaint);
            break;
        }
        canvas.restore();
    }

    public synchronized void drawBottomControls(Canvas canvas) {
        canvas.clipRect(new Rect(this.getPaddingLeft(), (int) bars[0].rect.bottom - 2, this.getRight() - this.getPaddingRight(), this.getBottom()),
                Region.Op.REPLACE);
        barPaint.setColor(Color.BLACK);
        barPaint.setStrokeWidth(2);
        canvas.drawLine(this.getPaddingLeft(), (int) ((float) this.getHeight() * porcentualChartHeight) + this.getPaddingTop(), canvas.getWidth()
                - (float) this.getPaddingRight(), (int) ((float) this.getHeight() * porcentualChartHeight) + this.getPaddingTop(), barPaint);

        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setColor(barIdleColor);
        fontPaint.setTextSize(fontSize);
        fontPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < bars.length; i++) {
            if (i == selectedBar) {
                fontPaint.setColor(Color.BLACK);
                Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                trianglePaint.setColor(barIdleColor);
            }
            else {
                fontPaint.setColor(barIdleColor);
            }
            float textSize = fontPaint.measureText("10-03");
            canvas.drawText("" + (10 + i) + "-03", bars[i].rect.left - ((textSize -
                    barWidth) / 2),
                    (int) ((float) this.getHeight() * porcentualChartHeight) +
                            this.getPaddingTop()
                            + (((float) this.getHeight() * (1 - porcentualChartHeight)) / 2),
                    fontPaint);

        }
        this.drawTriangle(canvas);
        canvas.drawLine(this.getPaddingLeft(), (int) (float) this.getHeight() - this.getPaddingBottom(),
                canvas.getWidth() - (float) this.getPaddingRight(), (int) (float) this.getHeight() - this.getPaddingBottom(), barPaint);
        canvas.restore();
    }

    public void drawTriangle(Canvas canvas) {
        canvas.clipRect(new Rect(this.getPaddingLeft(), (int) bars[0].rect.bottom - 2, this.getRight() - this.getPaddingRight(), this.getBottom()),
                Region.Op.REPLACE);
        Paint paint = new Paint();
        paint.setColor(barIdleColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);

        Point a = new Point((int) bars[selectedBar].rect.left + ((barWidth / 2) - (barWidth / 3)), (int) bars[selectedBar].rect.bottom);
        Point b = new Point((int) bars[selectedBar].rect.left + ((barWidth / 2) + (barWidth / 3)), (int) bars[selectedBar].rect.bottom);
        Point c = new Point((int) bars[selectedBar].rect.left + (barWidth / 2), (int) bars[selectedBar].rect.bottom + (barWidth / 3));

        Path pathTop = new Path();
        pathTop.setFillType(FillType.EVEN_ODD);
        pathTop.moveTo(a.x, a.y);
        pathTop.lineTo(b.x, b.y);
        pathTop.lineTo(c.x, c.y);
        pathTop.lineTo(a.x, a.y);
        pathTop.close();
        canvas.drawPath(pathTop, paint);

        Path patha = new Path();

        a = new Point((int) bars[selectedBar].rect.left + ((barWidth / 2) - (barWidth / 3)),
                ((int) (float) this.getHeight() - this.getPaddingBottom()));
        b = new Point((int) bars[selectedBar].rect.left + ((barWidth / 2) + (barWidth / 3)),
                ((int) (float) this.getHeight() - this.getPaddingBottom()));
        c = new Point((int) bars[selectedBar].rect.left + (barWidth / 2),
                ((int) (float) this.getHeight() - this.getPaddingBottom()) - (barWidth / 3));
        
        Path pathBottom = new Path();
        pathBottom.setFillType(FillType.EVEN_ODD);
        pathBottom.moveTo(a.x, a.y);
        pathBottom.lineTo(b.x, b.y);
        pathBottom.lineTo(c.x, c.y);
        pathBottom.lineTo(a.x, a.y);
       
        pathBottom.close();
        canvas.drawPath(pathBottom, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointX = event.getX();
        pointY = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            for (int i = 0; i < bars.length; i++) {
                if (bars[i].rect.contains(pointX, pointY)) {
                    pressedBar = i;
                    break;
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            for (int i = 0; i < bars.length; i++) {
                if (bars[i].rect.contains(pointX, pointY)) {
                    selectedBar = i;
                    break;
                }
            }
            pressedBar = -1;
            break;
        }
        this.invalidate();
        return true;
    }

    public int getBarFadeIn() {
        return barFadeIn;
    }

    public void setBarFadeIn(int barFadeIn) {
        this.barFadeIn = barFadeIn;
        this.invalidate();
    }

}

enum BarStyle {
    SELECTED,
    IDLE,
    PRESSED
}

class Bar {


    RectF rect;
    float value;

    public Bar() {
        rect = new RectF();
        value = 0;
    }

}