package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.enums.Role;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    boolean existsByMail(String mail);
    boolean existsByMailAndIdNot(String mail, int id);
    Account findByPhone(String phone);
    Optional<Account> findByMail(String email);

    boolean existsByPhone(String phone);
    boolean existsByPhoneAndIdNot(String phone,int id);

    List<Account> findAllByRole(Role role);
}
