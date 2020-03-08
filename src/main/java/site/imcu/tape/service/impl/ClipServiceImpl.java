package site.imcu.tape.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.ClipMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.service.IClipService;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:35
 */
@Service
@Slf4j
public class ClipServiceImpl implements IClipService {
    @Autowired
    ClipMapper clipMapper;
    @Override
    public Integer addClip(Clip clip) {
        return clipMapper.insertClip(clip);
    }

    @Override
    public List<Clip> getRecommendList() {
        return clipMapper.selectRecommendList();
    }
}
