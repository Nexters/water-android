package appvian.water.buddy.view.analytics.chart.weekly

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.formatter.XAxisValueFormatter
import com.github.mikephil.charting.formatter.YAxisValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.ceil
import kotlin.math.floor

object WeeklyValueFormatter : YAxisValueFormatter, ValueFormatter {

    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        return String.format("%.1fL", value)
    }

    override fun getFormattedValue(value: Float, yAxis: YAxis?): String {
        if(ceil(value).toInt() == floor(value).toInt()) return String.format("%dL", value.toInt())

        return String.format("%.1fL", value)
    }
}