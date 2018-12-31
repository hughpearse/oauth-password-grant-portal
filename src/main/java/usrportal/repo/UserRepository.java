package usrportal.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  @Query("SELECT e FROM User e WHERE username = ?1")
  List<User> findByUsername(String username);
}
