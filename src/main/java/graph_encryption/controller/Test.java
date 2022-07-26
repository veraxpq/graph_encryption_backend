package graph_encryption.controller;

import java.util.*;
class Node {
    String key;
    int value;
    List<Node> children;

    public Node() {}

    public Node(String key, int value) {
        this.key = key;
        this.value = value;
        children = new ArrayList<Node>();
    }
}
public class Test {
    static int cnt = 0;
    public static void main(String[] args) {
        // ** Tree-1 **
        //           a(1)
        //         /       \
        //      b(2)       c(3)
        //     /     \         \
        //   d(4)    e(5)      f(6)


        //  ** Tree-2 **
        //         a(1)
        //             \
        //            c(3)
        //                \
        //                f(66)
            // Tree-1
            Node nodeD = new Node("d", 4);
            Node nodeE = new Node("e", 5);
            Node nodeF = new Node("f", 6);

            Node nodeB = new Node("b", 2);
            nodeB.children = Arrays.asList(nodeD, nodeE);

            Node nodeC = new Node("c", 3);
            nodeC.children = Arrays.asList(nodeF);

            Node root1 = new Node("a", 1);
            root1.children = Arrays.asList(nodeB, nodeC);

            // Tree-2
            Node nodeC2 = new Node("c", 3);
            nodeC2.children = Arrays.asList(new Node("f", 66));
            Node root2 = new Node("a", 1);
            root2.children = Arrays.asList(nodeC2);

            System.out.println(countOperations(0, root1, root2));
        }

        public static int countOperations(int count, Node root1, Node root2) {
            if (root1 == null && root2 == null) {
                return count;
            }
            if (root1 == null || root2 == null) {
                return count + (root1 == null ? getTotalNodes(root2) : getTotalNodes(root1));
            }
            if (root1.key != root2.key && root1.value != root2.value) {
                count += 2;
            } else if (root1.key != root2.key || root1.value != root2.value) {
                count += 2;
            }
            return 0;
        }

    private static int getTotalNodes(Node node) {
        int numOfNodes = 1; // currentNode for the root input node

        for (Node currentNode: node.children) {
            // Default 1 for current processing Node if no childs
            numOfNodes += currentNode.children.size() != 0 ? currentNode.children.size() : 1;
        }

        return numOfNodes;
    }

}
