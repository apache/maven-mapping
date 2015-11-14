package org.apache.maven.shared.mapping;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Properties;

import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;

/**
 * This is a ValueSource, that can be used in an Interpolator. It supports special expressions, like
 * <code>dashClassifier</code> and <code>dashClassifier?</code>.
 *
 * @version $Id$
 */
public class DashClassifierValueSource
    extends PropertiesBasedValueSource
{
    /**
     * Create the ValueSource.
     *
     * @param classifier The classifier that should be used during interpolation
     */
    public DashClassifierValueSource( String classifier )
    {
        super( createDashClassifierProperties( classifier ) );
    }

    private static Properties createDashClassifierProperties( String classifier )
    {
        Properties classifierMask = new Properties();

        if ( classifier != null )
        {
            classifierMask.setProperty( "dashClassifier?", "-" + classifier );
            classifierMask.setProperty( "dashClassifier", "-" + classifier );
        }
        else
        {
            classifierMask.setProperty( "dashClassifier?", "" );
            classifierMask.setProperty( "dashClassifier", "" );
            // Make sure that the classifier property is usable, if it is null
            classifierMask.setProperty( "classifier", "" );
        }
        return classifierMask;
    }
}
