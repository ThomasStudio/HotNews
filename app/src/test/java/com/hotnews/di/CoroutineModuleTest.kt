package com.hotnews.di

import dagger.Provides
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CoroutineModuleTest {

    @Test
    fun `provideIoDispatcher should return IO dispatcher`() {
        // When
        val dispatcher = CoroutineModule.provideIoDispatcher()

        // Then
        assertSame(Dispatchers.IO, dispatcher)
    }


    @Test
    fun `provideIoDispatcher method should have correct annotations`() {
        // Given
        val method = CoroutineModule::class.java.declaredMethods
            .first { it.name == "provideIoDispatcher" }

        // Then
        assertNotNull(method.getAnnotation(Provides::class.java), "Provides annotation missing")

        val qualifierAnnotations = method.annotations
            .filter {
                it.annotationClass.java == Provides::class.java
            }
        assertEquals(1, qualifierAnnotations.size, "IoDispatcher qualifier missing")
    }
}