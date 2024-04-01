package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistorty.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistorty.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistorty.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserService,
  private val userLoanHistoryRepository: UserLoanHistoryRepository
) {
  
  @AfterEach
  fun clean() {
    userRepository.deleteAll()
  }
  
  @Test
  @DisplayName("유저 저장이 정상 동작한다")
  fun saveUserTest() {
    
    //given
    val request = UserCreateRequest("노태원", null)
    
    //when
    userService.saveUser(request)
    
    //then
    val results = userRepository.findAll()
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("노태원")
    assertThat(results[0].age).isNull()
  }
  
  @Test
  @DisplayName("유저 조회가 정상 동작한다")
  fun getUsersTest() {
    
    //given
    userRepository.saveAll(listOf(
      User("a", 20),
      User("B", null)
    ))
    
    //when
    val results = userService.getUsers()
    
    //then
    assertThat(results).hasSize(2) //[UserResponse(), UserResponse()]
    assertThat(results).extracting("name").containsExactlyInAnyOrder("a", "B")
    assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
  }
  
  @Test
  @DisplayName("유저 수정이 정상 동작한다")
  fun updateUserNameTest() {
    
    //given
    val savedUser = userRepository.save(User("A", null))
    val request = UserUpdateRequest(savedUser.id!!, "a")
    
    //when
    userService.updateUserName(request)
    
    //then
    val result = userRepository.findAll()
    assertThat(result[0].name).isEqualTo("a")
  }
  
  @Test
  @DisplayName("유저 삭제가 정상 동작한다")
  fun deleteUserNameTest() {
    
    //given
    userRepository.save(User("ex", 100))
    
    //when
    userService.deleteUser("ex")
    
    //then
    assertThat(userRepository.findAll()).isEmpty()
  }
  
  @Test
  @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
  fun getUserLoanHistoriesTest1() {
    //given
    userRepository.save(User("A", null))
    
    //when
    val results = userService.getUserLoanHistories()
    
    //then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("A")
    assertThat(results[0].books).hasSize(0)
  }
  
  @Test
  @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다")
  fun getUserLoanHistoriesTest2() {
    //given
    val savedUser = userRepository.save(User("A", null))
    userLoanHistoryRepository.saveAll(listOf(
      UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED),
    ))
    
    //when
    val results = userService.getUserLoanHistories()
    
    //then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("A")
    assertThat(results[0].books).hasSize(3)
    assertThat(results[0].books).extracting("name")
      .containsExactlyInAnyOrder("책1", "책2", "책3")
    assertThat(results[0].books).extracting("isReturn")
      .containsExactlyInAnyOrder(false, false, true)
  }
}