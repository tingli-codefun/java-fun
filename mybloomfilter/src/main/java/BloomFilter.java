import java.util.BitSet;
import java.util.function.Function;

/**
 * A simple implementation of a Bloom filter, a space-efficient probabilistic data structure.
 * @param <T> The type of elements to be inserted into the Bloom filter.
 */
public class BloomFilter<T> {
    private BitSet bitSet;  // The bit array to store the Bloom filter
    private int size;  // The size of the Bloom filter (number of bits)
    private int numHashFunctions;  // The number of hash functions used
    private Function<T, Integer>[] hashFunctions;  // Array of hash functions

    /**
     * Constructs a new Bloom filter.
     * @param size The size of the Bloom filter (number of bits).
     * @param numHashFunctions The number of hash functions to use.
     */
    @SuppressWarnings("unchecked")
    public BloomFilter(int size, int numHashFunctions) {
        this.size = size;
        this.numHashFunctions = numHashFunctions;
        this.bitSet = new BitSet(size);
        this.hashFunctions = new Function[numHashFunctions];

        // Create simple hash functions using the item's hashCode and a seed value
        for (int i = 0; i < numHashFunctions; i++) {
            final int seed = i;
            hashFunctions[i] = item -> Math.abs((item.hashCode() + seed) % size);
        }
    }

    /**
     * Adds an item to the Bloom filter.
     * @param item The item to add.
     */
    public void add(T item) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            bitSet.set(hashFunction.apply(item));  // Set the bit at the index returned by each hash function
        }
    }

    /**
     * Checks if an item might be in the Bloom filter.
     * @param item The item to check.
     * @return true if the item might be in the set, false if it definitely is not.
     */
    public boolean mightContain(T item) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            if (!bitSet.get(hashFunction.apply(item))) {
                return false;  // If any bit is not set, the item is definitely not in the set
            }
        }
        return true;  // All bits were set, so the item might be in the set
    }

    /**
     * Main method to demonstrate the usage of the Bloom filter.
     */
    public static void main(String[] args) {
        BloomFilter<String> filter = new BloomFilter<>(100000, 3);

        // Add some items to the filter
        filter.add("apple");
        filter.add("banana");
        filter.add("orange");
        filter.add("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUu");

        // Test for membership
        System.out.println(filter.mightContain("apple"));  // true
        System.out.println(filter.mightContain("banana")); // true
        System.out.println(filter.mightContain("grape"));  // false (probably)
        System.out.println(filter.mightContain("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUua")); // true
    }
}
