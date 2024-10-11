package com.markusw.lambda.core.utils.ext

import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Mentoring

fun Mentoring.toDto(): MentoringDto = MentoringDto(
    price = this.price,
    requesterId = this.requester.id,
    requesterDescription = this.requesterDescription,
    title = this.title,
    description = this.description,
    coverUrl = this.coverUrl,
    topic = this.topic,
    state = this.state
)