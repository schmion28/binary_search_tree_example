import java.lang.reflect.Array;
import java.util.*;

/**
 * Class for full balanced binary search tree.
 * This tree balances when a new node is added.
 */
public class BalancedBST<T extends Comparable> {
    private Node root; // Root node.
    private int nodeCount = 0;

    /**
     * Node class contains a value; and three references to nodes as left, right, and parent.
     */
    protected class Node{

        private T value; // Value of node.
        private Node left; // Reference to left node.
        private Node right; // Reference to right node.
        private Node parent; // Reference to parent node.
        private int count;
        private int height;

        /**
         * Constructor to build fully instantiated node.
         * @param value Value of node.
         * @param parent Reference to parent node.
         * @param left Reference to left node.
         * @param right Reference to right node.
         */
        Node(T value, Node parent, Node left, Node right){
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.count = 1;
        }

        /**
         * Constructor to build node with no children. If root node, 'parent' is null.
         * @param value Value of node.
         * @param parent Reference to parent node.
         */
        Node(T value, Node parent){
            this(value, parent, null, null);
        }

        /**
         * If there are multiple nodes of the same value, program may increase count of a node by calling this method.
         */
        void increment(){
            this.count++;
        }

        /**
         * If there are multiple nodes of the same value, program may decrement count of a node by calling this method.
         */
        void decrement(){
            this.count++;
        }

        /**
         * Access the count of this node.
         * @return Count of the node.
         */
        int getCount(){
            return this.count;
        }

        /**
         * Appends a node to the current node as a child.
         * If the node is not first added to the root, then heights must be reset and the tree balanced manually.
         * @param node Node to be appended.
         */
        @SuppressWarnings("unchecked")
        void appendChild(Node node){

            // Get comparison value of the nodes.
            int compareValue = node.value.compareTo(this.value);

            // If they are equal, increase count.
            if (compareValue == 0) {
                increment();
            }

            // If the node is less, add it to the left.
            if (compareValue < 0){
                // If left child is null, put the new node there.
                if (this.left == null) {
                    this.left = node;
                    this.left.parent = this;
                }
                // Otherwise, send it down the chain.
                else this.left.appendChild(node);
            }

            // If the node is more, add it to the right.
            if (compareValue > 0){
                // If the right child is null, put the new node there.
                if (this.right == null) {
                    this.right = node;
                    this.right.parent = this;
                }
                // Otherwise, send it down the chain.
                else this.right.appendChild(node);
            }

            // If this is the root node, reset the heights and re-balance.
            if (this.parent==null){
                this.heights();
                BalancedBST.this.balance();
            }
        }

        /**
         * Makes an Array-list the in-order array-representation of the values of the nodes.
         * Adds an item for every count of each node.
         * @return ArrayList of the values of the node children's values, in order.
         */
        ArrayList<T> toInorderArray(){

            // Make an ArrayList for the current recursion.
            ArrayList<T> list = new ArrayList<>();

            // Append the ArrayList for the right children.
            if (this.left != null) list.addAll(this.left.toInorderArray());

            // Append the value of this node for the number of times of its count.
            for (int i=0; i < this.count; i++) list.add(this.value);

            // Append the ArrayList for the left children.
            if (this.right != null) list.addAll(this.right.toInorderArray());

            // Send the current ArrayList back up.
            return list;
        }

        /**
         * This method balances the children of the node it is called on and resets the height if it is the parent node.
         * If this method is not called on the parent node, heights() should be called manually on the node.
         * @param values Array of values for the node and its children to contain.
         */
        void balance(T[] values){
            // Create a list without duplicates to get the true list center's value.
            // Then, get the index of that value in the full list/array.
            Set<T> set = new LinkedHashSet<>(Arrays.asList(values).subList(0, values.length));
            ArrayList<T> list = new ArrayList<>(set);
            ArrayList<T> listFull = new ArrayList<>(Arrays.asList(values));

            // Get the center value and set this.value to it.
            int center = listFull.indexOf(list.get(list.size()/2));
            this.value = values[center];

            // Count the occurrences of the current value in the array.
            int occ = Collections.frequency(listFull, this.value);
            this.count = occ;

            // Make arrays to be passed to the left and right nodes.
            T[] left = Arrays.copyOfRange(values,0, center);
            T[] right = Arrays.copyOfRange(values,center+occ,values.length);

            // If there are smaller values, pass it to a new node as the left node.
            if (left.length > 0){
                this.left = new Node(null, this);
                this.left.balance(left);
            }

            // If there are larger values, pass it to a new node as the right node.
            if (right.length > 0){
                this.right = new Node(null, this);
                this.right.balance(right);
            }

            // If there is no parent node ie this is the root node, reset the heights.
            if (this.parent == null) this.heights();
        }

        /**
         * Makes an ArrayList of the values in the tree in Inorder notation, including nulls.
         * @param recursionLevel When initially called this should be 0.
         * @param rootHeight Height of the initial node this method is being called on.
         * @return ArrayList of the values in the tree.
         */
        ArrayList<T> toInorderArrayWithNulls(int recursionLevel, int rootHeight){
            // Make an ArrayList for the current recursion.
            ArrayList<T> list = new ArrayList<>();

            // Append the ArrayList for the right children.
            if (this.left != null) list.addAll(this.left.toInorderArrayWithNulls(recursionLevel + 1, rootHeight));
            else for (int i=0; i < (Math.pow(2, rootHeight-recursionLevel)-1);i++) list.add(null);

            // Append the value of this node for the number of times of its count.
            for (int i=0; i < this.count; i++) list.add(this.value);

            // Append the ArrayList for the left children.
            if (this.right != null) list.addAll(this.right.toInorderArrayWithNulls(recursionLevel + 1, rootHeight));
            else for (int i=0; i < (Math.pow(2, rootHeight-recursionLevel)-1);i++) list.add(null);

            // Send the current ArrayList back up.
            return list;
        }

        /**
         * Makes a two-dimensional array of the values in the tree, including nulls.
         * @param recursionLevel When initially called this should be 0.
         * @param list When initially called this should be null.
         * @param rootHeight Height of the initial node this method is being called on.
         * @return ArrayList of ArrayLists of the values in each level of the tree.
         */
        ArrayList<ArrayList<T>> toMatrix(int recursionLevel, ArrayList<ArrayList<T>> list, int rootHeight){

            // If this is the first level, initialize the list.
            if (list == null) {
                list = new ArrayList<>();
                // Add a list for each level of the tree.
                for (int i=0; i<this.heights(); i++) list.add(new ArrayList<>());
            }

            // Add this node's value to this level's list.
            list.get(recursionLevel).add(this.value);

            // If the left node is not null, recurse this method there.
            if (this.left != null) this.left.toMatrix(recursionLevel + 1, list, rootHeight);
            // Otherwise append null nodes to each list accordingly.
            else if (recursionLevel + 1 != rootHeight) for (int i=recursionLevel+1; i<rootHeight; i++) for (int j=0; j<Math.pow(2, (i-(recursionLevel+1))); j++) list.get(i).add(null);

            // If the left node is not null, recurse this method there.
            if (this.right != null) this.right.toMatrix(recursionLevel + 1, list, rootHeight);
            // Otherwise append null nodes to each list accordingly.
            else if (recursionLevel + 1 != rootHeight) for (int i=recursionLevel+1; i<rootHeight; i++) for (int j=0; j<Math.pow(2, (i-(recursionLevel+1))); j++) list.get(i).add(null);

            return list;
        }

        /**
         * Makes a two-dimensional array of the values in the tree as strings, including nulls.
         * Entries in the matrix include a count, which follows the value, for example '3.14 (x8)'
         * @param recursionLevel When initially called this should be 0.
         * @param list When initially called this should be null.
         * @param rootHeight Height of the initial node this method is being called on.
         * @return ArrayList of ArrayLists of the values in each level of the tree.
         */
        ArrayList<ArrayList<String>> toStringMatrix(int recursionLevel, ArrayList<ArrayList<String>> list, int rootHeight){

            // If this is the first level, initialize the list.
            if (list == null) {
                list = new ArrayList<>();
                // Add a list for each level of the tree.
                for (int i=0; i<this.heights(); i++) list.add(new ArrayList<>());
            }

            // Add this node's value to this level's list.
            list.get(recursionLevel).add(this.value.toString() + " (x" + this.count + ")");

            // If the left node is not null, recurse this method there.
            if (this.left != null) this.left.toStringMatrix(recursionLevel + 1, list, rootHeight);
            // Otherwise append null nodes to each list accordingly.
            else if (recursionLevel + 1 != rootHeight) for (int i=recursionLevel+1; i<rootHeight; i++) for (int j=0; j<Math.pow(2, (i-(recursionLevel+1))); j++) list.get(i).add("∅");

            // If the left node is not null, recurse this method there.
            if (this.right != null) this.right.toStringMatrix(recursionLevel + 1, list, rootHeight);
            // Otherwise append null nodes to each list accordingly.
            else if (recursionLevel + 1 != rootHeight) for (int i=recursionLevel+1; i<rootHeight; i++) for (int j=0; j<Math.pow(2, (i-(recursionLevel+1))); j++) list.get(i).add("∅");

            return list;
        }

        /**
         * Sets the height variable of a node and its children.
         * @return Height of the node.
         */
        int heights(){

            // Get (and set) heights of children
            int leftHeight = this.left == null ? 0 : this.left.heights();
            int rightHeight = this.right == null ? 0 : this.right.heights();

            // Find the higher of the two and set and return it, plus one.
            this.height = Math.max(leftHeight, rightHeight) + 1;
            return this.height;
        }
    }

    /**
     * Constructor to make a new BalancedBST with a root node of a passed value.
     * @param valueOfRoot Value of the root of the BST.
     */
    BalancedBST(T valueOfRoot){
        this.root = new Node(valueOfRoot, null);
        this.nodeCount++;
        this.root.heights();
    }

    /**
     * Constructor to make an empty BalancedBST.
     */
    BalancedBST(){
        this.root = null;
    }

    /**
     * Add a node to the BST.
     * @param value Value to be placed in node.
     */
    void addNode(T value){
        if (nodeCount==0) this.root = new Node(value, null);
        else root.appendChild(new Node(value, this.root));
        this.nodeCount++;
        this.root.heights();
    }

    /**
     * Creates a string representation of the nodes in the tree.
     * @return String of nodes in the tree, comma + space delimited.
     */
    public String toString(){

        // Get the ArrayList of the root node and instantiate a StringBuilder.
        ArrayList<T> list = this.root.toInorderArray();
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate through the list and append the value to the StringBuilder.
        for (T t : list){
            stringBuilder.append(", ");
            stringBuilder.append(t.toString());
        }

        // Remove the initial delimiter from the string (if it contains them).
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(0);
        }

        // Make a string from the builder and return it.
        return stringBuilder.toString();
    }

    /**
     * Creates a string representation of the nodes in the tree including nulls.
     * @return String of nodes in the tree including nulls, comma + space delimited.
     */
    String toStringTree(){
        ArrayList<T> list = this.root.toInorderArrayWithNulls(1, this.root.height);
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate through the list and append the value to the StringBuilder.
        for (T t : list){
            stringBuilder.append(", ");
            stringBuilder.append(t==null?"∅":t.toString());
        }

        // Remove the initial delimiter from the string (if it contains them).
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(0);
        }

        // Make a string from the builder and return it.
        return stringBuilder.toString();
    }

    /**
     * Balances the tree.
     */
    @SuppressWarnings("unchecked")
    private void balance(){
        // Make a list of the values in the tree and an int for that array's size.
        ArrayList<T> array = this.root.toInorderArray();
        int size = array.size();

        // Iterate through the list and add the values to a new array.
        T[] ts = (T[])Array.newInstance(this.root.value.getClass(), size);
        for (int j=0; j<size; j++) ts[j] = array.get(j);

        // Balance the tree.
        this.root.balance(ts);
    }

    /**
     * Get the height of the tree.
     * @return Height of the tree.
     */
    int getHeight(){
        return root.heights();
    }

    /**
     * Get the tree in a two-dimensional array representation, with each row of the same height as a row in the matrix.
     * @return Two-dimensional array representation of the tree.
     */
    @SuppressWarnings("unchecked")
    T[][] toMatrix(){

        // Create an array of T to get its class.
        Class c = ((T[])Array.newInstance(this.root.value.getClass(), 0)).getClass();

        // Get the 2D-ArrayList from the root node of the tree.
        ArrayList<ArrayList<T>> list = this.root.toMatrix(0,null, this.root.heights());

        // Initialize the return matrix.
        T[][] bigArray = (T[][]) (Array.newInstance(c, this.root.heights()));

        // Populate the return matrix with the lists from the root's 'toMatrix'.
        for (int i=0; i<bigArray.length;i++){
            // Get the size of the list.
            int size = list.get(i).size();
            // Initialize the array.
            T[] ts = (T[])Array.newInstance(this.root.value.getClass(), size);
            // Move the current iteration's value over.
            for (int j=0; j<size; j++) ts[j] = list.get(i).get(j);
            // Set the array to the temporary one.
            bigArray[i] = ts;
        }
        return bigArray;
    }

    /**
     * Get the tree in a two-dimensional array representation, with each row of the same height as a row in the matrix.
     * This methods represents the values as strings with a postfix denoting count of the node.
     * @return Two-dimensional array representation of the tree.
     */
    String[][] toStringMatrix(){

        // Get the 2D-ArrayList from the root node of the tree.
        ArrayList<ArrayList<String>> list = this.root.toStringMatrix(0, null, this.root.heights());

        // Initialize the return matrix.
        String[][] strings = new String[list.size()][];

        // Populate the return matrix with the lists from the root's 'toStringMatrix'.
        for (int i=0; i<strings.length;i++){
            // Get the size of the list.
            int size = list.get(i).size();
            // Initialize the array.
            String[] temps = new String[size];
            // Move the current iteration's value over.
            for (int j=0; j<size; j++) temps[j] = list.get(i).get(j);
            // Set the array to the temporary one.
            strings[i] = temps;
        }
        return strings;
    }
}
