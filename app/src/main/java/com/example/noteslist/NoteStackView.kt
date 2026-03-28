package com.example.noteslist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.isGone
import com.example.noteslist.core.toLocalDate

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val KEY_SUPER_STATE = "superState"
        const val KEY_IS_EXPANDED = "isExpanded"
    }

    private val translationZFactor = 0.1f

    private var defaultStackSpacing = 20f
    private var defaultStackMaxVisible = 3
    private var defaultCollapseButtonHeight = 20f
    private var defaultVerticalPadding = 20f
    private var defaultCollapseButtonColor = Color.GRAY
    private var defaultCollapseButtonSize = 20f
    private var defaultCollapseButtonText = ""
    private var defaultMaxChildElevation = 20f

    private var stackSpacing = 20f
    private var stackMaxVisible = 3
    private var maxChildElevation = 20f
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

        clipChildren = false // чтобы не обрезалась тень
        clipToPadding = false
    }

    private fun initPaints() {
        collapseButtonPaint.apply {
            style = Paint.Style.FILL
            color = collapseButtonColor
            textSize = this@NoteStackView.collapseButtonSize
            textAlign = Paint.Align.LEFT
        }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteStackView, defStyleAttr, defStyleRes)
            try {
                stackSpacing = typedArray.getDimension(R.styleable.NoteStackView_stackSpacing, defaultStackSpacing)
                stackMaxVisible = typedArray.getInteger(R.styleable.NoteStackView_stackMaxVisible, defaultStackMaxVisible)
                maxChildElevation = typedArray.getDimension(R.styleable.NoteStackView_stackMaxChildElevation, defaultMaxChildElevation)
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
        val maxChildTranslationZ = stackMaxVisible * translationZFactor
        val shadowPadding = (maxChildElevation + maxChildTranslationZ).toInt()

        childIndexes?.let {
            if(!isExpanded) {
                for(i in childIndexes) {
                    val child = getChildAt(i)
                    measureChildWithMargins(
                        child,
                        widthMeasureSpec, shadowPadding * 2,
                        heightMeasureSpec, shadowPadding * 2
                    )
                }
                totalHeight += getChildAt(0).measuredHeight
                totalHeight += (stackSpacing * stackMaxVisible).toInt()
                totalHeight += shadowPadding * 2
            } else {
                for(i in childIndexes) {
                    val child = getChildAt(i)
                    measureChildWithMargins(
                        child,
                        widthMeasureSpec, shadowPadding * 2,
                        heightMeasureSpec, shadowPadding * 2
                    )

                    val lp = child.layoutParams as MarginLayoutParams
                    val totalChildHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

                    totalHeight += totalChildHeight
                }
                totalHeight += (collapseButtonHeight + verticalPadding).toInt()
                totalHeight += (maxChildElevation * childCount).toInt()         // место под elevation
            }
        }


        val measuredWidth = resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxChildTranslationZ = stackMaxVisible * translationZFactor
        val shadowPadding = (maxChildElevation + maxChildTranslationZ).toInt()

        var childLeft = paddingLeft + shadowPadding
        var childTop = paddingTop + shadowPadding

        childIndexes?.let {
            if (!isExpanded) {
                var counter = 0
                // Оставляем только самые важные элементы сверху
                val upperChildren = it.slice(0..<stackMaxVisible)
                    .reversed()
                for (i in upperChildren) {
                    val child = getChildAt(i)
                    if (child.isGone) continue

                    val lp = child.layoutParams as MarginLayoutParams
                    val childWidth = child.measuredWidth - stackSpacing * counter
                    val childHeight = child.measuredHeight

                    val left = childLeft + lp.leftMargin
                    val top = childTop + lp.topMargin
                    val right = (left + childWidth).toInt()
                    val bottom = top + childHeight

                    child.translationZ += translationZFactor * counter // для того чтобы карточки нормально накладывались друг на друга, с elevation

                    child.layout(left, top, right, bottom)
                    childLeft += stackSpacing.toInt()
                    childTop += stackSpacing.toInt()
                    counter++
                }
            } else {
                for (i in it) {
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
                    childTop += childTotalHeight + maxChildElevation.toInt()
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateGeometry()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        drawCollapseButton(canvas)
    }

    private fun updateGeometry() {
        val shadowPadding = maxChildElevation.toInt() // чтобы кнопка не прыгала относительно контента

        // Геометрия кнопки сворачивания
        val left = paddingLeft + shadowPadding
        val top = (measuredHeight - collapseButtonHeight).toInt()
        val right = measuredWidth
        val bottom = measuredHeight
        collapseButtonRect.set(left, top, right, bottom)
        collapseButtonPoint.set(left, (top + collapseButtonHeight / 2).toInt())
    }

    private fun drawCollapseButton(canvas: Canvas) {
        if(isExpanded) {
            canvas.drawText(
                collapseButtonText,
                collapseButtonPoint.x.toFloat(),
                collapseButtonPoint.y.toFloat(),
                collapseButtonPaint
            )
        }
    }

    // Перехват нажатия, если стэк свёрнут
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(!isExpanded) return true
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = event.x.toInt()
            val y = event.y.toInt()
            if(collapseButtonRect.contains(x, y)) {
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> return true
                    MotionEvent.ACTION_UP -> {
                        isExpanded = false
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun sortIndexes() {
        try {
            childIndexes?.sortWith(Comparator { i1, i2 ->
                val child1 = getChildAt(i1) as NoteView
                val child2 = getChildAt(i2) as NoteView
                val date1 = child1.date.toLocalDate()
                val date2 = child2.date.toLocalDate()

                if (date1 == null || date2 == null) {
                    throw IllegalArgumentException("Error while parse: Date can not be null")
                }

                date2.compareTo(date1)
            })
        } catch (e: Exception) {
            Log.e("NoteStackView", e.message ?: "")
        }
    }

    // Сохранение состояния
    override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        state.putBoolean(KEY_IS_EXPANDED, isExpanded)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state is Bundle) {
            val superState = state.getParcelable<Parcelable>(KEY_SUPER_STATE)
            super.onRestoreInstanceState(superState)

            val isExpandedState = state.getBoolean(KEY_IS_EXPANDED)
            isExpanded = isExpandedState
        } else {
            super.onRestoreInstanceState(state)
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