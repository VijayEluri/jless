package com.bazaarvoice.jless.ast;

import com.bazaarvoice.jless.ast.visitor.NodeVisitor;

import java.util.ListIterator;

public class PlaceholderNode extends Node {

    public PlaceholderNode() {
        super();
    }

    public PlaceholderNode(Node child) {
        super(child);
    }

    @Override
    public boolean accept(NodeVisitor visitor) {
        if (visitor.visitEnter(this)) {
            ListIterator<Node> it = pushChildIterator();
            while (it.hasNext()) {
                Node child = it.next();
                if (!child.accept(visitor)) {
                    break;
                }
            }
            popChildIterator();
        }

        return visitor.visit(this);
    }
}