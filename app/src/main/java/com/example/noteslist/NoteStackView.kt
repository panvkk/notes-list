package com.example.noteslist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.graphics.withTranslation
import androidx.core.view.isGone
import com.example.noteslist.core.toLocalDate

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private var defaultStackSpacing = 20f
    private var defaultStackMaxVisible = 3
    private var defaultCollapseButtonHeight = 20f
    private var defaultVerticalPadding = 20f
    private var defaultCollapseButtonColor = Color.GRAY
    private var defaultCollapseButtonSize = 20f
    private var defaultCollapseButtonText = ""

    private var stackSpacing = 20f
    private var stackMaxVisible = 3
    private var verticalPadding = defaultVerticalPadding
    private var collapseButtonHeight = defaultCollapseButtonHeight
    private var collapseButtonSize = defaultCollapseButtonSize
    private var collapseButtonColor = defaultCollapseButtonColor
    private var collapseButtonText = defaultCollapseButtonText
    private var _isExpanded = false
    var isExpanded: Boolean
        get() = _isExpanded
        set(value) {
            _isExpanded = value
            requestLayout()
            if(value) updateGeometry()
            invalidate()
        }

    // Paint
    private var collapseButtonPaint = Paint().apply { isAntiAlias = true }

    // Геометрия
    private var collapseButtonRect = Rect()
    private var collapseButtonPoint = Point()

    private var childIndexes: MutableList<Int>? = null


    init {
        val resources = context.resources
        resources.apply {
            defaultVerticalPadding = resources.getDimensionPixelSize(R.dimen.note_stack_vertical_padding).toFloat()
            defaultCollapseButtonHeight = resources.getDimension(R.dimen.note_stack_collapse_button_height)
            defaultCollapseButtonSize = resources.getDimension(R.dimen.note_stack_collapse_button_text_size)
            defaultCollapseButtonColor = resources.getColor(R.color.note_stack_collapse_button_color)
            defaultCollapseButtonText = resources.getString(R.string.note_stack_collapse_button_title)
        }
        collapseButtonText = defaultCollapseButtonText
        collapseButtonColor = defaultCollapseButtonColor
        collapseButtonSize = defaultCollapseButtonSize
        verticalPadding = defaultVerticalPadding
        collapseButtonHeight = defaultCollapseButtonHeight

        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
        setWillNotDraw(false)
    }

    private fun initPaints() {
        collapseButtonPaint.apply {
            style = Paint.Style.FILL
            color = collapseButtonColor
            textSize = this@NoteStackView.collapseButtonSize
        }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteStackView, defStyleAttr, defStyleRes)
            try {
                stackSpacing = typedArray.getDimension(R.styleable.NoteStackView_stackSpacing, defaultStackSpacing)
                stackMaxVisible = typedArray.getInteger(R.styleable.NoteStackView_stackMaxVisible, defaultStackMaxVisible)
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun shouldDelayChildPressedState(): Boolean = false

    override fun onFinishInflate() {
        childIndexes = MutableList(childCount) { index -> index }
        sortIndexes()
        super.onFinishInflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalHeight = paddingTop + paddingBottom

        childIndexes?.let {
            if(!isExpanded) {
                for(i in childIndexes) {
                    val child = getChildAt(i)
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                }
                totalHeight += getChildAt(0).measuredHeight
                totalHeight += (stackSpacing * stackMaxVisible).toInt()
            } else {
                for(i in childIndexes) {
                    val child = getChildAt(i)
                    measureChildWithMargins(child, widthMeasureSpec, 0 ,heightMeasureSpec, 0)

                    val lp = child.layoutParams as MarginLayoutParams
                    val totalChildHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

                    totalHeight += totalChildHeight
                }
                totalHeight += (collapseButtonHeight + verticalPadding).toInt()
            }
        }

        val measuredWidth = resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = paddingLeft
        var childTop = paddingTop

        childIndexes?.let {
            if (!isExpanded) {
                for (i in childIndexes) {
                    val child = getChildAt(i)
                    if (child.isGone) continue

                    val lp = child.layoutParams as MarginLayoutParams
                    val childWidth = child.measuredWidth - stackSpacing * i
                    val childHeight = child.measuredHeight

                    val left = childLeft + lp.leftMargin
                    val top = childTop + lp.topMargin
                    val right = (left + childWidth).toInt()
                    val bottom = top + childHeight

                    child.layout(left, top, right, bottom)
                    childLeft += stackSpacing.toInt()
                    childTop += stackSpacing.toInt()
                }
            } else {
                for (i in childIndexes) {
                    val child = getChildAt(i)
                    if (child.isGone) continue

                    val lp = child.layoutParams as MarginLayoutParams
                    val childWidth = child.measuredWidth
                    val childHeight = child.measuredHeight

                    val childTotalHeight = childHeight + lp.topMargin + lp.bottomMargin

                    val left = childLeft + lp.leftMargin
                    val top = childTop + lp.topMargin
                    val right = left + childWidth
                    val bottom = top + childHeight

                    child.layout(left, top, right, bottom)
                    childTop += childTotalHeight
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawCollapseButton(canvas)
    }

    private fun updateGeometry() {
        val left = paddingLeft
        val top = (measuredHeight - collapseButtonHeight).toInt()
        val right = measuredWidth
        val bottom = measuredHeight
        collapseButtonRect.set(left, top, right, bottom)
        collapseButtonPoint.set(left, (top + collapseButtonHeight / 2).toInt())
    }

    private fun drawCollapseButton(canvas: Canvas) {
        canvas.withTranslation(collapseButtonRect.left.toFloat(), collapseButtonRect.top.toFloat()) {
            this.drawText(
                collapseButtonText,
                collapseButtonPoint.x.toFloat(),
                collapseButtonPoint.y.toFloat(),
                collapseButtonPaint
            )
        }
    }
    private fun sortIndexes() {
        childIndexes?.let {
            try {
                it.sortWith(Comparator { i1, i2 ->
                    val child1 = getChildAt(i1) as NoteView
                    val child2 = getChildAt(i2) as NoteView
                    val date1 = child1.date.toLocalDate()
                    val date2 = child2.date.toLocalDate()

                    if (date1 == null || date2 == null) throw Throwable("Error while parse: Date can not be null")

                    if (date1 > date2) i1 else i2
                })
            } catch (e: Exception) {
                Log.e("NoteStackView", e.message ?: "")
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)  // Полная поддержка margins
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    // Проверяем совместимость LP (требуется для ViewGroup)
    override fun checkLayoutParams(lp: LayoutParams?): Boolean {
        return lp is MarginLayoutParams
    }
}