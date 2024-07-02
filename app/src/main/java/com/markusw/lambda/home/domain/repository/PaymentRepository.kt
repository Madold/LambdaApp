package com.markusw.lambda.home.domain.repository

import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.utils.Result

interface PaymentRepository {

    suspend fun payMentoringAccess(mentoring: Mentoring, user: User): Result<Unit>

}