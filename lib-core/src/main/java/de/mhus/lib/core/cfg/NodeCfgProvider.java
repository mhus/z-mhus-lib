package de.mhus.lib.core.cfg;

import de.mhus.lib.core.node.INode;

public abstract class NodeCfgProvider extends CfgProvider {

    protected INode config;

    public NodeCfgProvider(String name) {
        super(name);
    }

    @Override
    public INode getConfig() {
        return config;
    }

}
