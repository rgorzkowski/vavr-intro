import io.vavr.API
import io.vavr.collection.List
import io.vavr.control.Option
import spock.lang.Specification

class VavrCollectionsApiSpec extends Specification {

    def "should test vavr list"() {
        given:
            def list = List.of(1, 2, 3, 1)
        when:
            def modifiedList = list.removeLast { it -> 1.equals(it) } // groovy way
        then:
            list == API.List(1, 2, 3, 1)
            modifiedList == API.List(1, 2, 3)
    }

    def "should test optional's map"() {
        expect:
            "ALA MA KOTA" == Option.of("ala ma kota")
                    .map { it -> it.toUpperCase() }
                    .get()
        and:
            null == Option.of(null)
                    .map { it -> it.toUpperCase() }
                    .getOrNull()
    }

    def "should test optional's iterable"() {
        when:
            List<Integer> list = List.of(Option.of(100), Option.of(200))
                    .flatMap { it -> it }
        then:
            List.of(100, 200) == list

    }


}
