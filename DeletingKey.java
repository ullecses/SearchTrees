import java.io.*;
import java.util.*;

public class DeletingKey implements Runnable {
    public static void main(String[] args) throws IOException {
        new Thread(null, new DeletingKey(), "", 64 * 1024 * 1024).start();
    }


    @Override
    public void run() {
        Scanner in = null;
        try {
            in = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long deleteVertex = in.nextInt();
        long temp;
        BinaryTree tree = new BinaryTree();
        boolean flag = false;
        while(in.hasNext()) {
            temp = in.nextInt();
            if (temp == deleteVertex) flag = true;
            tree.insert(temp);
        }
        if (flag) tree.delete(deleteVertex);
        tree.preorderTraversal();
        out.println(BinaryTree.str);
        out.flush();
    }
}
class Node {
    long key;
    Node left;
    Node right;

    public Node(long key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }
}

class BinaryTree {
    Node root;
    public static StringBuilder str = new StringBuilder();

    public BinaryTree() {
        this.root = null;
    }

    public void insert(long key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node root, long key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (key < root.key) {
            root.left = insertRec(root.left, key);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key);
        }

        return root;
    }

    public void delete(long key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node root, long key) {
        if (root == null) {
            return root;
        }

        if (key < root.key) {
            root.left = deleteRec(root.left, key);
        } else if (key > root.key) {
            root.right = deleteRec(root.right, key);
        } else {
            // Найден узел для удаления

            // Узел без потомков или с одним потомком
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Узел с двумя потомками
            root.key = minValue(root.right);

            // Удаление наименьшего элемента в правом поддереве
            root.right = deleteRec(root.right, root.key);
        }

        return root;
    }

    private long minValue(Node root) {
        long minVal = root.key;
        while (root.left != null) {
            minVal = root.left.key;
            root = root.left;
        }
        return minVal;
    }
    public void preorderTraversal() {
        preorderRec(root);
    }

    private void preorderRec(Node root) {
        if (root != null) {
            str.append(root.key).append(System.lineSeparator());
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }
}