/*
 * Copyright (c) 1997, 2008, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


/*
 * The Original Code is HAT. The Initial Developer of the
 * Original Code is Bill Foote, with contributions from others
 * at JavaSoft/Sun.
 */

package com.sun.tools.hat.internal.parser;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * InputStream that keeps track of total bytes read (in effect
 * 'position' in stream) from the input stream.
 *
 */
public class PositionInputStream extends BufferedInputStream {
    
    private long position = 0L;
    
    private int byteBuffer = -1;
    
    private long markedPosition = 0L;
    
    public PositionInputStream(InputStream in, int byteBuffer) {
        super(in);
        this.byteBuffer = byteBuffer;
    }
    
    public int read() throws IOException {
        int res = super.read();
        if (res != -1) position++;
        return res;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int res = super.read(b, off, len);
        if (res != -1) position += res;
        return res;
    }

    public long skip(long n) throws IOException {
        long res = super.skip(n);
        position += res;
        return res;
    }
    
    public synchronized void mark(){
        if(byteBuffer< 0){
            // do nothing
            return;
        }
        super.mark(marklimit);
        markedPosition = position;
    }
    
    public synchronized long backOnMarkedPosition() throws IOException{
        if(byteBuffer< 0 ){
            //do nothing
            return 0;
        }
        super.reset();
        long returnedCount = position - markedPosition;
        position = markedPosition;
        return returnedCount;
    }
  
    public long position() {
        return position;
    }
}
