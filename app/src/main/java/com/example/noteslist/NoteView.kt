package com.example.noteslist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.apply

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    // Defaults
    private val defaultHeight = 200f
    private val defaultSectionHeight = 50f
    private val defaultTitleTextSize = 20f
    private val defaultDescriptionTextSize = 50f
    private val defaultDateTextSize = 20f

    private val defaultTitleVerticalPadding = 20f
    private val defaultDescriptionVerticalPadding = 24f
    private val defaultDateVerticalPadding = 20f
    private val defaultHorizontalPadding = 14f

    private val defaultBackgroundColor = Color.WHITE
    private val defaultSectionColor = Color.BLUE
    private val defaultTitleColor = Color.BLACK
    private val defaultDescriptionColor = Color.GRAY
    private val defaultDateColor = Color.GRAY

    private var defaultTitle = "Заголовок"
    private var defaultDescription = "Описание"
    private var defaultDate = "01.01.2025"
    private var defaultImportance = false

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
    private var center = Point()
    private var cardRect = RectF()
    private var sectionRect = RectF()
    private var shadowRadius = 10f

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val sectionPaint = Paint().apply { isAntiAlias = true }
    private val titleTextPaint = Paint().apply { isAntiAlias = true; textAlign = Paint.Align.CENTER }
    private val descriptionTextPaint = Paint().apply { isAntiAlias = true; textAlign = Paint.Align.CENTER }
    private val dateTextPaint = Paint().apply { isAntiAlias = true; textAlign = Paint.Align.CENTER }

    init {
        initPaints()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {

            }
        }
    }

    private fun initPaints() {
        titleTextPaint.apply {
            style = Paint.Style.FILL
            color = titleTextColor
            textSize = this@NoteView.titleTextSize
        }
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = backgroundColor
//            setShadowLayer()
        }
        sectionPaint.apply {
            style = Paint.Style.FILL
            color = titleSectionColor
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGeometry()
        drawNoteCard(canvas)
//        drawTitle(canvas)
    }

    private fun updateGeometry() {
        val leftPadding = paddingLeft.toFloat()
        val topPadding = paddingTop.toFloat()
        val rightPadding = paddingRight.toFloat()
        val bottomPadding = paddingBottom.toFloat()

        val contentWidth = width - leftPadding.toInt() - rightPadding.toInt()
        val contentHeight = height - topPadding.toInt() - bottomPadding.toInt()

        center.x = (leftPadding + contentWidth / 2).toInt()
        center.y = (topPadding + contentHeight / 2).toInt()

        val sectionHeight = (titlePadding * 2 + titleTextSize)

        val leftX = (leftPadding)
        val topY = (topPadding)
        val rightX = (width - rightPadding.toInt()).toFloat()
        val bottomY = (height - bottomPadding.toInt()).toFloat()

        sectionRect.set(leftX, topY, rightX, topY + sectionHeight + 50f) // эти 50f закроются фоном
        cardRect.set(leftX, topY + sectionHeight, rightX, bottomY)
    }

    private fun drawTitle(canvas: Canvas) {
        canvas.drawText(
            "isdfhai",
            center.x.toFloat(),
            center.y.toFloat(),
            titleTextPaint
        )
    }

    private fun drawNoteCard(canvas: Canvas) {
        // Добавляем верхнюю секцию для заголовка
        canvas.drawRoundRect(sectionRect, 50f, 50f, sectionPaint)
        // Добавляем фон
        canvas.drawRoundRect(cardRect, 50f, 50f, backgroundPaint)
    }
}