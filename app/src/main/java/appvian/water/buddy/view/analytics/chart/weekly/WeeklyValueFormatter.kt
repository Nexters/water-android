package appvian.water.buddy.view.analytics.chart.weekly

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.YAxisValueFormatter
import kotlin.math.ceil
import kotlin.math.floor

class WeeklyValueFormatter : YAxisValueFormatter {
    override fun getFormattedValue(value: Float, yAxis: YAxis?): String {
        if(ceil(value).toInt() == floor(value).toInt()) return String.format("%dL", value.toInt())

        return String.format("%.1fL", value)
    }
}