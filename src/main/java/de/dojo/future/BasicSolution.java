package de.dojo.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class BasicSolution {

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example1 = (BasicExample.Result result) ->
        CompletableFuture.supplyAsync(() -> {
            result.nonBlocking = true;
            result.async = false;
            result.error = true;
            return result;
        });

}
