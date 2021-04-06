package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.AnalysisReportDefinition

class AnalysisReportWrapper(
    private val report: AnalysisReport,
    val libraryWrappers: List<LibraryWrapper>
) : AnalysisReportDefinition by report