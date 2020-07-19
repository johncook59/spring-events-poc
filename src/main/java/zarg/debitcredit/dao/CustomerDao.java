package zarg.debitcredit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zarg.debitcredit.domain.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String name);
}
