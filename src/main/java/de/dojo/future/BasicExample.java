package de.dojo.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicExample {

    public class Result {
        String info;
        long count;

        Boolean nonBlocking;
        Boolean async;
        Boolean error;

        @Override
        public String toString() {
            return "Result{" +
                    "info='" + info + '\'' +
                    ", count=" + count +
                    ", nonBlocking=" + nonBlocking +
                    ", async=" + async +
                    ", error=" + error +
                    '}';
        }
    }

    private CompletableFuture<Result> getSupplyNewResult(String id) {
        return
        CompletableFuture.supplyAsync(() -> {
            System.out.println("  [Supplier" + id + "] creating something new cool stuff    (" + Thread.currentThread() + ")");
            Result result = new Result();
            sometimeWeHadToSleep(5, "  [Supplier" + id + "] SupplyNewResult", result);
            return result;
        });
    }

    // Supplier
    CompletableFuture<Result> supplyNewResult = CompletableFuture.supplyAsync(() -> {
        System.out.println("  [Supplier] creating something new cool stuff    (" + Thread.currentThread() + ")");
        Result result = new Result();
        sometimeWeHadToSleep(5, "  [Supplier] SupplyNewResult", result);
        return result;
    });

    // Consumer
    Consumer<Result> consumeResult = (Result result) -> {
        System.out.println("  [Consume] Result with values " + result + "    (" + Thread.currentThread() + ").");
    };


    // Function
    Function<Result, CompletableFuture<Result>> setCountAndGetAsFuture = (Result result) ->
            CompletableFuture.supplyAsync(() -> {
                System.out.println("  [1] SetCountAndGetAsFuture     (" + Thread.currentThread()+")");
                result.count++;
                sometimeWeHadToSleep(350, "  [1] SetCountAndGetAsFuture", result);
                return result;
            });
    Function<Result, CompletableFuture<Result>> setCountAndGetAsTest = (Result result) -> {
        CompletableFuture<Result> bla = new CompletableFuture<>();
        System.out.println("  [1] SetCountAndGetAsFuture     (" + Thread.currentThread()+")");
        result.count++;
        sometimeWeHadToSleep(350, "  [1] SetCountAndGetAsFuture", result);
        bla.complete(result);
        return bla;
    };


    Function<Result, Result> setCountAndGetAsResult = (Result result) -> {
        System.out.println("  [1] SetCountAndGetAsResult     (" + Thread.currentThread()+ ")");
        result.count++;
        sometimeWeHadToSleep(350, "  [1] SetCountAndGetAsResult", result);
        return result;
    };


    Function<Result, CompletableFuture<Result>> setInfoAndGetAsFuture = (Result result) ->
            CompletableFuture.supplyAsync(() -> {
                System.out.println("  [2] SetInfoAndGetAsFuture      (" + Thread.currentThread()+ ")");
                result.info= "magic";
                sometimeWeHadToSleep(200, "  [2] SetInfoAndGetAsFuture ", result);
                return result;
            });

    Function<Result, Result> setInfoAndGetAsResult = (Result result) -> {
                System.out.println("  [2] SetInfoAndGetAsResult      (" + Thread.currentThread() + ")");
                result.info= "magic";
                sometimeWeHadToSleep(200, "  [2] SetInfoAndGetAsResult ", result);
                return result;
    };


    /**
     * main Thread ist Non Blocking
     * -> wenn Ergebnis nicht direkt benutzt wird, sondern lediglich von einem Consumer
     *    verarbeitet, dann kann das schief gehen, da der main Thread eher beendet ist
     */
    public void example1() {
        System.out.println("starting example1 " + Thread.currentThread());
        CompletableFuture<Void> result = supplyNewResult
                .thenCompose(setCountAndGetAsFuture)
                .thenCompose(setInfoAndGetAsFuture)
                .thenAccept(consumeResult);
        System.out.println("done example1 " + Thread.currentThread());
    }

    /**
     * main Thread non blocking
     * -> mittels get und join kann die Verarbeitung blockierend abgewartet werden
     * -> get/join liefern das Ergebnis
     * -> Ergebnis ist in diesem Fall Void
     */
    public void example2() throws Exception {
        System.out.println("starting example2 " + Thread.currentThread());
        CompletableFuture<Void> result = supplyNewResult
                .thenCompose(setCountAndGetAsFuture)
                .thenCompose(setInfoAndGetAsFuture)
                .thenAccept(consumeResult);
        System.out.println("done example2 " + Thread.currentThread());
        result.get();
        System.out.println("ready example2 " + Thread.currentThread());
    }

    /**
     * ???
     */
    public void example3() throws Exception {
        System.out.println("starting example3 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult
                .thenComposeAsync(setCountAndGetAsFuture)
                .thenComposeAsync(setInfoAndGetAsFuture);
        System.out.println("done example3 " + Thread.currentThread());
        result.join();
        System.out.println("ready example3 " + Thread.currentThread());
    }

    // what's happend here?
    public void example4() throws Exception {
        System.out.println("starting example4 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult;

        CompletableFuture<Result> bla = result.thenCompose(setCountAndGetAsFuture);
        CompletableFuture<Result> blub = result.thenCompose(setInfoAndGetAsFuture);

        System.out.println("done example4 " + Thread.currentThread());
        result.get();
        System.out.println("ready example4 " + Thread.currentThread());
    }

    // what's happend here?
    public void example5() throws Exception {
        System.out.println("starting example5 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult;

        CompletableFuture<Result> result1 = result.thenCompose(setCountAndGetAsFuture);
        CompletableFuture<Result> result2 = result.thenCompose(setInfoAndGetAsFuture);

        System.out.println("done example5 " + Thread.currentThread());
        result.get();
        result1.get();
        result2.get();
        result.thenAccept(consumeResult);

        System.out.println("ready example5 " + Thread.currentThread());
    }

    /**
     * parallel ausführen und zum Schluss Ergebnis zusammen führen
     * => Function liefert Completeable Future, daher findet die Verarbeitung parallel statt
     */
    public void example6() throws Exception {
        System.out.println("starting example6 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult;

        CompletableFuture<Result> result1 = result.thenCompose(setCountAndGetAsFuture);
        CompletableFuture<Result> result2 = result.thenCompose(setInfoAndGetAsFuture);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(consumeResult);

        System.out.println("done example6 " + Thread.currentThread());
        System.out.println("bla " + result.get());
        result.thenAccept((Result blafo) -> System.out.println("blub " + blafo));
        System.out.println("ready example6 " + Thread.currentThread());
    }

    /**
     * Synchrone Verarbeitung
     * => Es handelt sich hierbei um eine Function, diese selbst ist synchron und
     *    liefert kein ComputeableFuture zurück
     * => Die Verarbeitung findet innerhalb eines Thread statt
     *
     * => die Verarbeitung im main thread ist selbst wieder non blocking
     */
    public void example7() throws Exception {
        System.out.println("starting example7 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult;

        CompletableFuture<Result> result1 = result.thenApply(setCountAndGetAsResult);
        CompletableFuture<Result> result2 = result.thenApply(setInfoAndGetAsResult);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(consumeResult);

        System.out.println("done example7 " + Thread.currentThread());
        result.get();
        System.out.println("ready example7 " + Thread.currentThread());
    }

    /**
     * Asynchrone Verarbeitung
     * => mittels thenApplyAsync wird die Verabeitung non blocking durchgeführt
     */
    public void example8() throws Exception {
        System.out.println("starting example8 " + Thread.currentThread());
        CompletableFuture<Result> result = supplyNewResult;

        CompletableFuture<Result> result1 = result.thenApplyAsync(setCountAndGetAsResult);
        CompletableFuture<Result> result2 = result.thenApplyAsync(setInfoAndGetAsResult);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(consumeResult);

        System.out.println("done example8 " + Thread.currentThread());
        result.get();
        System.out.println("ready example8 " + Thread.currentThread());
    }

    /**
     * acceptEither
     */
    public void example9() throws Exception {
        System.out.println("starting example9 " + Thread.currentThread());
        CompletableFuture<Result> result1 = supplyNewResult
                .thenComposeAsync(setCountAndGetAsFuture)
                .thenComposeAsync(setInfoAndGetAsFuture);
        System.out.println("done example9 " + Thread.currentThread());
        System.out.println("ready example9 " + Thread.currentThread());

        CompletableFuture<Result> result2 = supplyNewResult
                .thenComposeAsync(setCountAndGetAsFuture)
                .thenComposeAsync(setInfoAndGetAsFuture)
                .thenComposeAsync(setCountAndGetAsFuture);
        System.out.println("done example9 " + Thread.currentThread());
        System.out.println("ready example9 " + Thread.currentThread());

        CompletableFuture<Void> end = result1.acceptEither(result2, consumeResult);
        end.get();
        result2.get();
    }


    /**
     * allOf / anyOf
     */
    public void example10() throws Exception {
        System.out.println("starting example10 " + Thread.currentThread());
        CompletableFuture<Void> result1 = getSupplyNewResult("1")
                .thenCompose(setCountAndGetAsFuture)
                .thenAccept(consumeResult);
        CompletableFuture<Void> result2 = getSupplyNewResult("2")
                .thenCompose(setInfoAndGetAsFuture)
                .thenCompose(setCountAndGetAsFuture)
                .thenAccept(consumeResult);

//        CompletableFuture<Void> bla = CompletableFuture.allOf(result1, result2);
//        bla.join();

        CompletableFuture<Object> bla = CompletableFuture.anyOf(result1, result2);
        bla.join();
        // das ganze auch als anyOf
    }

    public void example11() throws Exception {
        System.out.println("starting example11 " + Thread.currentThread());
        CompletableFuture<Result> result1 = supplyNewResult
                .thenCompose(setCountAndGetAsFuture)
                .thenApply((r) -> {
                    if(r.count == 1)
                        throw new RuntimeException();
                    else return r;
                })
                .exceptionally(e -> {
                    System.out.println("Exception --> " + e);
                    return new Result();
                });

        result1.join();
    }

    public void example12() throws Exception {
        System.out.println("starting example12 " + Thread.currentThread());
        CompletableFuture<Result> result1 = supplyNewResult
                .thenCompose(setCountAndGetAsFuture)
                .thenCompose(setCountAndGetAsFuture)
                .thenApply((r) -> {
                    if (r.count == 1)
                        throw new RuntimeException();
                    else return r;
                })
                .handle((ok, e) -> {
                    if (ok != null) {
                        System.out.println("everything is fine");
                        return ok;
                    } else {
                        System.out.println("Exception --> " + e);
                        return new Result();
                    }
                });

        System.out.println("bla " + result1.get());
    }
    /**CompletableFuture<Integer> safe = future.handle((ok, ex) -> {
     if (ok != null) {
     return Integer.parseInt(ok);
     } else {
     log.warn("Problem", ex);
     return -1;
     }
     });
     */


    public static final void main(String[] args) throws Exception {
        BasicExample basicExample = new BasicExample();
        basicExample.example12();
    }


    public static final void sometimeWeHadToSleep(long time, String message, Result result) {
        try {
            System.out.println(message + " -sleep-    (" + Thread.currentThread()+ ") - " + result);
            Thread.sleep(time);
            System.out.println(message + " +done+     (" + Thread.currentThread()+ ") - " + result);
        }
        catch(Exception exc) {exc.printStackTrace();}
    }
}
