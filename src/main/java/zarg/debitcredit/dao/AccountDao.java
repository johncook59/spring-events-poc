package zarg.debitcredit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zarg.debitcredit.domain.Account;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {
    Optional<Account> findByBid(String bid);

    @Query(value = "SELECT * FROM account WHERE bid = ?1 FOR NO KEY UPDATE", nativeQuery = true)
    Optional<Account> findAndLockByBid(String bid);
}
