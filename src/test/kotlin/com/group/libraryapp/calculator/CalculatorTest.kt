package com.group.libraryapp.calculator

import kotlin.math.exp

fun main() {
  val test = CalculatorTest()
  test.addTest()
  test.divTest()
  test.divExceptionTest()
}

class CalculatorTest {
  
  fun addTest() {
    
    //given - 테스트 준비
    val calculator = Calculator(5)
    calculator.add(3)
    
    //when - 테스트 하는 기능 호출
    val expectedCalculator = Calculator(8)

    //then - 테스트 결과 확인
    /**
     1. data class 를 사용해서 검증 할 수 있고
     2. 생성자 필드를 public으로 사용해서 검증 할 수도 있고 -> 대신 setter사용을 하지 않아야 함
     3. custom getter를 만들어도 됨 -> 코틀린 컨벤션: 생성자 필드는 앞에 _(언더바) 붙이기
     */
    if (calculator != expectedCalculator) {
      throw IllegalArgumentException("add test fail")
    }
  }
  
  fun divTest() {
    val calculator = Calculator(5)
    
    calculator.div(2)
    
    if(calculator.number != 2) {
      throw IllegalArgumentException("div test fail")
    }
  }
  
  fun divExceptionTest() {
    val calculator = Calculator(5)

    try {
      calculator.div(0)
    } catch (e: IllegalArgumentException) {
      if(e.message != "0으로 나눌 수 없습니다"){
        throw IllegalArgumentException("메시지가 다릅니다.")
      }
      
      //테스트 성공
      return
    } catch (e: Exception) {
      //테스트 실패
      throw IllegalArgumentException()
    }
    
    throw IllegalArgumentException("기대하는 예외가 발생하지 않았습니다.")
    
  }
}