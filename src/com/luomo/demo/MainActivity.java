package com.luomo.demo;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.luomo.demo.view.AutoScrollTextView;

public class MainActivity extends Activity {
    //声明一系列的布局控件
    private AutoScrollTextView autoScrollTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_auto_scroll);
        //根据ID获取自定义AutoScrollTextView类的对象
        autoScrollTextView = (AutoScrollTextView) findViewById(R.id.TextViewNotice);
        autoScrollTextView.init(getWindowManager());
        autoScrollTextView.setOnMoveStatusListener(new AutoScrollTextView.OnMoveStatusListener() {
            @Override
            public void onMoveEnded() {
                Toast.makeText(MainActivity.this, "移到最后了", Toast.LENGTH_SHORT).show();
            }
        });
        autoScrollTextView.startScroll();
    }
}
