package crypt;



   import java.io.IOException;
   import java.security.MessageDigest;
   import java.security.SecureRandomSpi;
   import java.security.NoSuchAlgorithmException;
  
   /**
34    * <p>This class provides a crytpographically strong pseudo-random number
35    * generator based on the SHA-1 hash algorithm.
36    *
37    * <p>Note that if a seed is not provided, we attempt to provide sufficient
38    * seed bytes to completely randomize the internal state of the generator
39    * (20 bytes).  However, our seed generation algorithm has not been thoroughly
40    * studied or widely deployed.
41    *
42    * <p>Also note that when a random object is deserialized,
43    * <a href="#engineNextBytes(byte[])">engineNextBytes</a> invoked on the
44    * restored random object will yield the exact same (random) bytes as the
45    * original object.  If this behaviour is not desired, the restored random
46    * object should be seeded, using
47    * <a href="#engineSetSeed(byte[])">engineSetSeed</a>.
48    *
49    * @author Benjamin Renaud
50    * @author Josh Bloch
51    * @author Gadi Guy
52    */
   
   public final class SecureRandom extends SecureRandomSpi
   implements java.io.Serializable {
  
      private static final long serialVersionUID = 3581829991155417889L;
   
       /**
60        * This static object will be seeded by SeedGenerator, and used
61        * to seed future instances of SecureRandom
62        */
      private static SecureRandom seeder;
   
       private static final int DIGEST_SIZE = 20;
       private transient MessageDigest digest;
       private byte[] state;
      private byte[] remainder;
       private int remCount;
      /**
72        * This empty constructor automatically seeds the generator.  We attempt
73        * to provide sufficient seed bytes to completely randomize the internal
74        * state of the generator (20 bytes).  Note, however, that our seed
75        * generation algorithm has not been thoroughly studied or widely deployed.
76        *
77        * <p>The first time this constructor is called in a given Virtual Machine,
78        * it may take several seconds of CPU time to seed the generator, depending
79        * on the underlying hardware.  Successive calls run quickly because they
80        * rely on the same (internal) pseudo-random number generator for their
81        * seed bits.
82        */
       public SecureRandom() {
           init(null);
       }
   
       /**
88        * This constructor is used to instatiate the private seeder object
89        * with a given seed from the SeedGenerator.
90        *
91        * @param seed the seed.
92        */
      private SecureRandom(byte seed[]) {
           init(seed);
       }
  
       /**
98        * This call, used by the constructors, instantiates the SHA digest
99        * and sets the seed, if given.
100        */
       private void init(byte[] seed) {
           try {
               digest = MessageDigest.getInstance ("SHA");
          } catch (NoSuchAlgorithmException e) {
               throw new InternalError("internal error: SHA-1 not available.");
           }
   
           if (seed != null) {
             engineSetSeed(seed);
          }
       }
   
       /**
114        * Returns the given number of seed bytes, computed using the seed
115        * generation algorithm that this class uses to seed itself.  This
116        * call may be used to seed other random number generators.  While
117        * we attempt to return a "truly random" sequence of bytes, we do not
118        * know exactly how random the bytes returned by this call are.  (See
119        * the empty constructor <a href = "#SecureRandom">SecureRandom</a>
120        * for a brief description of the underlying algorithm.)
121        * The prudent user will err on the side of caution and get extra
122        * seed bytes, although it should be noted that seed generation is
123        * somewhat costly.
124        *
125        * @param numBytes the number of seed bytes to generate.
126        *
127        * @return the seed bytes.
128        */
       public byte[] engineGenerateSeed(int numBytes) {
           byte[] b = new byte[numBytes];
          SeedGenerator.generateSeed(b);
          return b;
       }
   
       /**
136        * Reseeds this random object. The given seed supplements, rather than
137        * replaces, the existing seed. Thus, repeated calls are guaranteed
138        * never to reduce randomness.
139        *
140        * @param seed the seed.
141        */
       synchronized public void engineSetSeed(byte[] seed) {
           if (state != null) {
              digest.update(state);
              for (int i = 0; i < state.length; i++)
                  state[i] = 0;
           }
           state = digest.digest(seed);
       }
   
       private static void updateState(byte[] state, byte[] output) {
           int last = 1;
           int v = 0;
          byte t = 0;
          boolean zf = false;
  
           // state(n + 1) = (state(n) + output(n) + 1) % 2^160;
           for (int i = 0; i < state.length; i++) {
               // Add two bytes
               v = (int)state[i] + (int)output[i] + last;
               // Result is lower 8 bits
               t = (byte)v;
              // Store result. Check for state collision.
               zf = zf | (state[i] != t);
              state[i] = t;
               // High 8 bits are carry. Store for next iteration.
               last = v >> 8;
           }
  
          // Make sure at least one bit changes!
           if (!zf)
              state[0]++;
       }
  
       /**
176        * Generates a user-specified number of random bytes.
177        *
178        * @param bytes the array to be filled in with random bytes.
179        */
       public synchronized void engineNextBytes(byte[] result) {
           int index = 0;
           int todo;
           byte[] output = remainder;
   
           if (state == null) {
               if (seeder == null) {
                   seeder = new SecureRandom(SeedGenerator.getSystemEntropy());
                   seeder.engineSetSeed(engineGenerateSeed(DIGEST_SIZE));
               }
   
              byte[] seed = new byte[DIGEST_SIZE];
               seeder.engineNextBytes(seed);
               state = digest.digest(seed);
           }
   
           // Use remainder from last time
           int r = remCount;
           if (r > 0) {
               // How many bytes?
              todo = (result.length - index) < (DIGEST_SIZE - r) ?
                           (result.length - index) : (DIGEST_SIZE - r);
               // Copy the bytes, zero the buffer
               for (int i = 0; i < todo; i++) {
                   result[i] = output[r];
                   output[r++] = 0;
               }
               remCount += todo;
               index += todo;
           }
   
           // If we need more bytes, make them.
           while (index < result.length) {
               // Step the state
               digest.update(state);
               output = digest.digest();
               updateState(state, output);
   
               // How many bytes?
               todo = (result.length - index) > DIGEST_SIZE ?
                   DIGEST_SIZE : result.length - index;
              // Copy the bytes, zero the buffer
              for (int i = 0; i < todo; i++) {
                  result[index++] = output[i];
                   output[i] = 0;
              }
              remCount += todo;
           }
  
           // Store remainder for next time
           remainder = output;
           remCount %= DIGEST_SIZE;
       }
   
      /*
235        * readObject is called to restore the state of the random object from
236        * a stream.  We have to create a new instance of MessageDigest, because
237        * it is not included in the stream (it is marked "transient").
238        *
239        * Note that the engineNextBytes() method invoked on the restored random
240        * object will yield the exact same (random) bytes as the original.
241        * If you do not want this behaviour, you should re-seed the restored
242        * random object, using engineSetSeed().
243        */
       private void readObject(java.io.ObjectInputStream s)
           throws IOException, ClassNotFoundException {
   
           s.defaultReadObject ();
  
           try {
               digest = MessageDigest.getInstance ("SHA");
           } catch (NoSuchAlgorithmException e) {
              throw new InternalError("internal error: SHA-1 not available.");
           }
       }
  }
