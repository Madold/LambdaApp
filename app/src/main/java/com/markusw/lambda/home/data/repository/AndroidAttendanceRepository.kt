package com.markusw.lambda.home.data.repository

import android.content.Context
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.data.model.AttendanceDto
import com.markusw.lambda.home.domain.repository.AttendanceRepository

class AndroidAttendanceRepository(
    private val remoteDatabase: RemoteDatabase,
    private val localDatabase: LocalDatabase,
    private val context: Context
) : AttendanceRepository {

    override suspend fun registerAttendance(attendanceDto: AttendanceDto): Result<Unit> {
        return try {
            val isUserAlreadyAttended = localDatabase.checkUserAttendance(attendanceDto)

            if (!isUserAlreadyAttended) {
                remoteDatabase.registerAttendanceDto(attendanceDto)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

}