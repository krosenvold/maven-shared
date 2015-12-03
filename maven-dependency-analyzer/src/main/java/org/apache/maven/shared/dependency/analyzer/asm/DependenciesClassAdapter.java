package org.apache.maven.shared.dependency.analyzer.asm;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Shut the fuck up.
 */
public final class DependenciesClassAdapter
    extends RemappingClassAdapter
{

    private static final EmptyVisitor EMPTYVISITOR = new EmptyVisitor();

    public DependenciesClassAdapter()
    {
        super( EMPTYVISITOR, new CollectingRemapper() );
    }

    public Set<String> getDependencies()
    {
        return ( (CollectingRemapper) super.remapper ).classes;
    }

    private static class CollectingRemapper
        extends Remapper
    {

        final Set<String> classes = new HashSet<String>();

        public String map( String pClassName )
        {
            classes.add( pClassName.replace( '/', '.' ) );
            return pClassName;
        }
    }

    static class EmptyVisitor
        extends ClassVisitor
    {

        private static final AnnotationVisitor ANNOTATION_VISITOR = new AnnotationVisitor( Opcodes.ASM5 )
        {

            @Override
            public AnnotationVisitor visitAnnotation( String name, String desc )
            {
                return this;
            }

            @Override
            public AnnotationVisitor visitArray( String name )
            {
                return this;
            }
        };

        private static final MethodVisitor METHOD_VISITOR = new MethodVisitor( Opcodes.ASM5 )
        {

            @Override
            public AnnotationVisitor visitAnnotationDefault()
            {
                return ANNOTATION_VISITOR;
            }

            @Override
            public AnnotationVisitor visitAnnotation( String desc, boolean visible )
            {
                return ANNOTATION_VISITOR;
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation( int parameter, String desc, boolean visible )
            {
                return ANNOTATION_VISITOR;
            }
        };

        private static final FieldVisitor FIELD_VISITOR = new FieldVisitor( Opcodes.ASM5 )
        {
            @Override
            public AnnotationVisitor visitAnnotation( String desc, boolean visible )
            {
                return ANNOTATION_VISITOR;
            }
        };

        public EmptyVisitor()
        {
            super( Opcodes.ASM5 );
        }

        @Override
        public AnnotationVisitor visitAnnotation( String desc, boolean visible )
        {
            return ANNOTATION_VISITOR;
        }

        @Override
        public FieldVisitor visitField( int access, String name, String desc, String signature, Object value )
        {
            return FIELD_VISITOR;
        }


        @Override
        public MethodVisitor visitMethod( int access, String name, String desc, String signature, String[] exceptions )
        {
            return METHOD_VISITOR;
        }
    }
}
