import java.io.*;

public class MaxSemipath implements Runnable{
    public static StringBuffer str = new StringBuffer();

    public static void main(String[] args) {
        new Thread(null, new MaxSemipath(), "", 64 * 1024 * 1024).start();
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(new FileWriter("out.txt"));
             BufferedReader reader = new BufferedReader(new FileReader("in.txt"))) {
            long key;
            String line;
            BinaryTree myTree = new BinaryTree();
            while ((line = reader.readLine()) != null) {
                key = Long.parseLong(line);
                myTree.insert(key);
            }
            if (myTree.root.left == null && myTree.root.right == null) {
                out.println(myTree.root.key);
                out.flush();
            }
            else {
                myTree.setHightsMSL();
                //myTree.valueToB();
                myTree.calculateA();
                myTree.postorderTravers();
                myTree.preorderTraversal();
                out.println(str);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Node {
    public long key;
    public Node parent, left, right;
    public long height, msl, numOfLeaves;
    public long a, b, c;
    public static long cMax = 0;

    public Node(long key) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.height = 0;
        this.msl = 0;
        this.numOfLeaves = 1;
        this.b = 0;
        this.a = 0;
    }
}
class BinaryTree {
    Node root;
    static long maxMSL = 0;

    public BinaryTree() {
        this.root = null;
    }

    private Node insertRec(Node root, long key, Node parent) {
        if (root == null) {
            root = new Node(key);
            root.parent = parent;
            return root;
        }

        if (key < root.key) {
            root.left = insertRec(root.left, key, root);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key, root);
        }

        return root;
    }

    public void insert(long key) {
        root = insertRec(root, key, null);
    }
    public void setHightsMSL() {
        setHightsMSGRec(root);
    }

    public void setHightsMSGRec(Node root) {
        if (root == null) {
            return;
        }
        setHightsMSGRec(root.left);

        setHightsMSGRec(root.right);

        long leftHeight = (root.left != null) ? root.left.height : -1;
        long rightHeight = (root.right != null) ? root.right.height : -1;
        root.height = Math.max(leftHeight, rightHeight) + 1;

        if (root.left == null && root.right == null) root.numOfLeaves = 1;
        if (root.left != null && root.right != null) {
            root.msl = root.left.height + root.right.height + 2;
            if (root.right.height == root.left.height) root.numOfLeaves = root.left.numOfLeaves + root.right.numOfLeaves;
            else root.numOfLeaves = (root.left.height > root.right.height) ? root.left.numOfLeaves : root.right.numOfLeaves;
        } else if (root.left != null) {
            root.msl = root.left.height + 1;
            root.numOfLeaves = root.left.numOfLeaves;
        } else if (root.right != null) {
            root.msl = root.right.height + 1;
            root.numOfLeaves = root.right.numOfLeaves;
        }
        if (root.msl > maxMSL) maxMSL = root.msl;
    }
    public void calculateA() {
        calculateARec(root);
    }

    private void calculateARec(Node node) {
        if (node == null) {
            return;
        }
        if (node.msl == maxMSL) {
            long leftLeaves = (node.left != null) ? node.left.numOfLeaves : 1;
            long rightLeaves = (node.right != null) ? node.right.numOfLeaves : 1;
            node.b = leftLeaves * rightLeaves;
        } else node.b = 0;

        if (node.right != null && node.left == null ) {
            node.right.a = node.a + node.b;
        } else if (node.left != null && node.right == null ) {
            node.left.a = node.a + node.b;
        } else if (node.left != null && node.right != null ) {
            if (node.right.height > node.left.height) {
                node.right.a = node.a + node.b;
                node.left.a = node.b;
            } else if (node.right.height < node.left.height) {
                node.left.a = node.a + node.b;
                node.right.a = node.b;
            } else {
                node.left.a = node.b + node.left.numOfLeaves * node.a / node.numOfLeaves;
                node.right.a = node.b + node.right.numOfLeaves * node.a / node.numOfLeaves;
            }
        }

        node.c = node.a + node.b;
        Node.cMax = Node.cMax < node.c ? node.c : Node.cMax;
        calculateARec(node.left);
        calculateARec(node.right);
    }
    public void preorderTraversal() {
        preorderTraversalRec(root);
    }

    private void preorderTraversalRec(Node node) {
        if (node == null) {
            return;
        }
        MaxSemipath.str.append(node.key).append("\n");

        preorderTraversalRec(node.left);

        preorderTraversalRec(node.right);
    }

    public void postorderTravers() {
        root = postorderTraversal(root);
    }

    public Node postorderTraversal(Node node) {
        if (node == null) {
            return null;
        }

        node.left = postorderTraversal(node.left);
        node.right = postorderTraversal(node.right);

        if (checkProperty(node)) {
            node = deleteNode(node);
            return node;
        }

        return node;
    }
    private Node deleteNode(Node node) {
        if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node successorParent = node;
            Node successor = node.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            if (successorParent != node) {
                successorParent.left = successor.right;
                successor.right = node.right;
            }

            successor.left = node.left;

            return successor;
        }
    }
    public boolean checkProperty(Node node) {
        return node.c == Node.cMax;
    }
}
