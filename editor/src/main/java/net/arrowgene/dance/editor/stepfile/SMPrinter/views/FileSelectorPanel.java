package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A panel that contains a tree-like structure for selecting which sim files
 * to open in the application. Select a single directory as the base, and all
 * child directories will be shown and can be navigated through.
 *
 * @author Dan
 */

public class FileSelectorPanel extends BasePanel implements TreeSelectionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JTree tree;

    public FileSelectorPanel(MainFrame main) {
        this(main, null);
    }

    public FileSelectorPanel(MainFrame main, File dir) {
        super(main);
        setLayout(new BorderLayout());

        openFileOrDirectory(dir);
    }

    public void openFileOrDirectory(File dir) {
        removeAll();
        if (dir != null) {
            tree = new JTree(populateTree(new DefaultMutableTreeNode(dir), dir));
        } else {
            tree = new JTree(new DefaultMutableTreeNode("Open a directory or file through the menu!"));
        }
        tree.addTreeSelectionListener(this);
        tree.setBorder(new EmptyBorder(8, 6, 8, 6));

        JScrollPane scrollPane = new JScrollPane(tree);

        add(BorderLayout.CENTER, scrollPane);
        validate();
        repaint();
    }

    private DefaultMutableTreeNode populateTree(DefaultMutableTreeNode curNode, File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] paths = dir.list();
            Arrays.sort(paths, String.CASE_INSENSITIVE_ORDER);

            //have to add directories then files, or else ordering is wrong
            List<DefaultMutableTreeNode> directoryList = new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> fileList = new ArrayList<DefaultMutableTreeNode>();
            for (String fileName : paths) {
                File nextFile = new File(dir.getAbsolutePath() + File.separator + fileName);
                if (nextFile.isDirectory()) {
                    NodeItem nodeItem = new NodeItem(nextFile);
                    directoryList.add(populateTree(new DefaultMutableTreeNode(nodeItem), nextFile));
                } else {
                    if (fileName.endsWith(".sm")) {
                        NodeItem nodeItem = new NodeItem(nextFile);
                        fileList.add(populateTree(new DefaultMutableTreeNode(nodeItem), nextFile));
                    }
                }
            }

            addAll(curNode, directoryList);
            addAll(curNode, fileList);
        }
        return curNode;
    }

    private DefaultMutableTreeNode addAll(DefaultMutableTreeNode node, List<DefaultMutableTreeNode> toAddNodes) {
        for (DefaultMutableTreeNode toAddNode : toAddNodes) {
            node.add(toAddNode);
        }
        return node;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        if (node.getUserObject() instanceof NodeItem) {
            File selectedFile = ((NodeItem) node.getUserObject()).file;
            if (selectedFile.isFile()) {
                main.openSimFile(selectedFile.getAbsolutePath());
            }
        }
    }

    private class NodeItem {
        public File file;

        public NodeItem(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName();
        }
    }
}