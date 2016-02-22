# TextScrollDemo
TextView跑马灯效果
[toc]
##工程目录
![这里写图片描述](http://img.blog.csdn.net/20160222123206522)
<br/>AutoScrollTextView自定义的 TextView
##主要文件
AutoScrollTextView.java
```java
public class AutoScrollTextView extends TextView{
    private Paint paint = null;//绘图样式

    private float moveSpeed = 2f;//移动的速度,速度设置：1-10
    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;//AutoScrollTextView控件的宽度
    private float step = 0f, y = 0f;//文字的横坐标;文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//TextView的长度+TextView文本的长度
    private float temp_view_plus_two_text_length = 0.0f;//TextView的长度+TextView文本的长度*2

    private boolean isStarting = false;//是否开始滚动

    private String text = "";//文本内容

    private OnMoveStatusListener onMoveStatusListener = null;

    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(WindowManager windowManager) {
        paint = getPaint();
        paint.setColor(this.getCurrentTextColor());//文本颜色

        text = getText().toString();
        textLength = paint.measureText(text); //measure()方法获取text的长度
        viewWidth = getWidth();
        if (viewWidth == 0 && windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            viewWidth = display.getWidth();
        }
        step = viewWidth + textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        y = getTextSize() + getPaddingTop();
    }

    /**
     * 覆写TextView的onDraw()方法，实现文本滚动显示的效果
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        /**
         * Draw the text, with origin at (x,y), using the specified paint. The
         * origin is interpreted based on the Align setting in the paint.
         * @param text  The text to be drawn  要显示的文本
         * @param x     The step-coordinate of the origin of the text being drawn  文本显示的x坐标，TextView的最左端的x坐标是0，最右端的x坐标是TextView.getWidth();
         * @param y     The y-coordinate of the origin of the text being drawn  文本显示的y坐标，TextView的顶端所在的y坐标。
         * @param paint The paint used for the text (e.g. color, size, style)  描绘文本的画笔
         */
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }
        step += moveSpeed;//速度设置：1-10
        if (step > temp_view_plus_two_text_length) {
            step = textLength;
            if(null != onMoveStatusListener){//有文字移动到最后的监听
                onMoveStatusListener.onMoveEnded();
                stopScroll();
            }
        }
        invalidate();
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        isStarting = true;
        invalidate();
    }

    /**
     * 停止滚动
     */
    public void stopScroll() {
        isStarting = false;
        invalidate();
    }

    /**
     * 设置移动到最后的监听类
     * @param onMoveStatusListener
     */
    public void setOnMoveStatusListener(OnMoveStatusListener onMoveStatusListener) {
        this.onMoveStatusListener = onMoveStatusListener;
    }

    /**
     * 文字滑动的监听类
     */
    public interface OnMoveStatusListener {
        /**
         * 最后一个文字移出控件区域
         */
        public void onMoveEnded();
    }
}
```
|字段|释义|
|:---|:---|
| moveSpeed |移动速度：1-10 |
|paint.setColor(this.getCurrentTextColor());//文本颜色|控件中设置的textColor的值<br/>如果用了SpannableStringBuilder则需要修改init（）方法中的代码|
如果需要循环指定次数，则需要添加一个控制的变量。实现比较简单这里就不写了


MainActivity.java
```java
public class MainActivity extends Activity {
    //声明一系列的布局控件
    private AutoScrollTextView autoScrollTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
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
```
autoScrollTextView.setOnMoveStatusListener不设置监听，则无限次循环;设置监听则只滚动一次
也可代码调用，
```
autoScrollTextView.startScroll();
autoScrollTextView.stopScroll();
```
实现开始，停止滚动
##运行效果
![这里写图片描述](http://img.blog.csdn.net/20160222130820123)
