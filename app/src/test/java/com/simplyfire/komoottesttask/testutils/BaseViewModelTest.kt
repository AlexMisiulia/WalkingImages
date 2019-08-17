package com.simplyfire.komoottesttask.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule

open class BaseViewModelTest {
    @get:org.junit.Rule
    val rule: TestRule = InstantTaskExecutorRule()
}