package zarg.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zarg.bank.domain.Account;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {
    Optional<Account> findByBid(String bid);
}
