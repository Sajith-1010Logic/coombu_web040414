package com.coombu.photobook.interceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fekade Aytaged
 * 
 * Logs Entering and Exiting messages for method calls and provides the class,
 * method, and execution time.
 */
public class MethodCallLoggingInterceptor implements MethodInterceptor{
	    private static Logger log = LoggerFactory
	            .getLogger(MethodCallLoggingInterceptor.class);

	    /*
	     * (non-Javadoc)
	     * 
	     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	     */
	    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
	        String declaringClass = methodInvocation.getMethod()
	                .getDeclaringClass().toString();
	        String methodName = methodInvocation.getMethod().getName();
	        log.debug("Entering method: " + declaringClass + "::" + methodName);
	        long startTime = System.currentTimeMillis();
	        try {
	            Object retVal = methodInvocation.proceed();
	            return retVal;
	        } finally {
	            log.debug("Exiting method: " + declaringClass + "::" + methodName
	                    + " execution: " + (System.currentTimeMillis() - startTime)
	                    + " ms");
	        }
	    }	
}
