package site.imcu.tape.service;

import site.imcu.tape.pojo.Clip;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:33
 */
public interface IClipService {

    /**
     * 添加新的短视频
     * @param clip clip
     * @return result
     */
    Integer addClip(Clip clip);
}
