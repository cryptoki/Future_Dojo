package de.dojo.future;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by hoppel on 17.04.15.
 */
public class FileAnalysis {

    Function<String, Integer> countWords = (s) -> {
        sometimeWeHadToSleep(5);
        return (s != null ? s.split(" ").length : 0);
    };

    public List<String> readFileInMemory(String path) throws Exception {
        return Files.readAllLines(Paths.get(path));
    }

    public int bla(List<String> lines) {
        return
                lines.parallelStream()
                        .map(x -> CompletableFuture.supplyAsync(() -> countWords.apply(x)))
                        .map(y -> y.join())
                        .mapToInt(Integer::valueOf)
                        .sum();
    }

    class Result {
        int i;
    }

    public int bla3(List<String> lines) {

        Collection<CompletableFuture<Integer>> i = new ArrayList<>();
        for(String line : lines) {
            i.add(CompletableFuture.supplyAsync(() -> countWords.apply(line)));
        }

        int j = 0;
        for(CompletableFuture<Integer> wordFuture : i) {
            j += wordFuture.join();
        }
        return j;
    }

    public int bla2(List<String> lines) {
        int i = 0;
        for(String line : lines) {
            i += countWords.apply(line);
        }
        return i;
    }

    public static final void main(String[] args) throws Exception {
        FileAnalysis fileAnalysis = new FileAnalysis();
        List<String> lines = fileAnalysis.readFileInMemory(args[0]);
        System.out.println("lines " + lines.size());

        long time = System.currentTimeMillis();
        int words = fileAnalysis.bla3(lines);
        System.out.println(" words " + words + "  " + (System.currentTimeMillis() - time) );
    }



    public static final void sometimeWeHadToSleep(long time) {
        try {
            Thread.sleep(time);
        }
        catch(Exception exc) {exc.printStackTrace();}
    }
}
