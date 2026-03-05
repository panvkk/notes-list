package com.example.noteslist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import android.text.Layout.Alignment
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.apply

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    // Defaults
    private var defaultHeight = 200f
    private var defaultSectionHeight = 50f
    private var defaultTitleTextSize = 20f
    private var defaultDescriptionTextSize = 50f
    private var defaultDateTextSize = 20f

    private var defaultTitleVerticalPadding = 20f
    private var defaultDescriptionVerticalPadding = 24f
    private var defaultDateVerticalPadding = 20f
    private var defaultHorizontalPadding = 14f

    private var defaultBackgroundColor = Color.WHITE
    private var defaultSectionColor = Color.BLUE
    private var defaultTitleColor = Color.BLACK
    private var defaultDescriptionColor = Color.GRAY
    private var defaultDateColor = Color.GRAY

    private val defaultTitle = "Заголовок"
    private val defaultDescription = "Описание"
    private val defaultDate = "01.01.2025"
    private val defaultImportance = false

    // State
    private var viewHeight = defaultHeight
    private var sectionHeight = defaultSectionHeight
    private var titleTextSize = defaultTitleTextSize
    private var descriptionTextSize = defaultDescriptionTextSize
    private var dateTextSize = defaultDateTextSize

    private var titleVerticalPadding = defaultTitleVerticalPadding
    private var descriptionVerticalPadding = defaultDescriptionVerticalPadding
    private var dateVerticalPadding = defaultDateVerticalPadding
    private var horizontalPadding = defaultHorizontalPadding

    private var backgroundColor = defaultBackgroundColor
    private var sectionColor = defaultSectionColor
    private var titleColor = defaultTitleColor
    private var descriptionColor = defaultDescriptionColor
    private var dateColor = defaultDateColor

    private var title = defaultTitle
    private var description = defaultDescription
    private var date = defaultDate
    private var importance = defaultImportance

    // Геометрия
    private val cardPath = Path()
    private var center = Point()
    private var titlePoint = Point()
    private var descriptionPoint = Point()
    private var datePoint = Point()
    private var cardRect = RectF()
    private var sectionRect = RectF()
    private var shadowRadius = 10f

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val sectionPaint = Paint().apply { isAntiAlias = true }
    private val titleTextPaint = Paint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val descriptionTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val dateTextPaint = Paint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }

    init {
        val resources = context.resources
        resources.apply {
            defaultHeight = resources.getDimension(R.dimen.note_view_height)
            defaultSectionHeight = resources.getDimension(R.dimen.note_section_height)
            defaultTitleTextSize = resources.getDimensionPixelSize(R.dimen.note_title_size).toFloat()
            defaultDescriptionTextSize = resources.getDimensionPixelSize(R.dimen.note_description_size).toFloat()
            defaultDateTextSize = resources.getDimensionPixelSize(R.dimen.note_date_size).toFloat()

            defaultTitleVerticalPadding = resources.getDimension(R.dimen.note_title_vertical_padding)
            defaultDescriptionVerticalPadding = resources.getDimension(R.dimen.note_description_vertical_padding)
            defaultDateVerticalPadding = resources.getDimension(R.dimen.note_date_vertical_padding)
            defaultHorizontalPadding = resources.getDimension(R.dimen.note_content_horizontal_padding)

            defaultBackgroundColor = resources.getColor(R.color.note_background)
            defaultSectionColor = resources.getColor(R.color.note_section)
            defaultTitleColor = resources.getColor(R.color.note_title)
            defaultDescriptionColor = resources.getColor(R.color.note_description)
            defaultDateColor = resources.getColor(R.color.note_date)
        }

        viewHeight = defaultHeight
        sectionHeight = defaultSectionHeight
        titleTextSize = defaultTitleTextSize
        descriptionTextSize = defaultDescriptionTextSize
        dateTextSize = defaultDateTextSize

        titleVerticalPadding = defaultTitleVerticalPadding
        descriptionVerticalPadding = defaultDescriptionVerticalPadding
        dateVerticalPadding = defaultDateVerticalPadding
        horizontalPadding = defaultHorizontalPadding

        backgroundColor = defaultBackgroundColor
        sectionColor = defaultSectionColor
        titleColor = defaultTitleColor
        descriptionColor = defaultDescriptionColor
        dateColor = defaultDateColor

        // Определяем Outline, чтобы canvas был обрезан и родительский контейнер рисовал правильный elevation
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                val radius = resources.getDimension(R.dimen.note_round_radius)

                val leftX = paddingLeft
                val topY = paddingTop
                val rightX = width - paddingRight
                val bottomY = paddingTop + viewHeight.toInt()

                outline?.setRoundRect(leftX, topY, rightX, bottomY, radius)
            }
        }
        // Обрезка по контуру
        clipToOutline = true


        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {
                title = typedArray.getString(R.styleable.NoteView_title) ?: defaultTitle
                description = typedArray.getString(R.styleable.NoteView_description) ?: defaultDescription
                date = typedArray.getString(R.styleable.NoteView_date) ?: defaultDate
                importance = typedArray.getBoolean(R.styleable.NoteView_importance, defaultImportance)
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
        }
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = backgroundColor
        }
        sectionPaint.apply {
            style = Paint.Style.FILL
            color = sectionColor
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGeometry()
        drawNoteCard(canvas)
        drawTitle(canvas)
        drawDescription(canvas)
    }

    private fun updateGeometry() {
        val leftPadding = paddingLeft.toFloat()
        val topPadding = paddingTop.toFloat()
        val rightPadding = paddingRight.toFloat()
        val bottomPadding = paddingBottom.toFloat()

        val leftX = (leftPadding)
        val topY = (topPadding)
        val rightX = (width - rightPadding.toInt()).toFloat()
        val bottomY = topPadding + viewHeight // TODO(Надо что-то поменять тут)

        val contentWidth = width - leftPadding.toInt() - rightPadding.toInt()
        val contentHeight = height - topPadding.toInt() - bottomPadding.toInt()

        center.x = (leftPadding + contentWidth / 2).toInt()
        center.y = (topPadding + contentHeight / 2).toInt()

        cardPath.set(Path())

        // Фон
        cardRect.set(leftX, topY, rightX, bottomY)
        sectionRect.set(leftX, topY, rightX, topY + sectionHeight)

        val titleCenterY = (sectionHeight / 2) + (titleTextPaint.fontMetrics.ascent + titleTextPaint.fontMetrics.descent) / 2 + titleVerticalPadding
        val descriptionCenterY = (descriptionTextPaint.fontMetrics.ascent + descriptionTextPaint.fontMetrics.descent) / 2 + sectionHeight + descriptionVerticalPadding

        // Текст
        titlePoint = Point((leftX + horizontalPadding).toInt(), (topY + titleCenterY).toInt())
        descriptionPoint = Point((leftX + horizontalPadding).toInt(), (topY + descriptionCenterY).toInt())
    }

    private fun drawTitle(canvas: Canvas) {
        canvas.drawText(
            title,
            titlePoint.x.toFloat(),
            titlePoint.y.toFloat(),
            titleTextPaint
        )
    }

    private fun drawDescription(canvas: Canvas) {
        // TODO( Доделать здесь )
        val contentWidth = (width - paddingLeft - paddingRight - 2 * horizontalPadding).toInt()
        val staticLayout = StaticLayout.Builder.obtain(description, 0, description.length, descriptionTextPaint, contentWidth)
            .setAlignment(Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .build()

        canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingRight.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }

    private fun drawNoteCard(canvas: Canvas) {
        // Добавляем фон
        canvas.drawRect(cardRect, backgroundPaint)
        // Добавляем верхнюю секцию для заголовка
        canvas.drawRect(sectionRect, sectionPaint)
    }
}