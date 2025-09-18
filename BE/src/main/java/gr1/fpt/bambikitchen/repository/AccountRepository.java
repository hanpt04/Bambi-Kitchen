package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    boolean existsByMail(String mail);
}
