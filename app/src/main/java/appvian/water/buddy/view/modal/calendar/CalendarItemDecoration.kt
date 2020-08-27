package appvian.water.buddy.view.modal.calendar

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CalendarItemDecoration : RecyclerView.ItemDecoration() {
    var space = -25
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        val rowCnt = Math.round(state.itemCount / 7f)

        outRect.bottom = space
        if (rowCnt > 0 && Math.round(position / 7f) == rowCnt) {
            outRect.bottom = 0
        }
    }
}