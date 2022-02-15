package com.leveloper.infinitecalendar

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.joda.time.DateTime

// ViewPager2를 위한 Adapter
class CalendarAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

	/* 달의 첫 번째 Day timeInMillis*/
	private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

	// adapter에 연결한 총 아이템의 갯수
	override fun getItemCount(): Int = Int.MAX_VALUE

	// 주어진 position에 해당하는 Fragment 생성
	override fun createFragment(position: Int): CalendarFragment {
		val millis = getItemId(position)
		return CalendarFragment.newInstance(millis)
	}

	// 해당 position의 고유 id를 반환하는 메소드
	override fun getItemId(position: Int): Long {
		// start(한달의 시작일)에서 position의 값을 달에 더하고 timeInMillis로 반환
		return DateTime(start).plusMonths(position - START_POSITION).millis
	}

	// 해당 itemid가 유효한 id인지를 판별하는 메소드
	// 매개변수로 getItemId에서 반환한 값이 넘어옴
	override fun containsItem(itemId: Long): Boolean {
		val date = DateTime(itemId)

		return date.dayOfMonth == 1 && date.millisOfDay == 0
	}

	companion object {
		const val START_POSITION = Int.MAX_VALUE / 2
	}
}