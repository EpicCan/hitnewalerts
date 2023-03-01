package com.inesa.hitnewalert;

import com.alibaba.fastjson.JSON;
import com.inesa.hitnewalert.vo.HitNewDataVo;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class HitnewalertApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void query_hitnewdatas() throws IOException {
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
                    System.out.println(progress_dt);
                    System.out.println();
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
