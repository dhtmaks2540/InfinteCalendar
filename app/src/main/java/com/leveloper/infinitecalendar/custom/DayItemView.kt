package com.leveloper.infinitecalendar.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.leveloper.infinitecalendar.R
import com.leveloper.infinitecalendar.utils.CalendarUtils.Companion.getDateColor
import com.leveloper.infinitecalendar.utils.CalendarUtils.Companion.isSameMonth
import org.joda.time.DateTime

// 커스텀뷰(View를 상속)
// JvmOverloads를 사용하여 생성자 파라미터에 대한 오버로드를 생성하도록 지시
class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    // Attribute Resource
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    // Style Resource
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime()
    // 주어진 theme를 가지고 새로운 context wrapper 생성
    // 주어진 theme는 context theme의 기초로 적용
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    // 직사각형 생성
    private val bounds = Rect()

    // Canvas에 옵션을 지정하는 Paint 클래스
    private var paint: Paint = Paint()

    private var currentDateItem = DateTime()

    init {
        /* Attributes */
        // withStyledAttributes를 사용하여 Attributes 획득
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize = getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 0).toFloat()

            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                isAntiAlias = true
                textSize = dayTextSize
                // getDateColor를 사용하여 색 획득
                color = getDateColor(date.dayOfWeek)
                // 같은 달이 아니라면 투명도 50 지정
                if (!isSameMonth(date, firstDayOfMonth)) {
                    alpha = 50
                }
            }
        }
    }

    // 그리기 메소드
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        // 해당 달의 일을 획득
        val date = date.dayOfMonth.toString()
        // 텍스트 경계선 획득
        paint.getTextBounds(date, 0, date.length, bounds)
        // Draw the text
        // 사이즈는 중간으로
        canvas.drawText(
            date,
            (width / 2 - bounds.width() / 2).toFloat() - 2,
            (height / 2 + bounds.height() / 2).toFloat(),
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                println("year : ${date.year}, month : ${date.monthOfYear}, day : ${date.dayOfMonth}")
                currentDateItem = date
            }
        }
        return true
    }

    private fun selectDateItem() {

    }
}