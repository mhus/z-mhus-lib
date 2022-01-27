package de.mhus.lib.core.operation;

import java.util.Map;

public class MutableOperationResult extends OperationResult {


    public MutableOperationResult() {}

    public MutableOperationResult(OperationDescription description) {
        if (description != null) {
            setOperationPath(description.getPath());
            setCaption(description.getCaption());
        }
    }

    public void setOperationPath(String operationPath) {
        this.path = operationPath;
    }

    public void setCaption(String title) {
        this.caption = title;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public void setNextOperation(OperationDescription nextOperation) {
        this.nextOperation = nextOperation;
    }

    /**
     * Set the result as object. Don't use it directly. Should be stored as INode or Map.
     *
     * @param result
     */
    @Deprecated
    public void setResult(Object result) {
        this.result = result;
    }

    public void setResultNode(Map<String, Object> result) {
        this.result = result;
    }

    public void setResultString(String result) {
        this.result = result;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }


}
