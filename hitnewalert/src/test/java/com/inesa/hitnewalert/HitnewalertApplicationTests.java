package com.inesa.hitnewalert;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inesa.hitnewalert.entity.HitNewDataVo;
import com.inesa.hitnewalert.entity.Hitnew;
import com.inesa.hitnewalert.mapper.HitNewDataVoMapper;
import com.inesa.hitnewalert.mapper.HitnewMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class HitnewalertApplicationTests {
    @Autowired
    private HitNewDataVoMapper hitNewDataVoMapper;

    @Autowired
    private HitnewMapper hitnewMapper;
    @Test
    void contextLoads() {
        Hitnew hitnew = hitnewMapper.selectById(1);
        System.out.println(hitnew);
    }

    @Test
    public void query_hitnewdatas() throws IOException {
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
        }else{
            System.out.println("无打新");
        }

    }



    @Test
    public void insert_hitnewdatas() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // https://www.jisilu.cn/webapi/cb/pre/?history=N
        HttpGet get = new HttpGet("https://www.jisilu.cn/webapi/cb/pre/?history=N");
        get.addHeader("cookie", "kbzw__Session=v4ckuot8ijfo57rof381kjb3m4; Hm_lvt_164fe01b1433a19b507595a43bf58262=1677648433; kbz_newcookie=1; Hm_lpvt_164fe01b1433a19b507595a43bf58262=1677653421");
        get.addHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse response = httpClient.execute(get);



        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            HitNewDataVo hitNewDataVo = new HitNewDataVo();
            HitNewDataVo hitNewDataVo1 = JSON.parseObject(res, HitNewDataVo.class);
            List<HitNewDataVo.DataBean> dataBeanList = hitNewDataVo1.getData();


            dataBeanList.stream().forEach(dataBean -> {
                // bond_nm : 中旗转债
                String bond_nm = dataBean.getBond_nm();
                if(bond_nm==null){

                }else{
                    System.out.println(bond_nm);
                    // progress_dt 2023-03-03
                    String progress_dt = dataBean.getProgress_dt();
                    Hitnew hitnew = new Hitnew();
                    hitnew.setName(bond_nm);
                    hitnew.setTime(progress_dt);

                    QueryWrapper<Hitnew> hitnewQueryWrapper = new QueryWrapper<>();
                    hitnewQueryWrapper.eq("name",bond_nm);

                    Hitnew hitnew1 = hitnewMapper.selectOne(hitnewQueryWrapper);
                    if(hitnew1==null){
                        int insert = hitnewMapper.insert(hitnew);
                        if(insert==1){
                            System.out.println("数据更新成功！");
                        }

                    }

                }

            });


//            Top50.DataBean dataBean = data.get(0);
//            String title = dataBean.getTarget().getTitle();
//            System.out.println(title);
//            String openAiQuestion = openAI.doChatGPT(title);
//            System.out.println(openAiQuestion);

        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

}
