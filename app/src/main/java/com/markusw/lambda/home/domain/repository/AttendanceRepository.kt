package com.markusw.lambda.home.domain.repository

import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.data.model.AttendanceDto

interface AttendanceRepository {
    suspend fun registerAttendance(attendanceDto: AttendanceDto): Result<Unit>
}