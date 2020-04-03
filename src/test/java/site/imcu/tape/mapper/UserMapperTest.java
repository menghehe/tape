package site.imcu.tape.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.imcu.tape.pojo.User;

import java.util.List;

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
}