# Future Dojo

## Wiederholung vom 27. Februar 2015

### Sync
* supplyAsync() - Konstruiert ein neues CompletableFuture<T> Objekt
* thenApply() - Führt eine Aktion in Form eines Function<T,R> aus
* thenAccept() - Führt eine abschließende Aktion aus

*Aufrufe erfolgen im aktuellen Thread (blockierend)*

### Async
* es gibt zu jeder Methode eine async Variante, endet mit _Async_



### Beispiele
##### Example 1
```console
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
starting example1 Thread[main,5,main]
done example1 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
Process finished with exit code 0
```
##### Example 2
* Abarbeitung erfolgt im aktuellen Thread, d.h. sequentiell

```console
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
starting example2 Thread[main,5,main]
done example2 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
  [1] SetCountAndGetAsFuture     (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [1] SetCountAndGetAsFuture -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [1] SetCountAndGetAsFuture +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [2] SetInfoAndGetAsFuture      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [2] SetInfoAndGetAsFuture  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [2] SetInfoAndGetAsFuture  +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [Consume] Result with values Result{info='magic', count=1, nonBlocking=null, async=null, error=null}    (Thread[ForkJoinPool.commonPool-worker-2,5,main]).
ready example2 Thread[main,5,main]

Process finished with exit code 0
```

##### Example 3

```console
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
starting example3 Thread[main,5,main]
done example3 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - ...
  [1] SetCountAndGetAsFuture     (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [1] SetCountAndGetAsFuture -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [1] SetCountAndGetAsFuture +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [2] SetInfoAndGetAsFuture      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [2] SetInfoAndGetAsFuture  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
  [2] SetInfoAndGetAsFuture  +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - ...
ready example3 Thread[main,5,main]

Process finished with exit code 0
```

##### Example 4
```
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
starting example4 Thread[main,5,main]
done example4 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsFuture      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [1] SetCountAndGetAsFuture     (Thread[ForkJoinPool.commonPool-worker-3,5,main])
ready example4 Thread[main,5,main]
  [1] SetCountAndGetAsFuture -sleep-    (Thread[ForkJoinPool.commonPool-worker-3,5,main]) - .
  [2] SetInfoAndGetAsFuture  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .

Process finished with exit code 0
```

##### Example 5
```
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
starting example5 Thread[main,5,main]
done example5 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsFuture      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [1] SetCountAndGetAsFuture     (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [2] SetInfoAndGetAsFuture  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [1] SetCountAndGetAsFuture -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsFuture  +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [1] SetCountAndGetAsFuture +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [Consume] Result with values Result{info='magic', count=1, nonBlocking=null, async=null, error=null}    (Thread[main,5,main]).
ready example5 Thread[main,5,main]

Process finished with exit code 0
```

##### Example 6

```
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
starting example6 Thread[main,5,main]
done example6 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsFuture      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [2] SetInfoAndGetAsFuture  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [1] SetCountAndGetAsFuture     (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [1] SetCountAndGetAsFuture -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsFuture  +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [1] SetCountAndGetAsFuture +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
lets combine together Thread[ForkJoinPool.commonPool-worker-1,5,main]
bla Result{info='magic', count=1, nonBlocking=null, async=null, error=null}
  [Consume] Result with values Result{info='magic', count=1, nonBlocking=null, async=null, error=null}    (Thread[ForkJoinPool.commonPool-worker-1,5,main]).
blub Result{info='magic', count=1, nonBlocking=null, async=null, error=null}
ready example6 Thread[main,5,main]

Process finished with exit code 0
```

##### Example 7
```
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
starting example7 Thread[main,5,main]
done example7 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsResult      (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [2] SetInfoAndGetAsResult  -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsResult  +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [1] SetCountAndGetAsResult     (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [1] SetCountAndGetAsResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [1] SetCountAndGetAsResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
lets combine together Thread[ForkJoinPool.commonPool-worker-1,5,main]
ready example7 Thread[main,5,main]
  [Consume] Result with values Result{info='magic', count=1, nonBlocking=null, async=null, error=null}    (Thread[ForkJoinPool.commonPool-worker-1,5,main]).

Process finished with exit code 0
```

##### Example 8
```
  [Supplier] creating something new cool stuff    (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [Supplier] SupplyNewResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
starting example8 Thread[main,5,main]
done example8 Thread[main,5,main]
  [Supplier] SupplyNewResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [1] SetCountAndGetAsResult     (Thread[ForkJoinPool.commonPool-worker-1,5,main])
  [2] SetInfoAndGetAsResult      (Thread[ForkJoinPool.commonPool-worker-2,5,main])
  [1] SetCountAndGetAsResult -sleep-    (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
  [2] SetInfoAndGetAsResult  -sleep-    (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [2] SetInfoAndGetAsResult  +done+     (Thread[ForkJoinPool.commonPool-worker-2,5,main]) - .
  [1] SetCountAndGetAsResult +done+     (Thread[ForkJoinPool.commonPool-worker-1,5,main]) - .
lets combine together Thread[ForkJoinPool.commonPool-worker-1,5,main]
  [Consume] Result with values Result{info='magic', count=1, nonBlocking=null, async=null, error=null}    (Thread[ForkJoinPool.commonPool-worker-1,5,main]).
ready example8 Thread[main,5,main]

Process finished with exit code 0
```

##### Example 9
