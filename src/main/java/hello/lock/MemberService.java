package hello.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository userRepository;

  @Transactional
  public Member updateUser(Long id, String name){
    Member user = userRepository.findById(id).orElseThrow();
    user.updateName(name);
    return user;
  }
  @Transactional
  public Member updateUserUsingOptimistic(Long id, String name){
    Member user = userRepository.findByIdUsingOptimistic(id).orElseThrow();
    user.updateName(name);
    sleep();
    return user;
  }

  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException();
    }
  }

  @Transactional
  public Member updateUserUsingSharedLock(Long id, String name) {
    Member user = userRepository.findByIdUsingSharedLock(id).orElseThrow();
    user.updateName(name);
    return user;
  }

  @Transactional
  public Member updateUserUsingExclusiveLock(Long id, String name) {
    Member user = userRepository.findByIdUsingExclusiveLock(id).orElseThrow();
    user.updateName(name);
    return user;
  }
}
