package com.example.noteslist.presentation.ui.view

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import androidx.core.view.isGone
import com.example.noteslist.R
import com.example.noteslist.core.toLocalDate
import kotlin.math.min

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

    private val translationZFactor = 0.2f

    private var defaultStackSpacing = 20f
    private var defaultStackMaxVisible = 3
    private var defaultVerticalPadding = 20f
    private var defaultMaxChildElevation = 20f

    var stackSpacing = 20f
    var stackMaxVisible = 3
    var maxChildElevation = 20f
    private var verticalPadding = defaultVerticalPadding
    var isExpanded: Boolean = false
        set(value) {
            if(field == value) return
            field = value
            requestLayout()
            invalidate()
        }

    // Клики на стэк
    private var onClickExpand: () -> Unit = { }
    private var onClickCollapse: () -> Unit = { }
    fun setOnClickExpandListener(l: () -> Unit) { onClickExpand = l }
    fun setOnClickCollapseListener(l: () -> Unit) { onClickCollapse = l }

    // Анимации
    private var collapseButtonAnimator: ValueAnimator? = null

    // Кнопка сворачивания
    private val collapseButton = CollapseButtonView(context)
    private var childIndexes: MutableList<Int>? = null
    // Оставляем только самые важные элементы сверху
    private var visibleChildren: List<Int> = emptyList()
    private var invisibleChildren: List<Int> = emptyList()


    init {
        val resources = context.resources
        resources.apply {
            defaultVerticalPadding = resources.getDimensionPixelSize(R.dimen.note_stack_vertical_padding).toFloat()
        }
        verticalPadding = defaultVerticalPadding

        initAttrs(attrs, defStyleAttr, defStyleRes)
        initCollapseButton()
        setWillNotDraw(false)

        clipToPadding = false // чтобы не обрезалась тень
    }

    private fun initCollapseButton() {
        collapseButton.apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            alpha = 0f
            setOnButtonClickListener {
                onClickCollapse()
                performCollapseAnimation()
                isExpanded = false
            }
        }
    }

    private fun updateCollapseButtonBeforeAnimation() {
        collapseButton.apply {
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
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
        addView(collapseButton)
        updateChildIndexes()
        super.onFinishInflate()
    }

    override fun onViewRemoved(child: View?) {
        updateChildIndexes()
        super.onViewRemoved(child)
    }

    override fun onViewAdded(child: View?) {
        updateChildIndexes()
        super.onViewAdded(child)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalHeight = paddingTop + paddingBottom

        childIndexes?.let {
            if(!isExpanded) {
                for(i in it) {
                    val child = getChildAt(i)
                    measureChildWithMargins(
                        child,
                        widthMeasureSpec, 0,
                        heightMeasureSpec, 0
                    )
                }
                if(it.isNotEmpty()) totalHeight += getChildAt(1).measuredHeight
                val visibleChildrenCount = min(stackMaxVisible, childCount)
                totalHeight += (stackSpacing * (visibleChildrenCount - 1)).toInt()
            } else {
                for(i in it) {
                    val child = getChildAt(i)
                    measureChildWithMargins(
                        child,
                        widthMeasureSpec, 0,
                        heightMeasureSpec, 0
                    )

                    val lp = child.layoutParams as MarginLayoutParams
                    val totalChildHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

                    totalHeight += totalChildHeight + verticalPadding.toInt()
                }
                measureChild(
                    collapseButton,
                    widthMeasureSpec,
                    heightMeasureSpec
                )
                val totalCollapseButtonHeight = collapseButton.measuredHeight
                totalHeight += (totalCollapseButtonHeight + verticalPadding).toInt()
            }
        }

        val measuredWidth = resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(childIndexes?.size == 0) return

        var childLeft = paddingLeft
        var childTop = paddingTop

        childIndexes?.let {
            if (!isExpanded) {
                var counter = 0

                for (i in invisibleChildren) {
                    val child = getChildAt(i)
                    val lp = child.layoutParams as MarginLayoutParams
                    val childWidth = child.measuredWidth
                    val childHeight = child.measuredHeight

                    val left = childLeft + lp.leftMargin
                    val top = childTop + lp.topMargin
                    val right = left + childWidth
                    val bottom = top + childHeight

                    child.visibility = INVISIBLE
                    child.layout(left, top, right, bottom)
                }
                for (i in visibleChildren) {
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
                    childTop += childTotalHeight + verticalPadding.toInt()
                }
                // Отдельно расставляем кнопку "Свернуть"
                val collapseButtonWidth = collapseButton.measuredWidth
                val collapseButtonHeight = collapseButton.measuredHeight

                val left = childLeft
                val top = measuredHeight - collapseButtonHeight
                val right = left + collapseButtonWidth
                val bottom = top + collapseButtonHeight

                collapseButton.layout(left, top, right, bottom)
            }
        }
    }

    private fun animateCollapseButtonAppearance(
        pathInterpolator: PathInterpolator,
        animDuration: Long,
        animStartDelay: Long
    ) {
        collapseButtonAnimator?.cancel()
        val scaleHolder = PropertyValuesHolder.ofFloat("scale", 0.7f, 1f)
        val alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)

        collapseButtonAnimator = ValueAnimator
            .ofPropertyValuesHolder(scaleHolder, alphaHolder)
            .apply {
                interpolator = pathInterpolator
                addUpdateListener {
                    collapseButton.scaleX = getAnimatedValue("scale") as Float
                    collapseButton.scaleY = getAnimatedValue("scale") as Float
                    collapseButton.alpha = getAnimatedValue("alpha") as Float
                    this@NoteStackView.invalidate()
                }
                duration = animDuration
                startDelay = animStartDelay
                start()
            }
    }

    private fun performExpandAnimation() {
        val pathInterpolator = PathInterpolator(0.4f, 0.1f, 0.2f, 1f)
        val transitionSet = TransitionSet().setInterpolator(pathInterpolator)

        val maxDuration = 800L
        val delayFactor = 20L
        val durationFactor = 40L
        val baseDuration = 200L
        val translationZFactor = 10f


        val indexes = childIndexes?.toList() ?: return

        val transitionDuration =
            min(maxDuration, baseDuration + indexes.size * durationFactor)

        var childCounter = 0
        for(i in indexes) {
            val view = getChildAt(i)
            if(i in invisibleChildren) {
                view.translationZ -= translationZFactor // Чтобы невидимые вьюхи появлялись под видимыми
                view.visibility = VISIBLE
            }

            val viewTransition = ChangeBounds().apply {
                addTarget(view)
                duration = transitionDuration
                startDelay = delayFactor * (childCounter++)
            }
            transitionSet.addTransition(viewTransition)
        }

        updateCollapseButtonBeforeAnimation()
        transitionSet.addListener(object: TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                animateCollapseButtonAppearance(
                    pathInterpolator = pathInterpolator,
                    animDuration = 200L, animStartDelay = 100L
                )
                invisibleChildren.forEach { i -> // Возвращаем translationZ
                    val child = getChildAt(i)
                    val targetTranslationZ = child.translationZ + translationZFactor
                    child.animate()
                        .translationZ(targetTranslationZ)
                        .setDuration(200)
                        .start()
                }
            }
        })
        TransitionManager.beginDelayedTransition(this, transitionSet)
    }

    private fun performCollapseAnimation() {
        val indexes = childIndexes?.toList() ?: return
        for(i in indexes) {
            val view = getChildAt(i)
            if(i in invisibleChildren) view.visibility = INVISIBLE
        }
        TransitionManager.beginDelayedTransition(this)

    }

    // Перехват нажатия, если стэк свёрнут
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(!isExpanded) return true
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                if(!isExpanded) {
                    onClickExpand()
                    performExpandAnimation()
                    isExpanded = true
                }
                return true
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

    private fun updateChildIndexes() {
        childIndexes = (0 until childCount)
                .filter { getChildAt(it) is NoteView }.toMutableList()
        sortIndexes()
        childIndexes?.let {
            if(stackMaxVisible < childCount) {
                visibleChildren = it.slice(0..<stackMaxVisible).reversed()
                invisibleChildren = it - visibleChildren
            } else {
                visibleChildren = it.reversed()
                invisibleChildren = emptyList()
            }
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

    private class CollapseButtonView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : View(context, attrs, defStyleAttr, defStyleRes)  {

        private var defaultCollapseButtonColor = Color.GRAY
        private var defaultCollapseButtonTextSize = 20f
        private var defaultCollapseButtonText = ""
        private var defaultCollapseButtonHeight = 20f

        private var collapseButtonTextSize = defaultCollapseButtonTextSize
        private var collapseButtonColor = defaultCollapseButtonColor
        private var collapseButtonText = defaultCollapseButtonText
        private var collapseButtonHeight = defaultCollapseButtonHeight

        private var onCollapseClick: () -> Unit = { }

        fun setOnButtonClickListener(onClick: () -> Unit) {
            onCollapseClick = onClick
        }

        // Paint
        private var collapseButtonPaint = Paint().apply { isAntiAlias = true }

        // Геометрия
        private var collapseButtonRect = Rect()
        private var collapseButtonPoint = Point()

        init {
            val resources = context.resources
            resources.apply {
                defaultCollapseButtonHeight = resources.getDimension(R.dimen.note_stack_collapse_button_height)
                defaultCollapseButtonTextSize = resources.getDimension(R.dimen.note_stack_collapse_button_text_size)
                defaultCollapseButtonColor = resources.getColor(R.color.note_stack_collapse_button_color)
                defaultCollapseButtonText = resources.getString(R.string.note_stack_collapse_button_title)

            }
            collapseButtonText = defaultCollapseButtonText
            collapseButtonColor = defaultCollapseButtonColor
            collapseButtonTextSize = defaultCollapseButtonTextSize
            collapseButtonHeight = defaultCollapseButtonHeight

            initPaints()
            updateGeometry()
        }


        private fun initPaints() {
            collapseButtonPaint.apply {
                style = Paint.Style.FILL
                color = collapseButtonColor
                textSize = this@CollapseButtonView.collapseButtonTextSize
                textAlign = Paint.Align.LEFT
            }
        }

        private fun updateGeometry() {
            val left = paddingLeft
            val top = paddingTop
            val right = measuredWidth
            val bottom = measuredHeight
            collapseButtonRect.set(left, top, right, bottom)
            collapseButtonPoint.set(left, (top + collapseButtonHeight / 2).toInt())
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val fixedViewHeight = (collapseButtonHeight + paddingTop + paddingBottom).toInt()
            val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(fixedViewHeight, MeasureSpec.EXACTLY)

            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        }

        override fun onDraw(canvas: Canvas) {
            drawCollapseButton(canvas)
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            updateGeometry()
        }

        private fun drawCollapseButton(canvas: Canvas) {
            canvas.drawText(
                collapseButtonText,
                collapseButtonPoint.x.toFloat(),
                collapseButtonPoint.y.toFloat(),
                collapseButtonPaint
            )
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> return true
                MotionEvent.ACTION_UP -> {
                    onCollapseClick.invoke()
                    return true
                }
            }
            return super.onTouchEvent(event)
        }
    }
}