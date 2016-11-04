package com.example.newtoncradle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Test extends Activity {
    private TextViewTest mTextViewTest;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextViewTest = new TextViewTest(this);
        setContentView(mTextViewTest);
    }

    public class TextViewTest extends TextView {
        public TextViewTest(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            setText("test test ");
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            measure(0, 0);
            Log.i("Tag", "width: " + getWidth() + ",height: " + getHeight());
            Log.i("Tag", "MeasuredWidth: " + getMeasuredWidth()
                    + ",MeasuredHeight: " + getMeasuredHeight());
        }
    }
}
