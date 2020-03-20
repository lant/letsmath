package com.github.lant.letsmath

import com.github.lant.letsmath.com.github.lant.letsmath.OperationsController
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OperationsControllerTest {

  @Test
  fun testMultiplier() {
    val operationsController = OperationsController()

    val multiplication = operationsController.getMultiplication(2, 3)
    assertEquals(multiplication.operation, "2 + 2 + 2")
    assertEquals(multiplication.result, 6)
  }
}