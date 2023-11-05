package com.xh.polling;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author H.Yang
 * @date 2023/11/5
 */
@Controller
@RequestMapping(produces = "text/html;charset=UTF-8")
public class AjaxLongPollingController {

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    public static final String[] NEWS = {
            "111",
            "222",
            "333",
            "444",
            "555"
    };


    @RequestMapping("/pushnews")
    public String news() {
        return "pushNews";
    }

    @RequestMapping(value = "/realTimeNews")
    @ResponseBody
    /*在WebInitializer中要加上servlet.setAsyncSupported(true);*/
    public DeferredResult<String> realtimeNews(HttpServletRequest request) {
        // 超时时间.
        long timeoutValue = 300;
        final DeferredResult<String> dr = new DeferredResult<>(timeoutValue);

        executorService.submit(() -> {
            //执行耗时的逻辑
            try {
                //模拟业务代码执行时间
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int index = new Random().nextInt(NEWS.length);

            //返回结果.
            dr.setResult(NEWS[index]);
        });
        return dr;
    }

    @RequestMapping(value = "/realTimeNews2")
    @ResponseBody
    public Callable<String> realtimeNews2(HttpServletRequest request) {

        Callable callable = () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int index = new Random().nextInt(NEWS.length);
            return NEWS[index];
        };
        return callable;
    }

}
