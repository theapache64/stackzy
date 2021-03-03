package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.GradleInfo
import com.theapache64.stackzy.ui.theme.*
import com.theapache64.stackzy.ui.util.Preview


private val firaCode by lazy {
    FontFamily(
        Font("fonts/FiraCode-Regular.ttf")
    )
}


private val codeViewerPadding = 10.dp
private val codeViewerTitleContentSpacerHeight = 4.dp
private val codeViewerFontSize = 14.sp

@Composable
fun MoreInfo(
    report: AnalysisReport,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            BuildGradleGroovy(
                packageName = report.packageName,
                gradleInfo = report.gradleInfo
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            PermissionsXml(
                permission = report.permissions
            )
        }
    }
}

@Composable
private fun BuildGradleGroovy(
    modifier: Modifier = Modifier,
    packageName: String,
    gradleInfo: GradleInfo
) {
    Column(
        modifier = modifier
    ) {

        // Title
        Text(
            text = "build.gradle",
            style = MaterialTheme.typography.body2
        )

        Spacer(
            modifier = Modifier.height(codeViewerTitleContentSpacerHeight)
        )

        Text(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary, RoundedCornerShape(5.dp))
                .padding(codeViewerPadding),
            text = with(AnnotatedString.Builder("")) {

                // line #1
                pushStyle(SpanStyle(JordyBlue))
                append("android")
                pushStyle(SpanStyle(Color.White))
                append(" {\n")

                // line #2
                pushStyle(SpanStyle(JordyBlue))
                append("  defaultConfig")
                pushStyle(SpanStyle(Color.White))
                append(" {\n")

                // line #3
                pushStyle(SpanStyle(JordyBlue))
                append("    applicationId")
                pushStyle(SpanStyle(YellowGreen))
                append(" \"${packageName}\"")

                gradleInfo.minSdk?.let { (minSdkInt, minSdkVersionName) ->
                    // line #4
                    pushStyle(SpanStyle(JordyBlue))
                    append("\n    minSdkVersion")
                    pushStyle(SpanStyle(WildWatermelon))
                    append(" $minSdkInt")

                    // sdk version as comment
                    pushStyle(SpanStyle(BlueBayoux))
                    append(" // $minSdkVersionName")
                }

                gradleInfo.targetSdk?.let { (targetSdkInt, targerSdkVersionName) ->
                    // line #5
                    pushStyle(SpanStyle(JordyBlue))
                    append("\n    targetSdkVersion")
                    pushStyle(SpanStyle(WildWatermelon))
                    append(" $targetSdkInt")

                    // sdk version as comment
                    pushStyle(SpanStyle(BlueBayoux))
                    append(" // $targerSdkVersionName")
                }

                gradleInfo.versionCode?.let { versionCode ->
                    // line #6
                    pushStyle(SpanStyle(JordyBlue))
                    append("\n    versionCode")
                    pushStyle(SpanStyle(WildWatermelon))
                    append(" $versionCode")
                }


                gradleInfo.versionName?.let { versionName ->
                    // line #6
                    pushStyle(SpanStyle(JordyBlue))
                    append("\n    versionName")
                    pushStyle(SpanStyle(YellowGreen))
                    append(" \"$versionName\"")
                }

                pushStyle(SpanStyle(Color.White))
                append("\n  }\n")
                append("}")

                toAnnotatedString()
            },
            fontFamily = firaCode,
            fontSize = codeViewerFontSize
        )
    }
}

@Composable
private fun PermissionsXml(
    modifier: Modifier = Modifier,
    permission: List<String>
) {
    Column(modifier) {

        // Title
        Text(
            text = "AndroidManifest.xml",
            style = MaterialTheme.typography.body2
        )

        Spacer(
            modifier = Modifier.height(codeViewerTitleContentSpacerHeight)
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary, RoundedCornerShape(5.dp))
                .padding(codeViewerPadding)
        ) {

            // Permissions
            permission.forEach { permission ->
                Text(
                    text = with(AnnotatedString.Builder("")) {
                        pushStyle(SpanStyle(color = WildWatermelon))
                        append("<uses-permission ")
                        pushStyle(SpanStyle(color = Goldenrod))
                        append("android:name")
                        pushStyle(SpanStyle(color = Color.White))
                        append("=")
                        pushStyle(SpanStyle(color = YellowGreen))
                        append("\"$permission\"")
                        pushStyle(SpanStyle(color = WildWatermelon))
                        append("/>")
                        toAnnotatedString()
                    },
                    fontFamily = firaCode,
                    fontSize = codeViewerFontSize
                )
            }
        }
    }
}