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

    companion object {

    }
    // Defaults
    private val defaultTitleTextColor = Color.BLACK
    private val defaultDescriptionTextColor = Color.GRAY
    private val defaultBackgroundColor = Color.WHITE
    private val defaultTitleSectionColor = Color.BLUE
    private val defaultTitleTextSize = 100f
    private val defaultDescriptionTextSize = 50f
    private val defaultTitlePadding = 20f


    // Стейт
    private val titleTextColor = defaultTitleTextColor
    private val descriptionTextColor = defaultDescriptionTextColor
    private val backgroundColor = defaultBackgroundColor
    private val titleSectionColor = defaultTitleSectionColor
    private val titleTextSize = defaultTitleTextSize
    private val descriptionTextSize = defaultDescriptionTextSize
    private val titlePadding = 20f

    // Геометрия
    private val center = Point()
    private val cardRect = RectF()
    private val sectionRect = RectF()
    private val shadowRadius = 10f

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val sectionPaint = Paint().apply { isAntiAlias = true }
    private val titleTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val descriptionTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val dateTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    init {
        initPaints()
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
            setShadowLayer()
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