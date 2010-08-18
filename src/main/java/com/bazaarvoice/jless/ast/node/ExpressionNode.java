package com.bazaarvoice.jless.ast.node;

import com.bazaarvoice.jless.ast.visitor.NodeTraversalVisitor;

import java.util.ListIterator;

public class ExpressionNode extends InternalNode {

    public ExpressionNode(Node child) {
        super(child);
    }

    @Override
    public boolean traverse(NodeTraversalVisitor visitor) {
        if (visitor.enter(this)) {
            ListIterator<Node> it = pushChildIterator();
            while (it.hasNext()) {
                Node child = it.next();
                if (!child.traverse(visitor)) {
                    break;
                }
            }
            popChildIterator();
        }

        return visitor.visit(this);
    }
}
