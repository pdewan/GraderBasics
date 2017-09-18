package grader.basics.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import util.trace.Tracer;
// code stolen from here: http://stackoverflow.com/questions/2920315/permutation-of-array
public class Permutations<E> implements  Iterator<E[]>{

    private E[] permutable;
    private Integer[] indices;
    private boolean has_next;

    protected E[] output;//next() returns this array, make it public

    public Permutations(E[] arr){
        this.permutable = arr.clone();
        indices = new Integer[arr.length];
        //convert an array of any elements into array of integers - first occurrence is used to enumerate
        Map<E, Integer> hm = new HashMap<E, Integer>();
        for(int i = 0; i < arr.length; i++){
            Integer n = hm.get(arr[i]);
            if (n == null){
                hm.put(arr[i], i);
                n = i;
            }
            indices[i] = n.intValue();
        }
        Arrays.sort(indices);//start with ascending sequence of integers


        //output = new E[arr.length]; <-- cannot do in Java with generics, so use reflection
        output = (E[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
        has_next = true;
    }

    public boolean hasNext() {
        return has_next;
    }

    /**
     * Computes next permutations. Same array instance is returned every time!
     * @return
     */
    public E[] next() {
        if (!has_next)
            throw new NoSuchElementException();

        for(int i = 0; i < indices.length; i++){
            output[i] = permutable[indices[i]];
        }


        //get next permutation
        has_next = false;
        for(int tail = indices.length - 1;tail > 0;tail--){
            if (indices[tail - 1] < indices[tail]){//still increasing

                //find last element which does not exceed ind[tail-1]
                int s = indices.length - 1;
                while(indices[tail-1] >= indices[s])
                    s--;

                swap(indices, tail-1, s);

                //reverse order of elements in the tail
                for(int i = tail, j = indices.length - 1; i < j; i++, j--){
                    swap(indices, i, j);
                }
                has_next = true;
                break;
            }

        }
        return output;
    }

    private void swap(Integer[] arr, int i, int j){
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public void remove() {

    }
    public Integer[] getIndices() {
    	return indices;
    }
    public static void main(String[] args) {
    	Class[] aTypes = {
    			Object.class, 
    			Integer.TYPE, 
//    			Double.class, 
    			Integer.TYPE
    			};
    	Permutations permutations = new Permutations(aTypes);
    	while (permutations.hasNext()) {
    		Tracer.info(Permutations.class, "Indices:" + Arrays.toString(permutations.getIndices()));
    		Tracer.info(Permutations.class, "permutation" + Arrays.toString(permutations.next()));
    	}
    }
}

