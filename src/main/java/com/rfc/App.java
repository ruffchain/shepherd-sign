package com.rfc;

import  org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import sun.rmi.runtime.Log;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LogManager.getLogger(App.class);
    public static void main( String[] args )
    {
        logger.info( "Hello World!" );
    }
}
