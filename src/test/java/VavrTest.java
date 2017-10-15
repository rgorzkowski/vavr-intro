import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;

import io.vavr.Lazy;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VavrTest {

    @Test
    public void shouldTestOption() {
        //given
        List<Integer> integers = List.of(Option.of(100), Option.of(200))
                .flatMap(it -> it);

        //when, then
        assertThat(integers).containsExactly(100, 200);
    }

    @Test
    public void testTry() {
        //given, when
        String result = Try.of(() -> new URI(""))
                .recoverWith(URISyntaxException.class, Try.of(() -> new URI("")))
                .map(uri -> uri.toString())
                .filter(i -> true)
                .getOrElse("default");

        //then
        assertThat(result).isEqualTo("");
    }

    @Test
    public void showJava8LazyInitializationEveryTimeWeInvokeGetMethod() {
        Supplier<Integer> sup = () -> {
            System.out.println("invoking");
            return 100;
        };

        sup.get();
        sup.get();
        sup.get();

    }

    @Test
    public void showVavrCache() {
        Lazy<Integer> lazy = Lazy.of(() -> {
            System.out.println("invoking");
            return getLazyValue();
        }).map(i -> {
            System.out.println("Mapping i " + i);
            return i + 1;
        });

        System.out.println(lazy.get());
        System.out.println(lazy.get());
        System.out.println(lazy.get());
    }

    private Integer getLazyValue() {
        System.out.println("Invoked getLazyValue");
        Integer val = (int) (Math.random() * 100);
        System.out.println("Lazy value " + val);
        return val;
    }

    @Test
    public void showVavrPersistentCollection() {
        //given
        List<Integer> originalList = List.of(1, 2, 3, 4, 5);

        //when, then
        assertThat(originalList.dropUntil(i -> i > 3)).containsExactly(4, 5);
        assertThat(originalList.drop(2)).containsExactly(3, 4, 5);

        assertThat(originalList).containsExactly(1, 2, 3, 4, 5);
    }

}
