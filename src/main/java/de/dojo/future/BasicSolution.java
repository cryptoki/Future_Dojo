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

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example2 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = false;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example3 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example4 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example5 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example6 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example7 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });

    public static Function<BasicExample.Result, CompletableFuture<BasicExample.Result>> example8 = (BasicExample.Result result) ->
            CompletableFuture.supplyAsync(() -> {
                result.nonBlocking = true;
                result.async = false;
                result.error = true;
                return result;
            });
}
