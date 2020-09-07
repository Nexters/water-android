package appvian.water.buddy.view.analytics.month

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CharacterItemDecoration : RecyclerView.ItemDecoration() {
    var space = -20
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space
    }
}