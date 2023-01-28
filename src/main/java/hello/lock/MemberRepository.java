package hello.lock;

import java.util.Optional;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

  @Lock(LockModeType.OPTIMISTIC)
  @Query("select m from Member m where m.id = :id")
  Optional<Member> findByIdUsingOptimistic(Long id);

  @Lock(LockModeType.PESSIMISTIC_READ)
  @Query("select m from Member m where m.id = :id")
  Optional<Member> findByIdUsingSharedLock(Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select m from Member m where m.id = :id")
  Optional<Member> findByIdUsingExclusiveLock(Long id);

  @Override
  Optional<Member> findById(Long id);
}
