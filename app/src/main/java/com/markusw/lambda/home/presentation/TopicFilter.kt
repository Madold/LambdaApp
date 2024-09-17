package com.markusw.lambda.home.presentation

sealed class TopicFilter(val label: String) {

    companion object {
        val entries get() = listOf(
            Math,
            Physics,
            Chemistry,
            Technology,
            Coding,
            CriticalReading,
            Kitchen,
            Music
        )
    }

    data object Math: TopicFilter(label =  "Matemáticas")
    data object Physics: TopicFilter(label =  "Física")
    data object Chemistry: TopicFilter(label =  "Química")
    data object Technology: TopicFilter(label =  "Informática")
    data object Coding: TopicFilter(label =  "Programación")
    data object CriticalReading: TopicFilter(label =  "Lectura Crítica")
    data object Kitchen: TopicFilter(label =  "Cocina")
    data object Music: TopicFilter(label =  "Musica")
}