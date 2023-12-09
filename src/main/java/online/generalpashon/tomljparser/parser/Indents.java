package online.generalpashon.tomljparser.parser;

import java.util.Collection;
import java.util.Stack;

public class Indents{

    private final Stack<Indent> queue;
    private int indents;

    public Indents(){
        this.queue = new Stack<>();
    }

    public void add(String key, int spaces){
        queue.add(new Indent(key, spaces));
        indents += spaces;
    }

    public void remove(int spaces){
        if(queue.isEmpty())
            return;

        if(spaces == 0 && queue.peek().spaces == 0){
            queue.pop();
            return;
        }

        while(spaces > 0){
            final int popSpaces = queue.peek().spaces;
            if(spaces < popSpaces)
                break;

            queue.pop();
            spaces -= popSpaces;
            indents -= popSpaces;
        }
    }

    public void removeTo(int spaces){
        remove(indents - spaces);
    }

    public Collection<Indent> toCollection(){
        return queue;
    }


    public static class Indent{

        public final String key;
        public final int spaces;

        public Indent(String key, int spaces){
            this.key = key;
            this.spaces = spaces;
        }

    }

}
