package com.nuttu.aicloud;

import com.alibaba.fastjson.JSON;
import com.nuttu.aicloud.model.gateway.Gateway;
import com.nuttu.aicloud.model.operation.Operation;
import com.nuttu.aicloud.model.status.Status;
import com.nuttu.aicloud.mqtt.service.MqttService;
import com.nuttu.aicloud.repository.GatewayRepository;
import com.nuttu.aicloud.repository.OperationRepository;
import com.nuttu.aicloud.repository.StatusRepository;
import com.nuttu.aicloud.util.QRCodeUtil;
import com.sun.net.ssl.X509TrustManager;
import lombok.val;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Dem {
    @Autowired
    private OperationRepository operationRepository;


    private static Logger logger = LoggerFactory.getLogger(MqttService.class);
    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Test
    public void index() {
        Operation operation = new Operation();
        operation.setGateway_mac("ABCDEF123456");
        operation.setCreatedAt(new Date(System.currentTimeMillis()));
        operation.setUpdatedAt(new Date(System.currentTimeMillis()));
        operation.setOperation("root");

        Operation operation1 = operationRepository.save(operation);
        System.out.println(operation1);

    }

    private String readJSONString(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            reader.close();
        }
        catch(Exception e) {
            //System.out.println(e.toString());
        }
        return json.toString();
    }
    @Test
    public void indes() {

//        List<Point> points_gw = new ArrayList<>();
//        points_gw.add(Point.measurement("Gateway")
//                        .time(new Date().getTime(),TimeUnit.MILLISECONDS)
//                        .tag("gatewayMac","08201e5048f943639a093718c6aca5e2")
//                        .addField("name","itsname")
//
//                .build());
//        influxDBTemplate.write(points_gw);
        Query query = new Query("select * from Gateway","test");
        QueryResult query1 = influxDBTemplate.query(query);

        List<QueryResult.Result> results = query1.getResults();
        List<QueryResult.Series> series = null;
        Object[] array = null;
        int i = 0;
        for (QueryResult.Result result : results) {
//            series.add((QueryResult.Series) result.getSeries());
            System.out.println("values:");
            System.out.println(Arrays.toString(result.getSeries().get(i).getValues().toArray()));
            array = result.getSeries().get(i).getValues().toArray();
            System.out.println("Columns:");
            System.out.println(result.getSeries().get(i).getColumns().toString());
            System.out.println("name:");
            if(result.getSeries().get(i).getName()!=null){
                System.out.println(result.getSeries().get(i).getName());
            }
            System.out.println("Tags:");
            if(result.getSeries().get(i).getTags() != null){
                System.out.println(result.getSeries().get(i).getTags());
            }
            i++;

        }
        for (int j = 0; j < array.length; j++) {
            System.out.println(array[i]);
        }

    }


}
