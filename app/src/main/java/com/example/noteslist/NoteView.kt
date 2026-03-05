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

    private var defaultTitleTopPadding = 20f
    private var defaultDescriptionTopPadding = 24f
    private var defaultDateBottomPadding = 20f
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

    private var titleTopPadding = defaultTitleTopPadding
    private var descriptionTopPadding = defaultDescriptionTopPadding
    private var dateBottomPadding = defaultDateBottomPadding
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
    private var textWidth = 0
    private var titlePoint = Point()
    private var descriptionPoint = Point()
    private var datePoint = Point()
    private var cardRect = RectF()
    private var sectionRect = RectF()

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val sectionPaint = Paint().apply { isAntiAlias = true }
    private val titleTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val descriptionTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }
    private val dateTextPaint = TextPaint().apply { isAntiAlias = true; textAlign = Paint.Align.LEFT }

    init {
        val resources = context.resources
        resources.apply {
            defaultHeight = resources.getDimension(R.dimen.note_view_height)
            defaultSectionHeight = resources.getDimension(R.dimen.note_section_height)
            defaultTitleTextSize = resources.getDimensionPixelSize(R.dimen.note_title_size).toFloat()
            defaultDescriptionTextSize = resources.getDimensionPixelSize(R.dimen.note_description_size).toFloat()
            defaultDateTextSize = resources.getDimensionPixelSize(R.dimen.note_date_size).toFloat()

            defaultTitleTopPadding = resources.getDimension(R.dimen.note_title_top_padding)
            defaultDescriptionTopPadding = resources.getDimension(R.dimen.note_description_top_padding)
            defaultDateBottomPadding = resources.getDimension(R.dimen.note_date_bottom_padding)
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

        titleTopPadding = defaultTitleTopPadding
        descriptionTopPadding = defaultDescriptionTopPadding
        dateBottomPadding = defaultDateBottomPadding
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
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGeometry()

        drawNoteCard(canvas)
        drawTitle(canvas)
        drawDescription(canvas)
        drawDate(canvas)
    }

    private fun updateGeometry() {
        val leftPadding = paddingLeft.toFloat()
        val topPadding = paddingTop.toFloat()
        val rightPadding = paddingRight.toFloat()
        val bottomPadding = paddingBottom.toFloat()

        val leftX = leftPadding
        val topY = topPadding
        val rightX = width - rightPadding
        val bottomY = topPadding + viewHeight

        val contentWidth = width - leftPadding.toInt() - rightPadding.toInt()
        val contentHeight = height - topPadding.toInt() - bottomPadding.toInt()

        center.x = (leftPadding + contentWidth / 2).toInt()
        center.y = (topPadding + contentHeight / 2).toInt()

        cardPath.set(Path())

        // Фон
        cardRect.set(leftX, topY, rightX, bottomY)
        sectionRect.set(leftX, topY, rightX, topY + sectionHeight)


        // Текст
        val titleCenterY = (sectionHeight / 2) + (titleTextPaint.fontMetrics.ascent + titleTextPaint.fontMetrics.descent) / 2 
        val descriptionCenterY = (descriptionTextPaint.fontMetrics.ascent + descriptionTextPaint.fontMetrics.descent) / 2 + sectionHeight + descriptionTopPadding
        val dateCenterY = (dateTextPaint.fontMetrics.ascent + dateTextPaint.fontMetrics.descent) / 2 - dateBottomPadding

        textWidth = (width - paddingLeft - paddingRight - 2 * horizontalPadding).toInt()

        titlePoint = Point((leftX + horizontalPadding).toInt(), (topY + titleCenterY).toInt())
        descriptionPoint = Point((leftX + horizontalPadding).toInt(), (topY + descriptionCenterY).toInt())
        datePoint = Point((leftX + horizontalPadding).toInt(), (bottomY + dateCenterY).toInt())
    }

    private fun drawTitle(canvas: Canvas) {
        val staticLayout = StaticLayout.Builder.obtain(title, 0, title.length, titleTextPaint, textWidth)
            .setAlignment(Alignment.ALIGN_NORMAL)
            .setMaxLines(1)
            .build()

        canvas.save()
        canvas.translate(titlePoint.x.toFloat(), titlePoint.y.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
//        canvas.drawText(title, titlePoint.x.toFloat(), titlePoint.y.toFloat(), titleTextPaint)

    }

    private fun drawDescription(canvas: Canvas) {
        val staticLayout = StaticLayout.Builder.obtain(description, 0, description.length, descriptionTextPaint, textWidth)
            .setAlignment(Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .build()

        canvas.save()
        canvas.translate(descriptionPoint.x.toFloat(), descriptionPoint.y.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }

    private fun drawDate(canvas: Canvas) {
        canvas.drawText(date, datePoint.x.toFloat(), datePoint.y.toFloat(), dateTextPaint)
    }

    private fun drawNoteCard(canvas: Canvas) {
        // Добавляем фон
        canvas.drawRect(cardRect, backgroundPaint)
        // Добавляем верхнюю секцию для заголовка
        canvas.drawRect(sectionRect, sectionPaint)
    }
}