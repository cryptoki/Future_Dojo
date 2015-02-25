package de.dojo.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicExample {
    class Result {
        String info;
        long count;

        @Override
        public String toString() {
            return "Result{" +
                    "info='" + info + '\'' +
                    ", count=" + count +
                    '}';
        }
    }

    final CompletableFuture<Result> sampleSupplier = CompletableFuture.supplyAsync(() -> {
        System.out.println("creating something new cool stuff");
        Result result = new Result();
        sometimeWeHadToSleep(5, "Supplier");
        return result;
    });

    Function<Result, CompletableFuture<Result>> func1 = (Result result) ->
            CompletableFuture.supplyAsync(() -> {
                System.out.println("func1 " + Thread.currentThread());
                result.count++;
                sometimeWeHadToSleep(250, "func1");
                return result;
            });
    Function<Result, Result> func1a = (Result result) -> {
        System.out.println("func1a " + Thread.currentThread());
        result.count++;
        sometimeWeHadToSleep(250, "func1a");
        return result;
    };

    Function<Result, CompletableFuture<Result>> func2 = (Result result) ->
            CompletableFuture.supplyAsync(() -> {
                System.out.println("func2 " + Thread.currentThread());
                result.info= "magic";
                sometimeWeHadToSleep(100, "func2");
                return result;
            });

    Function<Result, Result> func2a = (Result result) -> {
                System.out.println("func2a " + Thread.currentThread());
                result.info= "magic";
                sometimeWeHadToSleep(100, "func2a");
                return result;
    };

    Consumer<Result> sampleConsumer = (Result result) -> {
       System.out.println("consuming result with values " + result);
    };


    // non blocking main thread
    // -> wenn Ergebnis nicht direkt benutzt wird, sondern lediglich von einem Consumer
    //    verarbeitet, dann kann das schief gehen, da der main Thread eher beendet ist
    public void example1() {
        System.out.println("starting example1 " + Thread.currentThread());
        CompletableFuture<Void> result = sampleSupplier
                .thenCompose(func1)
                .thenCompose(func2)
                .thenAccept(sampleConsumer);
        System.out.println("done example1 " + Thread.currentThread());
    }

    // non blocking main thread
    // -> auch wenn der consumer das Ergebnis "konsumiert", kann über die get Methode
    //    die Verarbeitung abgewartet werden
    public void example2() throws Exception {
        System.out.println("starting example2 " + Thread.currentThread());
        CompletableFuture<Void> result = sampleSupplier
                .thenCompose(func1)
                .thenCompose(func2)
                .thenAccept(sampleConsumer);
        System.out.println("done example2 " + Thread.currentThread());
        result.get();
        System.out.println("ready example2 " + Thread.currentThread());
    }

    // why is it parallel?
    public void example3() throws Exception {
        System.out.println("starting example3 " + Thread.currentThread());
        CompletableFuture<Result> result = sampleSupplier
                .thenComposeAsync(func1)
                .thenComposeAsync(func2);
        System.out.println("done example3 " + Thread.currentThread());
        result.get();
        System.out.println("ready example3 " + Thread.currentThread());
    }

    // what's happend here?
    public void example4() throws Exception {
        System.out.println("starting example4 " + Thread.currentThread());
        CompletableFuture<Result> result = sampleSupplier;

        result.thenCompose(func1);
        result.thenCompose(func2);

        System.out.println("done example4 " + Thread.currentThread());
        result.get();
        System.out.println("ready example4 " + Thread.currentThread());
    }

    // what's happend here?
    public void example5() throws Exception {
        System.out.println("starting example5 " + Thread.currentThread());
        CompletableFuture<Result> result = sampleSupplier;

        CompletableFuture<Result> result1 = result.thenCompose(func1);
        CompletableFuture<Result> result2 = result.thenCompose(func2);

        // ups ;-)
        result.thenAccept(sampleConsumer);

        System.out.println("done example5 " + Thread.currentThread());
        result.get();
        result1.get();
        result2.get();
        System.out.println("ready example5 " + Thread.currentThread());
    }

    /**
     * parallel ausführen und zum Schluss Ergebnis zusammen führen
     * => Function liefert Completeable Future, daher findet die Verarbeitung parallel statt
     */
    public void example6() throws Exception {
        System.out.println("starting example6 " + Thread.currentThread());
        CompletableFuture<Result> result = sampleSupplier;

        CompletableFuture<Result> result1 = result.thenCompose(func1);
        CompletableFuture<Result> result2 = result.thenCompose(func2);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(sampleConsumer);

        System.out.println("done example6 " + Thread.currentThread());
        result.get();
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
        CompletableFuture<Result> result = sampleSupplier;

        CompletableFuture<Result> result1 = result.thenApply(func1a);
        CompletableFuture<Result> result2 = result.thenApply(func2a);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(sampleConsumer);

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
        CompletableFuture<Result> result = sampleSupplier;

        CompletableFuture<Result> result1 = result.thenApplyAsync(func1a);
        CompletableFuture<Result> result2 = result.thenApplyAsync(func2a);

        result = result1.thenCombine(result2, (Result a, Result b) -> {
            System.out.println("lets combine together " + Thread.currentThread());
            a.info = b.info;
            return a;
        });

        result.thenAccept(sampleConsumer);

        System.out.println("done example8 " + Thread.currentThread());
        result.get();
        System.out.println("ready example8 " + Thread.currentThread());
    }

    public static final void main(String[] args) throws Exception {
        BasicExample basicExample = new BasicExample();
        basicExample.example7();
    }


    public static final void sometimeWeHadToSleep(long time, String message) {
        try {
            System.out.println(message + " sleep " + Thread.currentThread());
            Thread.sleep(time);
            System.out.println(message + " done " + Thread.currentThread());
        }
        catch(Exception exc) {exc.printStackTrace();}
    }
}
