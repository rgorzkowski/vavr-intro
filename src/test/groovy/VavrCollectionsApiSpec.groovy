import io.vavr.API
import io.vavr.collection.List
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
}
