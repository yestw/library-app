package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistorty.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistorty.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
  private val bookRepository: BookRepository,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
  
  @Transactional
  fun saveBook(request: BookRequest) {
    val newBook = Book(request.name, request.type)
    bookRepository.save(newBook)
  }
  
  @Transactional
  fun loanBook(request: BookLoanRequest) {
    val book = bookRepository.findByName(request.bookName) ?: fail()
    if(userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
      throw IllegalArgumentException("진작 대출되어 있는 책입니다.")
    }
    val user = userRepository.findByName(request.userName) ?: fail()
    user.loanBook(book)
  }
  
  @Transactional
  fun returnBook(request: BookReturnRequest) {
    val user = userRepository.findByName(request.userName) ?: fail()
    user.returnBook(request.bookName)
  }
  
  @Transactional(readOnly = true)
  fun countLoanBook(): Int {
    return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
  }
  
  @Transactional(readOnly = true)
  fun getBookStatistics(): List<BookStatResponse> {
    return bookRepository.getStats()
    
    // db 활용한 쿼리 이전의 방법
//    return bookRepository.findAll()
//      .groupBy { book -> book.type }
//      .map { (type, books) -> BookStatResponse(type, books.size)}
    
    //위 코드 3줄과 동일한 결과를 반환하는 코드
    //위와 같이 구현하면 dto에서 불변 필드를 사용하고, plusOne이라는 함수도 없앨 수 있음
//    val results = mutableListOf<BookStatResponse>()
//    val books = bookRepository.findAll()
//    for(book in books) {
//      results.firstOrNull {dto -> book.type == dto.type}?.plusOne()
//        ?: results.add(BookStatResponse(book.type, 1))
//    }
//    return results
  }
}