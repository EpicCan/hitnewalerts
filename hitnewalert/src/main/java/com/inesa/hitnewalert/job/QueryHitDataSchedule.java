package com.inesa.hitnewalert.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inesa.hitnewalert.entity.Hitnew;
import com.inesa.hitnewalert.mapper.HitnewMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 小傅哥，微信：fustack
 * @description 问题任务
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
@EnableScheduling
@Configuration
public class QueryHitDataSchedule {

    private Logger logger = LoggerFactory.getLogger(QueryHitDataSchedule.class);

//    @Value("${chatbot-api.groupId}")
//    private String groupId;


    @Autowired
    private HitnewMapper hitnewMapper;


    // 表达式：cron.qqe2.com
    @Scheduled(cron = "0/30 * * * * ?")
    public void run() {
        try {
            if (new Random().nextBoolean()) {
                logger.info("随机打烊中...");
                return;
            }

            GregorianCalendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 22 || hour < 7) {
                logger.info("打烊时间不工作，AI 下班了！");
                return;
            }
            Date date=new Date();//此时date为当前的时间
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");//设置当前时间的格式，为年-月-日
            String sDate = dateFormat.format(date);
            System.out.println(sDate);

            QueryWrapper<Hitnew> hitnewQueryWrapper = new QueryWrapper<>();
            hitnewQueryWrapper.eq("time",sDate);
            List<Hitnew> hitnews = hitnewMapper.selectList(hitnewQueryWrapper);


            if(hitnews!=null){
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                // http://www.pushplus.plus/send?token=XXXXX&title=XXX&content=XXX&template=html
                HttpGet get = new HttpGet("http://www.pushplus.plus/send?token=dba7a3e4ed214357b669acf4dc27ce9f&title=打新提醒&content=可打新&template=html");
                CloseableHttpResponse response = httpClient.execute(get);
                System.out.println("可打新");
                logger.info("可打新");
            }else{
                System.out.println("无打新");
                logger.info("无打新");
            }


//         logger.info("编号：{} 问题：{} 回答：{} 状态：{}", topic.getTopic_id(), topic.getQuestion().getText(), answer, status);
        } catch (Exception e) {
            logger.error("自动回答问题异常", e);
        }
    }

}
