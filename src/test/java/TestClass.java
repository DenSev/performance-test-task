import org.junit.Test;

public class TestClass {

    @Test
    public void test() {
        System.out.println(new IllegalStateException() instanceof Throwable);
    }
}
