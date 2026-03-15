package com.example.noteslist.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Layout.Alignment
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.apply
import androidx.core.graphics.withTranslation
import com.example.noteslist.R

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        private const val KEY_SUPER_STATE = "superState"
        private const val KEY_IS_READ = "isRead"
    }

    // Defaults
    private var defaultHeight = 200f
    private var defaultSectionHeight = 50f
    private var defaultTitleTextSize = 20f
    private var defaultDescriptionTextSize = 50f
    private var defaultDateTextSize = 20f
    private var defaultStarSize = 20f
    private var defaultReadPointSize = 20f
    private var defaultDescriptionFadeWidth = 20f

    private var defaultTitleTopPadding = 20f
    private var defaultDescriptionTopPadding = 24f
    private var defaultContentBottomPadding = 20f
    private var defaultHorizontalPadding = 14f

    private var defaultBackgroundColor = Color.WHITE
    private var defaultSectionColor = Color.BLUE
    private var defaultTitleColor = Color.BLACK
    private var defaultDescriptionColor = Color.GRAY
    private var defaultDateColor = Color.GRAY
    private var defaultStarColor = Color.YELLOW
    private var defaultReadPointColor = Color.GREEN
    private var defaultTextReadColor = Color.GRAY

    private var defaultStarDrawable: Drawable? = null

    private val defaultTitle = "Заголовок"
    private val defaultDescription = "Описание"
    private val defaultDate = "01.01.2025"
    private val defaultImportance = false
    private val defaultIsRead = false
    private val defaultCornerRadius = 10f

    // State
    private var viewHeight = defaultHeight
    private var sectionHeight = defaultSectionHeight
    private var titleTextSize = defaultTitleTextSize
    private var descriptionTextSize = defaultDescriptionTextSize
    private var dateTextSize = defaultDateTextSize
    private var starSize = defaultStarSize
    private var readPointSize = defaultReadPointSize
    private var descriptionFadeWidth = defaultDescriptionFadeWidth

    private var titleTopPadding = defaultTitleTopPadding
    private var descriptionTopPadding = defaultDescriptionTopPadding
    private var contentBottomPadding = defaultContentBottomPadding
    private var horizontalPadding = defaultHorizontalPadding

    private var backgroundColor = defaultBackgroundColor
    private var sectionColor = defaultSectionColor
    private var titleColor = defaultTitleColor
    private var descriptionColor = defaultDescriptionColor
    private var dateColor = defaultDateColor
    private var starColor = defaultStarColor
    private var readPointColor = defaultReadPointColor
    private var textReadColor = defaultTextReadColor

    var title: String = defaultTitle
        set(value) {
            if(field == value) return
            field = value
            updateTextLayouts()
            requestLayout()
            invalidate()
        }
    var description: String = defaultDescription
        set(value) {
            if(field == value) return
            field = value
            updateTextLayouts()
            requestLayout()
            invalidate()
        }
    var date: String = defaultDate
        set(value) {
            if(field == value) return
            field = value
            invalidate()
        }
    var isImportant: Boolean = defaultImportance
        set(value) {
            if(field == value) return
            field = value
            updatePaints()
            updateGeometry()
            invalidate()
        }
    var isRead: Boolean = defaultIsRead
        set(value) {
            if(field == value) return
            field = value
            updatePaints()
            invalidate()
        }
    private var cornerRadius = 10f

    // Геометрия
    private var maxTextWidth = 0
    private var titlePoint = Point()
    private var descriptionPoint = Point()
    private var datePoint = Point()
    private var backgroundRect = RectF()
    private var sectionRect = RectF()
    private var readPoint = PointF()
    private var descriptionFadeRect = RectF()

    private var starDrawable = defaultStarDrawable

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val sectionPaint = Paint().apply { isAntiAlias = true }
    private val titleTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val descriptionTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val descriptionFadePaint = Paint().apply { isAntiAlias = true }
    private val dateTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val readPointPaint = Paint().apply { isAntiAlias = true }

    // Text Layout
    private var titleLayout: StaticLayout? = null
    private var descriptionLayout: StaticLayout? = null

    init {
        val resources = context.resources
        resources.apply {
            defaultHeight = resources.getDimension(R.dimen.note_view_height)
            defaultSectionHeight = resources.getDimension(R.dimen.note_section_height)
            defaultTitleTextSize = resources.getDimensionPixelSize(R.dimen.note_title_size).toFloat()
            defaultDescriptionTextSize = resources.getDimensionPixelSize(R.dimen.note_description_size).toFloat()
            defaultDateTextSize = resources.getDimensionPixelSize(R.dimen.note_date_size).toFloat()
            defaultStarSize = resources.getDimensionPixelSize(R.dimen.note_star_size).toFloat()
            defaultReadPointSize = resources.getDimensionPixelSize(R.dimen.note_read_point_size).toFloat()
            defaultDescriptionFadeWidth = resources.getDimensionPixelSize(R.dimen.description_fade_width).toFloat()

            defaultTitleTopPadding = resources.getDimension(R.dimen.note_title_top_padding)
            defaultDescriptionTopPadding = resources.getDimension(R.dimen.note_description_top_padding)
            defaultContentBottomPadding = resources.getDimension(R.dimen.note_content_bottom_padding)
            defaultHorizontalPadding = resources.getDimension(R.dimen.note_content_horizontal_padding)

            defaultStarDrawable = resources.getDrawable(R.drawable.star_icon).mutate()
        }

        viewHeight = defaultHeight
        sectionHeight = defaultSectionHeight
        titleTextSize = defaultTitleTextSize
        descriptionTextSize = defaultDescriptionTextSize
        dateTextSize = defaultDateTextSize
        starSize = defaultStarSize
        readPointSize = defaultReadPointSize
        descriptionFadeWidth = defaultDescriptionFadeWidth

        titleTopPadding = defaultTitleTopPadding
        descriptionTopPadding = defaultDescriptionTopPadding
        contentBottomPadding = defaultContentBottomPadding
        horizontalPadding = defaultHorizontalPadding

        starDrawable = defaultStarDrawable


        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()


        // Определяем Outline, чтобы canvas был обрезан и родительский контейнер рисовал правильный elevation
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                val leftX = paddingLeft
                val topY = paddingTop
                val rightX = width - paddingRight
                val bottomY = paddingTop + viewHeight.toInt()

                outline?.setRoundRect(leftX, topY, rightX, bottomY, cornerRadius)
            }
        }
        // Обрезка по контуру
        clipToOutline = true
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {
                cornerRadius = typedArray.getDimension(R.styleable.NoteView_noteCornerRadius, defaultCornerRadius)
                backgroundColor = typedArray.getColor(R.styleable.NoteView_noteBackgroundColor,defaultBackgroundColor)
                sectionColor = typedArray.getColor(R.styleable.NoteView_noteSectionColor,defaultSectionColor)
                titleColor = typedArray.getColor(R.styleable.NoteView_noteTitleColor, defaultTitleColor)
                descriptionColor = typedArray.getColor(R.styleable.NoteView_noteDescriptionColor, defaultDescriptionColor)
                dateColor = typedArray.getColor(R.styleable.NoteView_noteDateColor, defaultDateColor)
                starColor = typedArray.getColor(R.styleable.NoteView_noteStarColor, defaultStarColor)
                readPointColor = typedArray.getColor(R.styleable.NoteView_noteReadPointColor, defaultReadPointColor)
                textReadColor = typedArray.getColor(R.styleable.NoteView_noteTextReadColor, defaultTextReadColor)

                title = typedArray.getString(R.styleable.NoteView_title) ?: defaultTitle
                description = typedArray.getString(R.styleable.NoteView_description) ?: defaultDescription
                date = typedArray.getString(R.styleable.NoteView_date) ?: defaultDate
                isImportant = typedArray.getBoolean(R.styleable.NoteView_importance, defaultImportance)
                isRead = typedArray.getBoolean(R.styleable.NoteView_isRead, defaultIsRead)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun initPaints() {
        descriptionTextPaint.apply {
            style = Paint.Style.FILL
            color = descriptionColor
            textSize = this@NoteView.descriptionTextSize
        }
        titleTextPaint.apply {
            style = Paint.Style.FILL
            color = titleColor
            textSize = this@NoteView.titleTextSize
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setTypeface(Typeface.create(Typeface.DEFAULT, 900, false))
            } else {
                setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            }
        }
        dateTextPaint.apply {
            style = Paint.Style.FILL
            color = dateColor
            textSize = this@NoteView.dateTextSize
        }
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = backgroundColor
        }
        sectionPaint.apply {
            style = Paint.Style.FILL
            color = sectionColor
        }
        readPointPaint.apply {
            style = Paint.Style.FILL
            color = readPointColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = readPointSize
        }
    }

    private fun updatePaints() {
        if(isRead) {
            titleTextPaint.apply { color = textReadColor }
            descriptionTextPaint.apply { color = textReadColor }
            dateTextPaint.apply { color = textReadColor }
            sectionPaint.apply { color = backgroundColor }
        } else {
            titleTextPaint.apply { color = titleColor }
            descriptionTextPaint.apply { color = descriptionColor }
            dateTextPaint.apply { color = dateColor }
            sectionPaint.apply { color = sectionColor }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec)

        val maxTextAvailableWidth = (availableWidth - paddingLeft - paddingRight - 2 * horizontalPadding).toInt()
        if(maxTextAvailableWidth != maxTextWidth) {
            maxTextWidth = maxTextAvailableWidth
            updateTextLayouts()
        }
        val viewContentHeight = context.resources.getDimensionPixelSize(R.dimen.note_view_height)
        val fixedViewHeight = viewContentHeight + paddingTop + paddingBottom
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(fixedViewHeight, MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxTextWidth = (w - paddingLeft - paddingRight - 2 * horizontalPadding.toInt())

        if(maxTextWidth > 0) {
            updateTextLayouts()
        }
        updateGeometry()
    }

    private fun updateTextLayouts() {
        descriptionLayout = StaticLayout.Builder.obtain(description, 0, description.length, descriptionTextPaint, maxTextWidth)
            .setAlignment(Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setMaxLines(2)
            .setEllipsize(TextUtils.TruncateAt.END)
            .build()

        titleLayout = StaticLayout.Builder.obtain(title, 0, title.length, titleTextPaint, maxTextWidth)
            .setAlignment(Alignment.ALIGN_NORMAL)
            .setMaxLines(1)
            .build()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawNoteCard(canvas)
        drawTitle(canvas)
        drawDescription(canvas)
        drawDate(canvas)
        drawDescriptionFadeRect(canvas)

        if(isImportant) drawStar(canvas)
        if(isRead) drawReadPoint(canvas)
    }

    private fun updateGeometry() {

        val leftX = paddingLeft.toFloat()
        val topY = paddingTop.toFloat()
        val rightX = width - paddingRight.toFloat()
        val bottomY = paddingTop + viewHeight

        // Фон
        backgroundRect.set(leftX, topY, rightX, bottomY)
        sectionRect.set(leftX, topY, rightX, topY + sectionHeight)

        // Текст
        val titleLayoutHeight = titleLayout?.height ?: 0
        val descriptionLayoutHeight = descriptionLayout?.height ?: 0

        val titleTopY = (topY + sectionHeight/ 2 - titleLayoutHeight / 2).toInt()
        val titleLeftX = (if(isImportant) leftX + 2 * horizontalPadding + starSize
            else leftX + horizontalPadding).toInt()
        val descriptionTopY = (sectionHeight + descriptionTopPadding).toInt()
        val descriptionLeftX = (leftX + horizontalPadding).toInt()

        val dateCenterY = (bottomY + (dateTextPaint.fontMetrics.ascent - dateTextPaint.fontMetrics.descent) / 2 - contentBottomPadding).toInt()
        val dateLeftX = (leftX + horizontalPadding).toInt()

        titlePoint.set(titleLeftX, titleTopY)
        descriptionPoint.set(descriptionLeftX, descriptionTopY)
        datePoint.set(dateLeftX, dateCenterY)

        // Иконки (важно/не важно и прочитано/не прочитано)
        val starLeft = (leftX + horizontalPadding).toInt()
        val starTop = (topY + sectionHeight / 2 - starSize / 2).toInt()
        starDrawable?.setBounds(
            starLeft,
            starTop,
            (starLeft + starSize).toInt(),
            (starTop + starSize).toInt()
        )

        val readCenterX = rightX - horizontalPadding - readPointSize / 2
        val readCenterY = bottomY - contentBottomPadding - readPointSize / 2
        readPoint.set(readCenterX, readCenterY)

        // Фейд в конце описания
        val lastLineIndex = (descriptionLayout?.lineCount ?: 1) - 1
        val isEllipsized = (descriptionLayout?.getEllipsisCount(lastLineIndex) ?: 0) > 0
        if(isEllipsized) {
            descriptionFadeRect.set(
                rightX - descriptionFadeWidth,
                descriptionTopY + descriptionLayoutHeight / 2f,
                rightX - horizontalPadding,
                descriptionTopY + descriptionLayoutHeight.toFloat()
            )
            descriptionFadePaint.apply {
                shader = LinearGradient(
                    descriptionFadeRect.left, 0f, descriptionFadeRect.right, 0f,
                    intArrayOf(Color.TRANSPARENT, backgroundColor),
                    floatArrayOf(0f, 0.6f),
                    Shader.TileMode.CLAMP
                )
            }
        } else {
            descriptionFadeRect.setEmpty()
        }

    }

    private fun drawTitle(canvas: Canvas) {
        titleLayout?.let {
            canvas.withTranslation(titlePoint.x.toFloat(), titlePoint.y.toFloat()) {
                it.draw(this)
            }
        }
    }

    private fun drawDescription(canvas: Canvas) {
        descriptionLayout?.let {
            canvas.withTranslation(descriptionPoint.x.toFloat(), descriptionPoint.y.toFloat()) {
                it.draw(this)
            }
        }
    }

    private fun drawDate(canvas: Canvas) {
        canvas.drawText(date, datePoint.x.toFloat(), datePoint.y.toFloat(), dateTextPaint)
    }

    private fun drawNoteCard(canvas: Canvas) {
        // Добавляем фон
        canvas.drawRect(backgroundRect, backgroundPaint)
        // Добавляем верхнюю секцию для заголовка
        canvas.drawRect(sectionRect, sectionPaint)
    }

    private fun drawStar(canvas: Canvas) {
        starDrawable?.setTint(starColor)
        starDrawable?.draw(canvas)
    }

    private fun drawReadPoint(canvas: Canvas) {
        canvas.withTranslation(readPoint.x, readPoint.y) {
            this.drawPoint(0f, 0f, readPointPaint)
        }
    }
    private fun drawDescriptionFadeRect(canvas: Canvas) {
        canvas.drawRect(descriptionFadeRect, descriptionFadePaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                isRead = !isRead
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // Сохранение состояния
    override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        state.putBoolean(KEY_IS_READ, isRead)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state is Bundle) {
            val superState = state.getParcelable<Parcelable>(KEY_SUPER_STATE)
            super.onRestoreInstanceState(superState)

            val isReadState = state.getBoolean(KEY_IS_READ)
            isRead = isReadState

            // обновляем, так как isRead мог измениться
            updatePaints()
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}