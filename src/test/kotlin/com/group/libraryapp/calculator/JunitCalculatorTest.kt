package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JunitCalculatorTest {
  
  @Test
  fun addTest() {
    //given
    val calculator = Calculator(5)
    
    //when
    calculator.add(3)
    
    //then
    assertThat(calculator.number).isEqualTo(8)
    //assertThat(확인하고 싶은 값).isEqualTo(기대값)
    /**
    자주 사용되는 단언 문
      isTrue, isFalse, hasSize,
     */
    
  }
  
  @Test
  fun divTest() {
    //given
    val calculator = Calculator(5)
    //when
    calculator.div(2)
    
    //then
    assertThat(calculator.number).isEqualTo(2)
  }
  
  @Test
  fun divExceptionTest() {
    //given
    val calculator = Calculator(5)
    
    //when & then
    val message = assertThrows<IllegalArgumentException> {
      calculator.div(0)
    }.message
    
    assertThat(message).isEqualTo("0으로 나눌 수 없습니다.")
    
//    assertThrows<IllegalArgumentException> {
//      calculator.div(0)
//    }.apply {
//      assertThat(message).isEqualTo("0으로 나눌 수 없습니다.")
//    }
  }
}