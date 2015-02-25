package de.dojo.future;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicExampleTest {

    @Test
    public void example1() {
        BasicExample basicExample = new BasicExample();
        basicExample.example1();

        BasicExample.Result result = basicExample.supplyNewResult
                .thenCompose(BasicSolution.example1)
                .join();

        assertEquals(result.nonBlocking, true);
        assertEquals(result.async, false);
        assertEquals(result.error, true);
    }

    @Test
    public void example2() throws Exception {
        BasicExample basicExample = new BasicExample();
        basicExample.example2();
    }

    @Test
    public void example3() throws Exception {
        BasicExample basicExample = new BasicExample();
        basicExample.example3();
    }
}
