package site.imcu.tape.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: MengHe
 * @date: 2020/2/22 17:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Clip extends Base {
    private Integer id;
    private String title;
    private String clipPath;
    private String sourceFile;
    private String thumbnail;
    private Boolean transcoded;
    private User user;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;

}
