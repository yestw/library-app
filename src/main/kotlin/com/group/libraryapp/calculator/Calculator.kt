package com.group.libraryapp.calculator

data class Calculator(
  var number: Int,
) {

  //custom getter
//  val number: Int
//    get() = this.number
  
  fun add(operand: Int) {
    this.number += operand
  }
  fun minus(operand: Int) {
    this.number -= operand
  }
  fun mul(operand: Int) {
    this.number *= operand
  }
  fun div(operand: Int) {
    if(operand == 0) {
      throw IllegalArgumentException("0으로 나눌 수 없습니다.")
    }
    this.number /= operand
  }
}