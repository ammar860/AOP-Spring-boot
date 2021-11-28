package com.demo.aop;

import com.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

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
        User user = findUser(pjp);
        String str = find_userId(pjp);
        logger.info("method invoked "+ methodName + "()" + "  with user_id: "
                + user.getId() + "  param  " + str);

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
    public User findUser(ProceedingJoinPoint pjp){
        for (Object obj: pjp.getArgs()) {
            if(obj instanceof User){
                return (User) obj;
            }
        }

        return null;
    }

    public String find_userId(ProceedingJoinPoint pjp){
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof RequestParam))
                    continue;
                RequestParam requestParam = (RequestParam) annotation;
                if (! "userid".equals(requestParam.value()))
                    continue;
                return args[argIndex].toString();
            }
        }

        return null;
    }
}
