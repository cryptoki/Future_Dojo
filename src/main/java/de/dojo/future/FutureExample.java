package de.dojo.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FutureExample {

    List<String> TEST = Arrays.asList("This", "is", "a", "stupid", "testcase", "without", "any", "content", ".");
    Supplier<List<String>> gen2 = () -> TEST.stream().distinct().collect(Collectors.toList());

    Supplier<List<String>> generate() {
        ArrayList<String> result = new ArrayList<>();

        TEST.stream().forEach(x ->  {
            System.out.println("generate add '" + x + "'. " + Thread.currentThread() );
            result.add(x);
            try {
                System.out.println("generate sleep " + Thread.currentThread());
                Thread.sleep(1000);
                System.out.println("generate done " + Thread.currentThread());
            }
            catch(Exception exc ) {}
        });

        return () -> result;
    }

    Function<List<String>, Integer> sumUp = (x) -> {
        int i= 0;
        for(String tmp : x) {
            System.out.println("sumUp add '" + tmp + "'. " + Thread.currentThread() );
            i += tmp.length();

            try {
                System.out.println("sumUp sleep " + Thread.currentThread());
                Thread.sleep(1000);
                System.out.println("sumUp done " + Thread.currentThread());
            }
            catch(Exception exc ) {}
        }
        return i;
    };

    Function<List<String>, Integer> sumUp2 = x -> x.stream().mapToInt(y -> y.length()).sum();

    Function<Integer, Integer> mixIn= x -> 42;

    public static void main(String[] args) {
        FutureExample futureExample = new FutureExample();
        CompletableFuture<List<String>> bla = CompletableFuture.supplyAsync(futureExample.generate());
        CompletableFuture result1 = bla.thenApplyAsync(futureExample.sumUp);
        CompletableFuture result2 = bla.thenApplyAsync(futureExample.sumUp);

        try {
            result1.get();
            result2.get();
        } catch(Exception exc) {}
    }
}
