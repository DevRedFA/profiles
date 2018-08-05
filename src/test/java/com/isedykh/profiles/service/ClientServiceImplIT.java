package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cern.jet.math.Functions.sin;

//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
////@DataJpaTest
//@SpringBootTest
//@RequiredArgsConstructor
//@ContextConfiguration(classes = {ClientServiceImpl.class, PersonEntityRepository.class})
public class ClientServiceImplIT {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientEntityRepository clientEntityRepository;


    private String printData(String data) {
        System.out.println(data);
        return data;

    }

    private <I, O> void printFunc(Supplier<O> supplier) {
        System.out.println(supplier.get());
    }

    @Test
    public void savePerson() {
        printFunc(() -> printData("two"));
    }

    @Test
    public void findByPhone() {
        Consumer<Integer> printer = System.out::println;
        Consumer<Integer> devNull = (val) -> { int v = val * 2; };

//        Consumer<Integer> combinedConsumer = devNull.andThen(devNull.andThen(printer));
        Consumer<Integer> combinedConsumer = devNull.andThen(printer);
        combinedConsumer.accept(100);

      }

    public static IntPredicate disjunctAll(List<IntPredicate> predicates) {
        return predicates.stream().reduce((in) -> false, IntPredicate::or);
    }


    ;
    @FunctionalInterface
    public interface TernaryIntPredicate {
        boolean test(int a, int b, int c);
// Write a method here
    }

    public static final TernaryIntPredicate allValuesAreDifferentPredicate = (a,b,c) -> a!=b && b!=c && a!=c;
    class Account {
        String number;
        Long balance;
        boolean isLocked;

        public String getNumber() {
            return number;
        }

        public Long getBalance() {
            return balance;
        }

        public boolean isLocked() {
            return isLocked;
        }
    }

    @Test
    public void findByPhoneSecond() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 10)

                .thenCompose(result ->
                        CompletableFuture.supplyAsync(() -> result * 2)

                ).thenApply(integer -> (integer + 5));
        ;

        System.out.println(future2.get());

    }

}
