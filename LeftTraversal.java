import java.io.*;
import java.util.*;

public class LeftTraversal implements Runnable{
    static PrintWriter out;

    static {
        try {
            out = new PrintWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LeftTraversal() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        new Thread(null, new LeftTraversal(), "", 64 * 1024 * 1024).start();
    }

    @Override
    public void run() {
        Scanner in = null;
        try {
            in = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Tree tree = new Tree();
        while (in.hasNext()) {
            tree.insert(in.nextLong());
        }

        tree.preorderTraversal();
        out.flush();
    }

    static class Node {
        public long key;
        public Node left;
        public Node right;

        public Node(long key) {
            this.key = key;
        }
    }
    static class Tree {
        private Node root;
        public void insert(long x) {
            Node parent = null;
            Node node = root;
            while (node != null) {
                parent = node;
                if (x < node.key) {
                    node = node.left;
                } else if (x > node.key) {
                    node = node.right;
                } else {
                    return;
                }
            }

            Node newNode = new Node(x);

            if (parent == null) {
                root = newNode;
            } else if (x < parent.key) {
                parent.left = newNode;
            } else if (x > parent.key) {
                parent.right = newNode;
            }
        }
        public void preorderTraversal() {
            preorderTraversal(root);
        }

        private void preorderTraversal(Node node) {
            if (node == null) {
                return;
            }

            out.println(node.key);
            preorderTraversal(node.left);
            preorderTraversal(node.right);
        }
    }
}
