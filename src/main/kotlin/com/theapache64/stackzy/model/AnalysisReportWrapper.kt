package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.AnalysisReportDefinition

class AnalysisReportWrapper(
    private val report: AnalysisReport,
    val appLibWrappers: List<LibraryWrapper>,
    val transLibWrappers: List<LibraryWrapper>,
) : AnalysisReportDefinition by report