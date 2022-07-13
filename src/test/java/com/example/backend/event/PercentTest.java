//package com.example.backend.event;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class PercentTest {
//    public static void main(String[] args) {
//        double tmpRandom = (Math.random() * 100);
//        double tmpRatePrev = 0, tmpRateNext = 0;
//        int result = 0;
//        //소수 둘째자리까지 절삭
//        tmpRandom = Math.round(tmpRandom * 100) / 100.0;
//
//        System.out.println("설정된 랜덤값" + tmpRandom);
//
//        HashMap<String, String> map = new HashMap<String, String>();
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
//
//        map.put("rate", "20");
//        map.put("value", "100");
//        list.add(map);
//        //map.clear();
//        //처음에 clear를 썼는데, list에 삽입된 map까지 clear 해버려서 에러가 났다. 새로 선언해줘야함!
//        map = new HashMap<String, String>();
//
//        map.put("rate", "20");
//        map.put("value", "200");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "20");
//        map.put("value", "300");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "10");
//        map.put("value", "500");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "10");
//        map.put("value", "1000");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "10");
//        map.put("value", "1500");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "5");
//        map.put("value", "2000");
//        list.add(map);
//        map = new HashMap<String, String>();
//
//        map.put("rate", "5");
//        map.put("value", "3000");
//        list.add(map);
//        map.clear();
//        map = new HashMap<String, String>();
//
//
//        for(int i = 0; i < list.size(); i++) {
//            if(tmpRandom == 100) {
//                //만약 난수가 100이라면 가장 마지막에있는 list 인덱스에 있는 value 적용
//                result = Integer.parseInt(list.get(list.size()-1).get("value"));
//                break;
//            } else {
//                double rate = Double.parseDouble(list.get(i).get("rate"));
//                tmpRateNext = tmpRatePrev + rate;
//                if(tmpRandom >= tmpRatePrev && tmpRandom < tmpRateNext) {
//                    result = Integer.parseInt(list.get(i).get("value"));
//                    break;
//                } else {
//                    tmpRatePrev = tmpRateNext;
//                }
//            }
//        }
//
//        System.out.println(result + " 원에 당첨되었습니다.");
//
//    }
//}
