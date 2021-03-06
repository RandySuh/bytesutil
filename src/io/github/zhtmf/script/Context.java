package io.github.zhtmf.script;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Map-like container holding global values which provides a context for 
 * script evaluation.
 * <p>
 * Property names are case sensitive.
 * 
 * @author dzh
 */
class Context extends AbstractMap<String, Object> {
    
    /**
     * At runtime, operands (identifiers, strings etc.) are pushed onto this stack
     * while operators pop them out, do calculation and push result back to it, if
     * any.
     */
    private final LinkedList<Object> operandStack = new LinkedList<>();
    
    
    /**
     * Actual map holding mappings of values
     */
    private Map<String,Object> values = new HashMap<String, Object>();
    
    private Map<Integer,Object> cachedIdentifierValues = new HashMap<Integer, Object>();
    
    /**
     * Initialize this context with values from a map
     * 
     * @param initialMap initial mappings in this context.
     */
    public Context(Map<String,Object> initialMap) {
        this.values.putAll(initialMap);
    }
    
    Object getCachedValue(Identifier id) {
        return cachedIdentifierValues.get(id.getId());
    }
    
    /**
     * Peek the first operand.
     * 
     * @return the first operand or null if there isn't any.
     */
    Object peek() {
        return operandStack.peek();
    }
    
    /**
     * Pop and return the first operand.
     * 
     * @return the first operand or null if there isn't any.
     */
    Object pop() {
        return operandStack.poll();
    }
    
    /**
     * Push an operand onto the operand stack.
     * @param operand
     */
    void push(Object operand) {
        if(operand instanceof Identifier) {
            /*
             * Cache its value on first encounter to deal with operators which modifies
             * value of identifiers in place.
             * Without this, b + b++ will be 11 not 10 and likely b + b++ + b
             * + b++ + b++ will be 24 instead of 23. Although 11 and 24 are exactly what
             * returns by C compiler we must be compatible with Java compilers.
             */
            Identifier identifier = (Identifier)operand;
            int id = identifier.getId();
            if(!cachedIdentifierValues.containsKey(id)) {
                cachedIdentifierValues.put(id, identifier.dereference(this));
            }
        }
        operandStack.push(operand);
    }

    /**
     * Recursively get an property in this context all one of its parents.
     */
    public Object get(Object name) {
        return values.get(name);
    }
    
    public Object put(String name,Object value) {
        values.put(name, value);
        return null;
    }
    
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return values.entrySet();
    }
    
}
