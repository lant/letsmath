package com.github.lant.letsmath.com.github.lant.letsmath

import java.lang.IllegalStateException
import java.util.Random

class OperationsController {

  private val rd = Random(System.nanoTime())

  fun getOperations(number: Int): List<Operation> {
    return (0..number).map { getSingleOperation() }
  }

  private fun getSingleOperation(): Operation {
    return when (rd.nextInt(3)) {
      0 -> getSum()
      1 -> getSubstract()
      2 -> getMultiplication()
      else -> throw IllegalStateException()
    }
  }

  private fun getMultiplication(): OperationsController.Operation {
    val multiplier = rd.nextInt(20) + 1
    val factor = rd.nextInt(7 ) + 2

    return getMultiplication(multiplier, factor)
  }

  fun getMultiplication(multiplier: Int, factor: Int): Operation {
    val array = mutableListOf<String>()

    repeat(factor) {
      array.add(multiplier.toString())
    }

    val stringRepresentation = array.toTypedArray().joinToString(" + ")

    return Operation(stringRepresentation, multiplier * factor)
  }

  private fun getSubstract(): OperationsController.Operation {
    val a = rd.nextInt(40) + 1
    val b = rd.nextInt(15) + 1

    val minuend = a + b
    return Operation("$minuend - $b", a)
  }

  private fun getSum(): OperationsController.Operation {
    val a = rd.nextInt(50) + 1
    val b = rd.nextInt(50) + 1
    return Operation("$a + $b", a + b)
  }

  fun evaluate(operation: String, expected: Int, provided: Int): Result {
    return Result(operation, expected, provided, expected == provided)
  }

  data class Operation(val operation: String, val result: Int)
  data class Result(val operation: String, val provided: Int, val expected: Int, val valid: Boolean)
}
