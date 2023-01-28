package hello.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceWithOptimisticLockTest {

  public static final String FIRST_UPDATE = "첫 번째 스레드";
  public static final String SECOND_UPDATE = "두 번째 스레드";
  @Autowired
  MemberRepository userRepository;

  @Autowired
  MemberService userService;

  Member user;

  @BeforeEach
  void setUp() {
    user = userRepository.save(new Member("tis"));
  }

  private Thread getThread(String firstUpdate) {
    Thread thread = new Thread(new TestThread(firstUpdate));
    thread.setName(firstUpdate);
    return thread;
  }

  private class TestThread implements Runnable {

    private final String name;

    public TestThread(String name) {
      this.name = name;
    }

    @Override
    public void run() {
      System.out.println(name + " : 스레드 시작");
      user = userService.updateUserUsingOptimistic(user.getId(), name);
      System.out.println(user);
      System.out.println(name + " : 스레드 종료");
    }
  }

  @Test
  @DisplayName("평번하게 값을 수정한다.")
  void updateUser() {
    System.out.println(user);
    user = userService.updateUser(user.getId(), "update name");
    System.out.println(user);
  }

  @Test
  @DisplayName("버전을 이용해 값을 확인한다.")
  void first_test() throws InterruptedException {
    Thread firstUpdate = getThread(FIRST_UPDATE);
    Thread secondUpdate = getThread(SECOND_UPDATE);

    System.out.println(user);
    firstUpdate.start();
    Thread.sleep(4000);
    secondUpdate.start();
    Thread.sleep(5000);

  }

  @Test
  @DisplayName("동시에 접근한다.")
  void second_test_with_consistency() throws InterruptedException {
    Thread firstUpdate = getThread(FIRST_UPDATE);
    Thread secondUpdate = getThread(SECOND_UPDATE);

    firstUpdate.start();
    Thread.sleep(100);
    secondUpdate.start();

    Thread.sleep(5000);
  }
}
