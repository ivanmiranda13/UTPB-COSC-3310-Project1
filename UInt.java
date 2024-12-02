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

    public void or(UInt u) {
    int maxLength = Math.max(this.length, u.length);
    
    // Resize the bits array if necessary
    if (this.length < maxLength) {
        this.bits = Arrays.copyOf(this.bits, maxLength);
        this.length = maxLength;
    }
    
    // Perform OR operation
    for (int i = 0; i < maxLength; i++) {
        boolean thisBit = i < this.length ? this.bits[this.length - i - 1] : false;
        boolean otherBit = i < u.length ? u.bits[u.length - i - 1] : false;
        this.bits[this.length - i - 1] = thisBit || otherBit;
    }
}

        return;
    }

    public static UInt or(UInt a, UInt b) {
    UInt result = a.clone();
    result.or(b);
    return result;
}

        return null;
    }

    public void xor(UInt u) {
    int maxLength = Math.max(this.length, u.length);
    
    // Resize the bits array if necessary
    if (this.length < maxLength) {
        this.bits = Arrays.copyOf(this.bits, maxLength);
        this.length = maxLength;
    }
    
    // Perform XOR operation
    for (int i = 0; i < maxLength; i++) {
        boolean thisBit = i < this.length ? this.bits[this.length - i - 1] : false;
        boolean otherBit = i < u.length ? u.bits[u.length - i - 1] : false;
        this.bits[this.length - i - 1] = thisBit != otherBit; // XOR: true if bits are different
    }
}

        return;
    }

    public static UInt xor(UInt a, UInt b) {
    UInt result = a.clone();
    result.xor(b);
    return result;
}

        return null;
    }

    public void add(UInt u) {
    int maxLength = Math.max(this.length, u.length);
    
    // Resize the bits array if necessary
    if (this.length < maxLength) {
        this.bits = Arrays.copyOf(this.bits, maxLength);
        this.length = maxLength;
    }
    
    boolean carry = false;
    
    for (int i = 0; i < maxLength; i++) {
        boolean thisBit = i < this.length ? this.bits[this.length - i - 1] : false;
        boolean otherBit = i < u.length ? u.bits[u.length - i - 1] : false;
        
        // XOR for the sum, AND for the carry
        boolean sum = thisBit != otherBit != carry;
        carry = (thisBit && otherBit) || (carry && (thisBit || otherBit));
        
        // Set the current bit to the sum
        this.bits[this.length - i - 1] = sum;
    }
    
    // If there's still a carry after the last bit, extend the bits array
    if (carry) {
        this.bits = Arrays.copyOf(this.bits, maxLength + 1);
        this.bits[0] = true;
        this.length++;
    }
}

        return;
    }

    public static UInt add(UInt a, UInt b) {
    UInt result = a.clone();
    result.add(b);
    return result;
}

        return null;
    }

    public void negate() {
    // Invert the bits
    for (int i = 0; i < this.length; i++) {
        this.bits[i] = !this.bits[i];
    }
    
    // Add 1 to the inverted bits
    add(new UInt(1)); // 1 is represented as "0b1"
}

    }

    public void sub(UInt u) {
    UInt uNeg = u.clone();
    uNeg.negate();
    add(uNeg);
    
    // If the result is negative, we coerce it to zero.
    if (toInt() < 0) {
        Arrays.fill(this.bits, false); // Set all bits to 0
    }
}

        return;
    }

    public static UInt sub(UInt a, UInt b) {
    UInt result = a.clone();
    result.sub(b);
    return result;
}

        return null;
    }

    public void mul(UInt u) {
    int maxLength = this.length + u.length; // Maximum possible bit-length after multiplication
    
    // Extend this.bits and u.bits to accommodate the result.
    this.bits = Arrays.copyOf(this.bits, maxLength);
    boolean[] result = new boolean[maxLength]; // Result array to store the product.
    
    // Copy of this.bits and u.bits for multiplication.
    boolean[] multiplicand = Arrays.copyOf(this.bits, this.length); // The first number (this)
    boolean[] multiplier = Arrays.copyOf(u.bits, u.length); // The second number (u)
    
    // Extend the multiplier by adding a 0 at the least significant bit (to simulate "Booth's" 2-bit window)
    boolean[] extendedMultiplier = new boolean[maxLength + 1]; 
    System.arraycopy(multiplier, 0, extendedMultiplier, 0, multiplier.length);
    
    boolean[] extendedMultiplicand = new boolean[maxLength];
    System.arraycopy(multiplicand, 0, extendedMultiplicand, 0, multiplicand.length);
    
    boolean carry = false; // Initial carry for addition/subtraction.
    
    for (int step = 0; step < u.length; step++) {
        // Check the last two bits in the extended multiplier
        boolean lastBit = extendedMultiplier[maxLength]; // Last bit in multiplier
        boolean secondLastBit = extendedMultiplier[maxLength - 1]; // Second last bit in multiplier
        
        if (lastBit == false && secondLastBit == true) {
            // Case 10: Perform subtraction of multiplicand
            result = subtract(result, extendedMultiplicand); // Subtract multiplicand from result
        } else if (lastBit == true && secondLastBit == false) {
            // Case 01: Perform addition of multiplicand
            result = add(result, extendedMultiplicand); // Add multiplicand to result
        }
        
        // Right shift the result and multiplier (this is the "Booth's Algorithm step")
        shiftRight(result, extendedMultiplier);
    }
    
    // The final result
    this.bits = result;
    this.length = maxLength;
}

// Helper method: Add two boolean arrays (representing binary numbers)
private boolean[] add(boolean[] a, boolean[] b) {
    int len = a.length;
    boolean[] sum = new boolean[len];
    boolean carry = false;
    
    for (int i = len - 1; i >= 0; i--) {
        boolean bitA = a[i];
        boolean bitB = b[i];
        
        // Perform bitwise addition with carry
        sum[i] = bitA ^ bitB ^ carry;
        carry = (bitA && bitB) || (carry && (bitA || bitB));
    }
    return sum;
}

// Helper method: Subtract b from a (binary subtraction)
private boolean[] subtract(boolean[] a, boolean[] b) {
    // To subtract, we negate b (2's complement) and add it to a.
    boolean[] negB = new boolean[b.length];
    
    for (int i = 0; i < b.length; i++) {
        negB[i] = !b[i]; // Invert the bits
    }
    
    negB = add(negB, new boolean[b.length]); // Add 1 to complete 2's complement
    return add(a, negB); // Add the result of 2's complement to a
}

// Helper method: Perform right shift on both the result and the extended multiplier
private void shiftRight(boolean[] result, boolean[] multiplier) {
    // Shift result and multiplier to the right by one bit
    for (int i = result.length - 1; i > 0; i--) {
        result[i] = result[i - 1];
    }
    result[0] = false; // This bit is filled with zero during shift.
    
    // Shift the multiplier in the same way.
    for (int i = multiplier.length - 1; i > 0; i--) {
        multiplier[i] = multiplier[i - 1];
    }
    multiplier[0] = false; // Fill the left-most bit with zero.
}

}

        return;
    }

    public static UInt mul(UInt a, UInt b) {
    UInt result = a.clone();
    result.mul(b);
    return result;
}

        return null;
    }
}
