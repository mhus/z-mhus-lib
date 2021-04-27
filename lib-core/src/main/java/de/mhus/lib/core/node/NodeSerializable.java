package de.mhus.lib.core.node;

public interface NodeSerializable {

    
    /**
     * Read the inner state of the object from the given node object.
     * @param node Node with stored state for this object
     * @throws Exception 
     */
    void readSerializabledNode(INode node) throws Exception;
    
    /**
     * Write the inner state of the object to the given node object.
     * @param node Node to store the state of this object in
     * @throws Exception 
     */
    void writeSerializabledNode(INode node) throws Exception;
    
}
