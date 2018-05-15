package jiyang.cdu.kits.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import jiyang.cdu.kits.R;

public class CharAvatarView extends android.support.v7.widget.AppCompatImageView {
    public CharAvatarView(Context context) {
        this(context, null);
    }

    public CharAvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CharAvatarView);
        backColor = typedArray.getColor(R.styleable.CharAvatarView_backColor, Color.BLUE);
        contentColor = typedArray.getColor(R.styleable.CharAvatarView_contentColor, Color.WHITE);
        content = typedArray.getString(R.styleable.CharAvatarView_avatarContent);
        typedArray.recycle();
        init();
    }

    private String content;
    private Paint paintBackground;
    private Paint paintContent;
    private Rect mRect;
    private int backColor, contentColor;

    private void init() {
        paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintContent = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
        if (content == null) {
            content = " ";
        }
        content = content.substring(0, 1).toUpperCase();
    }

    public void setContent(String content) {
        if (content == null) {
            content = " ";
        }
        this.content = content.substring(0, 1).toUpperCase();
        invalidate();
    }

    public void setBackColor(int color) {
        this.backColor = color;
        invalidate();
    }

    public void setContentColor(int color) {
        this.contentColor = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (content == null) {
            return;
        }

        paintBackground.setColor(this.backColor);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paintBackground);

        paintContent.setColor(this.contentColor);
        paintContent.setTextSize(getWidth() / 2);
        paintContent.getTextBounds(content, 0, 1, mRect);

        Paint.FontMetricsInt fontMetricsInt = paintContent.getFontMetricsInt();
        int baseLine = (getMeasuredHeight() - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

        paintContent.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, getWidth() / 2, baseLine, paintContent);
    }
}
