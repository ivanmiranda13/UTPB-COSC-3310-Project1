/**
 * @auth
 */
import java.util.Arrays;
/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * @author Tim Fielder
 * @version 1.0 (Sept 30, 2024)
 */
public class UInt {
    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;
    // The number of bits used to represent the unsigned integer.
    protected int length;
    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }
    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
         bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
          // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;
            // Deprecated analog method
            /*int p = 0;
            while (Math.pow(2, p) < i) {
                p++;
            }
            p--;
            bits[p] = true;
            i -= Math.pow(2, p);*/
        }
    }
    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }
    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }
    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }
    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }
    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }
    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }
        // In the specific case that this.length is greater, there are additional elements of
        //   this.bits that are not getting ANDed against anything.
        // Depending on the implementation, we may want to treat the operation as implicitly padding
        //   the u.bits array to match the length of this.bits, in which case what we actually
        //   perform is simply setting the remaining indices of this.bits to false.
        // Note that while this logic is helpful for the AND operation if we want to use this
        //   implementation (implicit padding), it is never necessary for the OR and XOR operations.
        if (this.length > u.length) {
            for (int i = u.length; i < this.length; i++) {
                this.bits[this.length - i - 1] = false;
            }
        }
    }


    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }
    
    public int toSignedInt() {
        if (this.length == 0) {
            return 0;
        }

        boolean isNegative = this.bits[0]; // Check if the most significant bit is 1 (indicating negative in 2's complement)
        int value = 0;

        // If the number is negative, take its 2's complement for conversion
        if (isNegative) {
            UInt temp = this.clone();
            temp.negate(); // Get the positive version by negating
            value = -temp.toInt();
        } else {
            value = this.toInt(); // Directly use the toInt() method for non-negative numbers
        }

        return value;
    }
    
    public void or(UInt u) {
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] |
                            u.bits[u.length - i - 1];
        }

    }

    public static UInt or(UInt a, UInt b) {
      UInt temp = a.clone();
        temp.or(b);
        return temp;
    }
     public void xor(UInt u) {
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] ^
                            u.bits[u.length - i - 1];
        }
     }

    public static UInt xor(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.xor(b);
        return temp;
        
    }

    public void add(UInt u) 
      int maxLength = Math.max(this.length, u.length);
        boolean[] result = new boolean[maxLength + 1]; // Extra bit added in for carry

        boolean carry = false;
        for (int i = 0; i < maxLength; i++) {
            boolean bitA = i < this.length && this.bits[this.length - 1 - i];
            boolean bitB = i < u.length && u.bits[u.length - 1 - i];
            
            boolean sum = bitA ^ bitB ^ carry;
            carry = (bitA & bitB) || (bitA && carry) || (bitB && carry);

            result[maxLength - i] = sum; // Storing the result in reverse.
        }
        
        if (carry) {
            result[0] = true;
        }
        
        if (result[0]) {
            this.length = maxLength + 1;
            this.bits = result;
        } else {
            this.length = maxLength;
            this.bits = Arrays.copyOfRange(result, 1, result.length);
        }
    }
    
    public static UInt add(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.add(b);
        return temp;
    }

    public void negate() {
      for (int i = 0; i < length; i++) {
            bits[i] = !bits[i];
        }
        UInt one = new UInt(1);


        this.add(one);
        
        public void sub(UInt u) {
        // Ensuring that the length is the same size, and resizing it as needed.
        int maxLength = Math.max(this.length, u.length);
        boolean[] result = new boolean[maxLength];
        boolean borrow = false;

        //Starting from the least significant bit, and moving to the most significant bit.
        for (int i = 0; i < maxLength; i++) {
            boolean bitA = i < this.length && this.bits[this.length - 1 - i];
            boolean bitB = i < u.length && u.bits[u.length - 1 - i];

            //result calculated, borrow status updated.
            result[maxLength - 1 - i] = bitA ^ bitB ^ borrow;
            borrow = (!bitA && (bitB || borrow)) || (bitA && bitB && borrow);

        }

        if (borrow) {  //Checks if the result is negative; set to 0.
            this.length = 1;
            this.bits = new boolean[]{false};
        }
        //Otherwise, update bits and trim down leading zeros.
        else {
            int firstTrue = 0;
            while (firstTrue < result.length && !result[firstTrue]) firstTrue++;
            this.length = result.length - firstTrue;
            this.bits = Arrays.copyOfRange(result, firstTrue, result.length);
        }
        
        }


    public static UInt sub(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.sub(b);
        return temp;
        
    }

    public void mul(UInt u) {
      
       int signedThis = this.toSignedInt();
       
        int signedU = u.toSignedInt();

        int product = signedThis * signedU;

        if (product < 0) {
            UInt result = new UInt(-product); // Create a positive UInt and then negate it
            result.negate(); // Negate to get the 2's complement representation
            this.bits = result.bits;
            this.length = result.length;
        } else {
            UInt result = new UInt(product);
            this.bits = result.bits;
            this.length = result.length;
        }
    }
    public static UInt mul(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.mul(b);
        return temp;
    }
}

