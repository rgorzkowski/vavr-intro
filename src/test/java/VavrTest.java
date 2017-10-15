import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;
import java.util.function.Supplier;

import io.vavr.API;
import io.vavr.Lazy;
import io.vavr.Predicates;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
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

    @Test
    public void crossProduct() {
        //given
        List<Integer> originalList = List.of(1, 2, 3, 4, 5);

        //when, then
        originalList.crossProduct(2).toJavaStream().forEach(System.out::println);
    }

    @Test
    public void zipTwoCollections() {
        //given
        List<Integer> first = List.of(1, 2, 3, 4, 5);
        List<String> second = List.of("A", "B");

        //when, then
        System.out.println(first.zip(second));
    }

    @Test
    public void zipWithIndex() {
        //given
        List<Integer> first = List.of(1, 2, 3, 4, 5);

        //when, then
        System.out.println(first.zipWithIndex());
    }

    @Test
    public void vavrStream() {
        //given
        Stream<Integer> stream = Stream.iterate(0, i -> i + 1)
                .take(10);

        //when, then
        System.out.println(stream);
    }

    @Test
    public void asJavaExample() {
        //given
        java.util.List<Integer> list = List.of(1, 2, 3, 4, 5).asJava();

        //when, then
        System.out.println(list);
    }

    @Test
    public void tuple() {
        //given
        Tuple2<String, Integer> tuple = Tuple.of("Java", 8);

        //when
        String result1 = tuple.apply((s, integer) -> s + " " + integer);
        Tuple2<String, Integer> tupleRes = tuple.map(s -> s.substring(0, 1), Function.identity());
        String result2 = tupleRes.apply((s, integer) -> s + " " + integer);

        //then
        assertThat(result1).isEqualTo("Java 8");
        assertThat(result2).isEqualTo("J 8");
    }

    @Test
    public void catchExceptions() {
        List.of(null, "")
                //                .map(s -> new URI(s))
                .map(API.unchecked(s -> new URI(s)))
                .get().getHost();
    }

    @Test
    public void patterMatching() {
        //given
        Object i = "42";

        //when
        String result = Match(i).of(
                Case($(Predicates.instanceOf(String.class)), "string"),
                Case($(Predicates.instanceOf(Integer.class)), "int")
        );

        //then
        assertThat(result).isEqualTo("string");
    }

}
