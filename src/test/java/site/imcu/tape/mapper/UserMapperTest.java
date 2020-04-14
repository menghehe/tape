package site.imcu.tape.mapper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.imcu.tape.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: MengHe
 * @date: 2020/4/3 15:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void selectMapPage() {
        Page<User> page = new Page<>();
        User user = new User();
        IPage<User> users = userMapper.selectPage(page,user);
        System.out.println(JSONObject.toJSON(users));
    }

    @Test
    public void read(){

        List<SubTable> subTableList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ExcelReader reader = ExcelUtil.getReader("C:\\Users\\root\\Desktop\\2019年ns样式凭证.xlsx",i);
            subTableList.addAll(reader.readAll(SubTable.class));
        }
//
//        int i = 0;
//        int part = 0;
//        List<MainTable> tmpList = new ArrayList<>();
//        for (MainTable mainTable : subTableList) {
//            i++;
//            tmpList.add(mainTable);
//            if (i==25000){
//                BigExcelWriter writer= ExcelUtil.getBigWriter(StrUtil.format("e:/result_part{}.xlsx", part));
//                writer.write(tmpList);
//                writer.close();
//                i=0;
//                part++;
//                tmpList.clear();
//            }
//        }

        BigExcelWriter writer= ExcelUtil.getBigWriter(StrUtil.format("e:/subresult.xlsx"));
        writer.write(subTableList);
        writer.close();




    }
}