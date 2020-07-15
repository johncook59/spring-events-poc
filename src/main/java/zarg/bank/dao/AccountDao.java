package zarg.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zarg.bank.domain.Account;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {
    @Query("select a.id from Account a")
    List<Integer> findAllKeys();

    Account findByBid(String bid);
}
