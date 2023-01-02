package com.hashem.mousavi.beziercurves

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hashem.mousavi.beziercurves.ui.theme.BezierCurvesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BezierCurvesTheme {

                val points = remember {
                    mutableStateListOf<Offset>()
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CubicBezierCurve(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(color = Color.Yellow.copy(alpha = 0.4f))
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        points = points,
                        onAddPoint = {
                            points.add(it)
                        },
                        onNewPosition = { offset: Offset, index: Int ->
                            points[index] = offset
                        }
                    )


                    Button(
                        onClick = {
                            points.clear()
                        }
                    ) {
                        Text(text = "Clear")
                    }
                }

            }
        }
    }
}
