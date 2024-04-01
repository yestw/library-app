package com.group.libraryapp.domain.user.loanhistorty

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository: JpaRepository<UserLoanHistory, Long> {

  fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus): UserLoanHistory?
}