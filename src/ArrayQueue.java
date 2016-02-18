/**
 * The ArrayQueue class implements the QueueADT. Items put into the queue
 * can be retrieved in the order they are put in. Public methods include
 * enqueue, dequeue, isEmpty, size, and incrementIndex.
 * 
 * @author Joshua Kellerman, CS 367
 */
import java.util.*;

public class ArrayQueue<E> implements QueueADT<E> {
	
    private static final int INITSIZE = 52;  // initial array size
    private E[] items; // the items in the queue
    private int numItems;   // the number of items in the queue
    private int frontIndex;
    private int backIndex;

    
    public ArrayQueue() { //constructor
    	items = (E[]) new Object[INITSIZE];
    	numItems = 0;
    	frontIndex = 0;
    	backIndex = - 1;
    } //end constructor
    
	/**
	 * Enqueues the given item into the queue (i.e., adds it at the back of 
	 * the queue).
	 * @param item The item to be added
	 */
	public void enqueue(E item){
		if (item == null){ //check to see if item is null
			throw new NullPointerException();
		}
		if (items.length == numItems) { //if it is full...
		      E[] itemsDouble = (E[]) new Object[items.length*2]; //resize
		      System.arraycopy(items, frontIndex, itemsDouble, frontIndex,
		                 items.length-frontIndex);
	        if (frontIndex != 0) { //if the front index is not 0, move it to start
	        					   //at 0
	            System.arraycopy(items, 0, itemsDouble, items.length, frontIndex);
	        }
	        items = itemsDouble; //refresh items
		    backIndex = frontIndex + numItems - 1; //reset backIndex to -1
		    									   //or otherwise directly before
		    									   //the frontIndex if queue has
		    									   //just been resized
		    }
		backIndex = incrementIndex(backIndex); //increment queue backIndex
		items[backIndex] = item; //add item to queue
		numItems ++; //increment numItems
		return;
	} //end enqueue()
	
	/**
	 * Wraps around an index to create a circular array
	 * @param item The item to be added
	 * @return int the index, wrapped around
	 */
	private int incrementIndex(int index) {
	    if (index == items.length-1) 
	        return 0; //wrap around to back of queue
	    else 
	        return index + 1;
	} //end incrementIndex()
	
	/**
	 * Dequeues the first item in the queue (i.e., removes the item from the 
	 * front of the queue and returns it). If the queue is empty, returns null.
	 * @return the item that was dequeued, or null if the queue is empty.
	 */
	public E dequeue() throws NoSuchElementException{
		if (numItems == 0){
			throw new NoSuchElementException(); // if queue is empty
		}
		if (backIndex == frontIndex){
			backIndex = frontIndex + numItems - 1; //reset backIndex if queue is full
		}
			E temp = items[frontIndex]; //temp reference to item
			items[frontIndex] = null; //remove item from queue
			frontIndex = incrementIndex(frontIndex); //update frontIndex
			numItems --; //update numItems
			return temp; //return item
	} //end dequeue()
	
	/**
	 * Returns whether or not this queue is empty.
	 * @return True if this queue is empty, otherwise false.
	 */
	public boolean isEmpty(){
	if(numItems == 0){
		return true;
	} else return false;
	} // end isEmpty()
	
	/**
	 * Returns how many items are currently in the queue.
	 * @return how many items are currently in the queue.
	 */
	public int size(){
	return numItems;
	} // end size()
} // end class
