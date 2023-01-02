package com.hashem.mousavi.beziercurves

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
@Composable
fun CubicBezierCurve(
    modifier: Modifier,
    points: List<Offset>,
    onAddPoint: (Offset) -> Unit,
    onNewPosition: (offset: Offset, index: Int) -> Unit
) {

    var width by remember {
        mutableStateOf(0f)
    }
    var height by remember {
        mutableStateOf(0f)
    }

    val textMeasurer = rememberTextMeasurer()

    val circleRadius = with(LocalDensity.current) { 10.dp.toPx() }

    val animate = remember {
        Animatable(initialValue = 0f)
    }

    var draggedIndex by remember {
        mutableStateOf(-1)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectTapGestures(
                    onPress = { offset ->
                        if (!points.contains(offset) && (points.size < 4)) {
                            onAddPoint(offset)
                        } else if (points.size == 4) {
                            points
                                .find {
                                    offset
                                        .minus(it)
                                        .getDistance() < circleRadius
                                }
                                ?.let {
                                    draggedIndex = points.indexOf(it)
                                }
                        }
                    }
                )
            }
            .pointerInput(true) {
                detectDragGestures(
                    onDragEnd = {
                        draggedIndex = -1
                    },
                    onDrag = { change, dragAmount ->
                        if (draggedIndex != -1) {
                            val newOffset = points[draggedIndex] + dragAmount
                            onNewPosition(newOffset, draggedIndex)
                        }
                    }
                )
            }
    ) {
        width = this.size.width
        height = this.size.height

        points.forEachIndexed { index, offset ->
            if (index < points.size - 1) {
                drawLine(
                    color = Color.LightGray,
                    start = points[index],
                    end = points[index + 1],
                    strokeWidth = circleRadius,
                    cap = StrokeCap.Round
                )
            }

            drawCircle(
                color = if (draggedIndex == index) Color.Green else Color.Red,
                radius = circleRadius,
                center = offset,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(0.5f, 0.5f)
                    )
                )
            )
            val layoutResult = textMeasurer.measure("P${index}".toAnnotatedString())
            drawText(
                textLayoutResult = layoutResult,
                topLeft = offset.minus(
                    Offset(
                        x = layoutResult.size.width / 2f,
                        y = layoutResult.size.height + circleRadius
                    )
                )
            )

            val path = Path().apply {
                moveTo(points[0].x, points[0].y)
                if (points.size == 4) {
                    cubicTo(
                        x1 = points[1].x,
                        y1 = points[1].y,
                        x2 = points[2].x,
                        y2 = points[2].y,
                        x3 = points[3].x,
                        y3 = points[3].y,
                    )
                }
            }

            drawPath(
                path = path,
                color = Color.Blue,
                style = Stroke(width = 1.dp.toPx())
            )

        }


    }
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = Color.DarkGray, fontSize = 12.sp)
        ) {
            append(this@toAnnotatedString)
        }
    }
}

@Preview
@Composable
fun BezierCurvePreview() {
    // BezierCurve(modifier = Modifier.size(300.dp), uiState = UIState.Idle)
}