package com.leveloper.infinitecalendar.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.leveloper.infinitecalendar.R
import com.leveloper.infinitecalendar.utils.CalendarUtils.Companion.WEEKS_PER_MONTH
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import kotlin.math.max

// 커스텀뷰(ViewGroup를 상속) -> 레이아웃을 만듬
class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.calendarViewStyle,
    @StyleRes defStyleRes: Int = R.style.Calendar_CalendarViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private var _height: Float = 0f

    // Attribute
    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.CalendarView_dayHeight, 0f)
        }
    }

    /**
     * Measure
     */
    // 뷰와 그 하위항목의 사이즈 요구사항을 결정하기 위해 호출
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // suggestedMinimumHeight : Returns the suggested minimum height that the view should use.
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height * WEEKS_PER_MONTH).toInt())
        // 뷰의 크기 지정(Width, Height)
        // getDefaultSize : Uses the supplied size if the MeasureSpec imposed no constraints.
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    /**
     * Layout
     */
    // 뷰가 하위 뷰에 크기와 위치를 할당해야 할 때 호출됩니다.
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // View의 width를 지정(전체 width / 7)
        // DAYS_PER_WEEK는 상수
        val iWidth = (width / DAYS_PER_WEEK).toFloat()
        // View의 height를 지정(전체 Height / 6)
        val iHeight = (height / WEEKS_PER_MONTH).toFloat()

        var index = 0
        // 이 뷰 그룹의 하위 뷰에 대한 시퀀스를 반환
        children.forEach { view ->
            val left = (index % DAYS_PER_WEEK) * iWidth
            val top = (index / DAYS_PER_WEEK) * iHeight

            // left, top, right, bottom
            view.layout(left.toInt(), top.toInt(), (left + iWidth).toInt(), (top + iHeight).toInt())

            index++
        }
    }

    /**
     * 달력 그리기 시작한다.
     * @param firstDayOfMonth   한 달의 시작 요일
     * @param list              달력이 가지고 있는 요일과 이벤트 목록 (총 42개)
     */
    fun initCalendar(firstDayOfMonth: DateTime, list: List<DateTime>) {
        list.forEach {
            // Adds a child view.
            addView(DayItemView(
                context = context,
                date = it,
                firstDayOfMonth = firstDayOfMonth
            ))
        }
    }
}