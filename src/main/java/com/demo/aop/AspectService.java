package com.demo.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Aspect
@Component
public class AspectService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /*

    @Before(value = "execution(* com.demo.*.*.*())")
    public void beforeProcess() throws Throwable {
        logger.info("Process Started");
    }
    @After(value = "execution(* com.demo.controller.ProjectController.*.*())")
    public void afterProcess() throws Throwable {
        logger.info("Process Ended");
    }

     */


    @Pointcut(value="execution(* com.demo.controller.*.*(..) )")
    public void myPointcut() {

    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        Object[] array = pjp.getArgs();
        logger.info("method invoked "+ methodName + "()" + "arguments : "
                + mapper.writeValueAsString(array));

        long startTime = System.currentTimeMillis();
        Object object = pjp.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("method invoked "+ methodName + "()" + "Response : "
                + mapper.writeValueAsString(object));
        logger.info("method invoked "+ methodName + "()" + "Time taken : "+(endTime-startTime)+"ms");
        return object;
    }

    @AfterThrowing(pointcut ="myPointcut()",throwing = "ex")
    public void exceptionHandler(JoinPoint joinPoint, Throwable ex){
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String stuff = signature.toString();
        String arguments = Arrays.toString(joinPoint.getArgs());
        logger.error("We have caught exception in method: "
                + methodName + " with arguments "
                + arguments + "\nand the full toString: " + stuff + "\nthe exception is: "
                + ex.getMessage());
    }
}
