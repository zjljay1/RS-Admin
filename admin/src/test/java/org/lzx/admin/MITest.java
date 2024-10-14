package org.lzx.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MITest {


    @Test
    public void MapTest() {
        Map<String,String> map = new HashMap<>();
        map.put("a","a");
        map.put("a","b");
//        输出map
        System.out.println(map.get("a"));
    }

}
