/*
 * The MIT License
 *
 * Copyright (c) Red Hat, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sun.tools.hat.internal.server;

import com.sun.tools.hat.internal.model.JavaHeapObject;
import java.util.List;

/**
 *
 * Show objects which are unreachable from roots objects
 * 
 * @author Lucie Votypkova
 */
public class UnreachableObjectsQuery extends QueryHandler{

    @Override
    void run() {
        String className = null;
        List<JavaHeapObject> unreachableObjects = null;
        if(query==null || query.isEmpty()){
            unreachableObjects = snapshot.getUnreachableObjects();
        }
        else{
            String parameters[] = query.split("&");
            className = parameters[0].substring(7);
            boolean subclass = parameters[1].contains("All");
            unreachableObjects = snapshot.getUnreachableObjects(className,subclass);
        }
        startHtml("Unreachable objects from roots");
        out.println("<br><br>");
        out.println("<form action='/unreachableObjects/' method='get'>");
        out.print("Filter objects by class:");
        out.print("<input type='text' name='class' />");
        out.println("<input type='submit' value='All instances' name='subclassOk' />");
        out.println("<input type='submit' value='Dirrect instances' name='subclassOk' />");
        out.println("<br>");
        out.println("(Filter does not include interfaces, because hprof format does not provide informaiton about interfaces)");
        out.println("</form>");
        out.println("<br>");
        if(className!=null){
            out.println("Filtered by " + className);
            out.println("<br>");
        }
        for(JavaHeapObject obj : unreachableObjects){
            printThing(obj);
            out.println("<br>");
        }
    }
    
}
