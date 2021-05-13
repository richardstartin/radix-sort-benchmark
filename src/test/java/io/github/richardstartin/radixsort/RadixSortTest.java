package io.github.richardstartin.radixsort;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Execution(ExecutionMode.CONCURRENT)
class RadixSortTest {

  enum Impl {

    BASIC {
      @Override
      void sort(int[] data) {
        RadixSort.basic(data);
      }
    },
    BASIC_BUFFERED {
      @Override
      void sort(int[] data) {
        RadixSort.basic(data, new int[data.length]);
      }
    },
    UNROLL {
      @Override
      void sort(int[] data) {
        RadixSort.unroll(data);
      }
    },
    UNROLL_BUFFER {
      @Override
      void sort(int[] data) {
        RadixSort.unroll(data, new int[data.length]);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistograms(data);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS_SKIP_LEVELS {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistogramsSkipLevels(data);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS_SKIP_LEVELS_SIGNED {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistogramsSkipLevelsSigned(data);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS_SKIP_LEVELS_BUFFER {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistogramsSkipLevels(data, new int[data.length]);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS_SKIP_LEVELS_DETECTION {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistogramsSkipLevelsWithDetection(data);
      }
    },
    UNROLL_ONE_PASS_HISTOGRAMS_SKIP_LEVELS_DETECTION_BUFFER {
      @Override
      void sort(int[] data) {
        RadixSort.unrollOnePassHistogramsSkipLevelsWithDetection(data, new int[data.length]);
      }
    },
    JDK {
      @Override
      void sort(int[] data) {
        RadixSort.jdk(data);
      }
    };

    abstract void sort(int[] data);
  }

  @ParameterizedTest
  @EnumSource(Impl.class)
  public void testRandom(Impl impl) {
    int[] data = new int[10000];
    int[] copy = new int[10000];
    for (int i = 0; i < 10000; ++i) {
      fillRandom(data);
      System.arraycopy(data, 0, copy, 0, copy.length);
      impl.sort(data);
      Arrays.sort(copy);
      assertArrayEquals(data, copy);
    }
  }

  @ParameterizedTest
  @EnumSource(Impl.class)
  public void testRandomRange(Impl impl) {
    int[] data = new int[10000];
    int[] copy = new int[10000];
    int[] ranges = {0, 1 << 8,
            0, 1 << 16,
            0, 1 << 24,
            0, 1 << 30,
            1 << 8, 1 << 16,
            1 << 8, 1 << 24,
            1 << 8, 1 << 30,
            1 << 16, 1 << 24,
            1 << 16, 1 << 30,
            1 << 24, 1 << 30,};
    for (int i = 0; i < 10000; ++i) {
      for (int j = 0; j < ranges.length; j += 2)
        fillRandomInRange(data, ranges[j], ranges[j + 1]);
      System.arraycopy(data, 0, copy, 0, copy.length);
      impl.sort(data);
      Arrays.sort(copy);
      assertArrayEquals(data, copy);
    }
  }

  @ParameterizedTest
  @EnumSource(Impl.class)
  public void testSequential(Impl impl) {
    int[] data = new int[10000];
    int[] copy = new int[10000];
    fillSequential(data);
    System.arraycopy(data, 0, copy, 0, copy.length);
    impl.sort(data);
    assertArrayEquals(data, copy);
  }

  @ParameterizedTest
  @EnumSource(Impl.class)
  public void testSequentialReverse(Impl impl) {
    int[] data = new int[10000];
    int[] copy = new int[10000];
    fillSequentialReverse(data);
    System.arraycopy(data, 0, copy, 0, copy.length);
    impl.sort(data);
    Arrays.sort(copy);
    assertArrayEquals(data, copy);
  }

  @ParameterizedTest
  @EnumSource(Impl.class)
  public void testConstant(Impl impl) {
    int[] data = new int[10000];
    int[] copy = new int[10000];
    Arrays.fill(data, 0xF0F0F0F0);
    System.arraycopy(data, 0, copy, 0, copy.length);
    impl.sort(data);
    assertArrayEquals(data, copy);
  }


  private static void fillRandom(int[] data) {
    for (int i = 0; i < data.length; ++i) {
      data[i] = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }
  }

  private static void fillRandomInRange(int[] data, int min, int max) {
    for (int i = 0; i < data.length; ++i) {
      data[i] = ThreadLocalRandom.current().nextInt(min, max);
    }
  }

  private static void fillSequential(int[] data) {
    int min = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE - data.length);
    for (int i = 0; i < data.length; ++i) {
      data[i] = min + i;
    }
  }

  private static void fillSequentialReverse(int[] data) {
    int max = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE - data.length);
    for (int i = 0; i < data.length; ++i) {
      data[i] = max - i;
    }
  }


}