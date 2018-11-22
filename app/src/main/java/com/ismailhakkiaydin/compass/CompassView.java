package com.ismailhakkiaydin.compass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

    private float bearing;

    private Paint markerPaint;
    private Paint textPaint;
    private Paint circlePaint;

    private String northString;
    private String southString;
    private String westString;
    private String eastString;

    private int textHeight;


    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }


    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    public CompassView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }

    protected void initCompassView(){
        setFocusable(true);

        Resources resources = this.getResources();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(resources.getColor(R.color.backgroundColor));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = resources.getString(R.string.compass_north);
        eastString = resources.getString(R.string.compass_east);
        southString = resources.getString(R.string.compass_south);
        westString = resources.getString(R.string.compass_west);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(resources.getColor(R.color.textColor));

        textHeight = (int)textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.markerColor));


    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        int d = Math.min(measureWidth,measureHeight);

        setMeasuredDimension(d,d);
    }

    private int measure(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED){
            result =200;
        }else {
            result = specSize;
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int px = getMeasuredWidth() / 2 ;
        int py = getMeasuredHeight() / 2 ;
        int radius = Math.min(px,py);

        canvas.drawCircle(px, py, radius,circlePaint);
        canvas.save();
        canvas.rotate(-bearing,px,py);

        int textWidth = (int)textPaint.measureText("W");
        int cardinalX = px-textWidth/2;
        int cardinalY = py-radius+textHeight;

        for (int i=0; i<24; i++){
            canvas.drawLine(px,py-radius, px,py-radius+10, markerPaint);
            canvas.save();
            canvas.translate(0,textHeight);
            if (i % 6 == 0 ){
                String string = "";
                switch (i){
                    case(0) : {
                        string = northString;
                        int arrowY =2*textHeight;
                        canvas.drawLine(px,arrowY,px-5,25*textHeight,markerPaint);
                        break;
                    }
                    case(6) :
                        string = eastString;
                        break;
                    case(12) :
                        string = southString;
                        break;
                    case(18) :
                        string = westString;
                        break;
                }
                canvas.drawText(string,cardinalX,cardinalY,textPaint);
            }
            else if (i % 3 == 0){
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int)(px-angleTextWidth/2);
                int angleTextY = py-radius+textHeight;
                canvas.drawText(angle, angleTextX,angleTextY,textPaint);
            }
            canvas.restore();
            canvas.rotate(15,px,py);
        }
        canvas.restore();
    }



}
