package appvian.water.buddy.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import appvian.water.buddy.R
import appvian.water.buddy.model.data.CalendarData
import kotlinx.android.synthetic.main.wb_calendar_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class WbCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mCal = Calendar.getInstance()
    private var year = mCal.get(Calendar.YEAR)
    private var month = mCal.get(Calendar.MONTH) + 1
    private val adapter = WbCalcAdapter()

    init {
        initSetting(context)
    }

    private fun initSetting(context: Context) {
        View.inflate(context, R.layout.wb_calendar_view, this)

        cal_veiw.adapter = adapter

        cal_prev_btn.setOnClickListener {
            month -= 1
            if (month < 1) {
                month = 12
                year -= 1
            }
            setCalendar()
        }

        cal_next_btn.setOnClickListener {
            month += 1
            if (month > 12) {
                month = 1
                year += 1
            }
            setCalendar()
        }

        setCalendar()
    }

    private fun setCalendar() {
        cal_year.text = year.toString()
        cal_month.text = "$month 월"

        setCalendarData(month)
        invalidate()
    }

    private fun setCalendarData(month: Int) {
        val dateList = ArrayList<CalendarData>()
        cal_month.text = "$month 월"

        mCal.set(year, month - 1, 1)

        var dayOfWeek = mCal.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek != -1) {
            while (dayOfWeek - 1 > 0) {
                dateList.add(
                    CalendarData(
                        "",
                        null
                    )
                )
                dayOfWeek -= 1
            }
        }

        for (i in 1..mCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dateList.add(
                CalendarData(
                    i.toString(),
                    null
                )
            )
        }

        adapter.setDateList(dateList)
    }

}